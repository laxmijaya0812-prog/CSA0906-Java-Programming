# Task 2.3 – Handler Logic (Pseudocode)

> Style: structured pseudocode with ALGORITHM / INPUT / OUTPUT / BEGIN…END, numbered steps, `←` for assignment, and `IF … THEN … ELSE … END IF`.
> (Align keywords with the *SIMATS Engineering – Standard for Algorithmic Pseudocode* document supplied by your faculty if it differs.)

## (a) Refresh the doctor list when the department changes

```
ALGORITHM  RefreshDoctorList
EVENT      itemStateChanged(event)  on departmentBox
INPUT      event, doctorMap : Map<String, List<Doctor>>
OUTPUT     doctorBox refilled, slotBox cleared, statusLabel updated

BEGIN
 1. IF event.stateChange ≠ SELECTED THEN
 2.     RETURN                      // ignore the "deselected" event that Swing also fires
 3. END IF
 4. dept ← departmentBox.selectedItem
 5. IF dept = NULL OR dept is the prompt "-- Select --" THEN
 6.     doctorBox.removeAllItems()
 7.     slotBox.removeAllItems()
 8.     RETURN
 9. END IF
10. doctors ← doctorMap.get(dept)                // O(1) average
11. doctorBox.removeAllItems()
12. slotBox.removeAllItems()
13. IF doctors = NULL OR doctors is empty THEN
14.     statusLabel.text ← "No doctors available in " + dept
15.     RETURN
16. END IF
17. FOR EACH doctor IN doctors DO                 // m_d doctors of this department
18.     doctorBox.addItem(doctor)
19. END FOR
20. statusLabel.text ← ""
END
```

## (b) Validate and book the appointment

```
ALGORITHM  BookAppointment
EVENT      actionPerformed(event)  on bookButton
INPUT      patientIdField, departmentBox, doctorBox, slotBox
OUTPUT     confirmation or error message in statusLabel

BEGIN
 1. patientId ← trim(patientIdField.text)
 2. dept      ← departmentBox.selectedItem
 3. doctor    ← doctorBox.selectedItem
 4. slot      ← slotBox.selectedItem
 5. IF patientId is empty THEN
 6.     statusLabel.text ← "Error: Patient ID is required";  RETURN
 7. END IF
 8. IF patientId does not match pattern [A-Za-z0-9]{4,12} THEN
 9.     statusLabel.text ← "Error: Patient ID format invalid";  RETURN
10. END IF
11. IF dept = NULL OR doctor = NULL OR slot = NULL THEN
12.     statusLabel.text ← "Error: choose department, doctor and time slot";  RETURN
13. END IF
14. bookingInProgress ← TRUE
15. disable bookButton
16. statusLabel.text ← "Booking…"
17. START background task (SwingWorker):
18.        IF slot is already booked for doctor THEN
19.            RESULT ← error "Slot already taken"
20.        ELSE
21.            save appointment(patientId, doctor, slot)
22.            RESULT ← confirmation "Appointment booked with Dr. X at <slot>"
23.        END IF
24. ON COMPLETION (back on Event Dispatch Thread):
25.        statusLabel.text ← RESULT
26.        bookingInProgress ← FALSE
27.        enable bookButton
28.        IF RESULT is confirmation THEN clear form fields
END
```

## (c) Close-window guard (supports the "ask before closing" requirement)
```
ALGORITHM  HandleWindowClosing
EVENT      windowClosing(event)
BEGIN
 1. IF bookingInProgress THEN
 2.     answer ← showConfirmDialog("A booking is in progress. Close anyway?")
 3.     IF answer ≠ YES THEN RETURN
 4. END IF
 5. frame.dispose()
END
```
