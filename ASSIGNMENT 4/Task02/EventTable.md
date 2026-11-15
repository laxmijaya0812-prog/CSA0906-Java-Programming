# Task 2.1 – Model the Events (Hospital Outpatient Appointment Desk)

## Requirement (interpreted)
Receptionist enters **patient ID**, selects a **department**, chooses a **doctor** (list depends on department), picks a **time slot**, and **books**. The window shows a confirmation or an error and asks for confirmation before closing if a booking is in progress.

## Event table

| # | Event source (component) | Event object generated | Listener interface (method) | Action taken |
|---|---|---|---|---|
| 1 | `JComboBox departmentBox` (user picks a department) | `ItemEvent` (state change SELECTED) – or `ActionEvent` | `ItemListener.itemStateChanged(ItemEvent)` | Look up doctors of the department in `Map<String,List<Doctor>>`, refill `doctorBox`, clear the time slots, clear old message |
| 2 | `JComboBox doctorBox` | `ActionEvent` | `ActionListener.actionPerformed(ActionEvent)` | Load available slots of that doctor into `slotBox` |
| 3 | `JComboBox slotBox` | `ActionEvent` | `ActionListener.actionPerformed` | Remember chosen slot; enable Book button when all fields are filled |
| 4 | `JTextField patientIdField` (typing) | `DocumentEvent` (insert/remove) – or `KeyEvent` | `DocumentListener.insertUpdate / removeUpdate / changedUpdate` | Mark "booking in progress" (form is dirty); re-check whether Book can be enabled |
| 5 | `JButton bookButton` | `ActionEvent` | `ActionListener.actionPerformed` | Validate the form, call `AppointmentService.book(...)` in a `SwingWorker`, show confirmation or error in the status label |
| 6 | `JButton clearButton` | `ActionEvent` | `ActionListener.actionPerformed` | Reset all fields and the "in progress" flag |
| 7 | `JFrame` – window being opened | `WindowEvent` | `WindowListener.windowOpened` | Load departments and doctors once (SwingWorker), focus the patient-ID field |
| 8 | `JFrame` – user clicks the close (X) button | `WindowEvent` (WINDOW_CLOSING) | `WindowListener.windowClosing` (via `WindowAdapter`) | If a booking is in progress → `JOptionPane.showConfirmDialog`; YES → `dispose()`, NO → stay open |
| 9 | `JFrame` – window disposed | `WindowEvent` | `WindowListener.windowClosed` | Release resources (e.g. close pool / stop background workers), exit the JVM |
| 10 | `JFrame` – minimise / restore | `WindowEvent` | `WindowListener.windowIconified / windowDeiconified` | (No special action – listed for completeness) |
| 11 | `JFrame` – gains/loses focus | `WindowEvent` | `WindowListener.windowActivated / windowDeactivated` | (No special action) |
| 12 | `JMenuItem Exit` (optional menu) | `ActionEvent` | `ActionListener.actionPerformed` | Same logic as event 8 |

### Notes
* `WindowAdapter` is used so only `windowOpened`, `windowClosing` and `windowClosed` need to be overridden (the adapter provides empty versions of the other five methods).
* In AWT the equivalents are `Choice` (`ItemListener`), `TextField` (`TextListener`), `Button` (`ActionListener`), `Frame` (`WindowListener`).
* Events 1 and 2 are **dependent**: event 1 changes the model of the doctor list, which then fires its own events when refilled. The handler for event 1 therefore removes the doctor listener, refills the list and re-adds it (or uses a "isRefreshing" flag) so that refilling does not trigger unwanted bookings or slot loads.
