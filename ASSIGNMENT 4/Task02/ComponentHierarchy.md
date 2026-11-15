# Task 2.2 – Component Hierarchy (Containment Tree)

```
JFrame  "Outpatient Appointment Desk"              BorderLayout
 │
 ├── NORTH  : JLabel  "Outpatient Appointment Desk" (title)
 │
 ├── CENTER : JPanel  (formPanel)                  GridLayout(5, 2, 5, 5)
 │     ├── JLabel     "Patient ID"
 │     ├── JTextField patientIdField
 │     ├── JLabel     "Department"
 │     ├── JComboBox<String> departmentBox
 │     ├── JLabel     "Doctor"
 │     ├── JComboBox<Doctor> doctorBox
 │     ├── JLabel     "Time Slot"
 │     ├── JComboBox<String> slotBox
 │     ├── JLabel     "Date"            (optional, if required)
 │     └── JTextField dateField
 │
 ├── SOUTH  : JPanel  (southPanel)                 BorderLayout
 │     ├── CENTER: JPanel (buttonPanel)            FlowLayout
 │     │     ├── JButton bookButton   "Book Appointment"
 │     │     └── JButton clearButton  "Clear"
 │     └── SOUTH : JLabel statusLabel              (confirmation / error)
 │
 └── JMenuBar (optional)
       └── JMenu "File"
             └── JMenuItem "Exit"
```

| Level | Component | Swing class (AWT equivalent) | Purpose |
|---|---|---|---|
| 1 | Top-level window | `JFrame` (`Frame`) | Holds everything; receives `WindowEvent`s |
| 2 | Title | `JLabel` (`Label`) | Heading |
| 2 | Form container | `JPanel` (`Panel`) with `GridLayout` | Aligns labels and inputs in a neat grid |
| 3 | Patient ID | `JTextField` (`TextField`) | Text input |
| 3 | Department / Doctor / Slot | `JComboBox` (`Choice`) | Drop-down selection |
| 2 | Button container | `JPanel` + `FlowLayout` | Centres the buttons |
| 3 | Book / Clear | `JButton` (`Button`) | Actions |
| 2 | Status | `JLabel` | Shows confirmation or error |
| 2 | Menu | `JMenuBar`, `JMenu`, `JMenuItem` (`MenuBar`, `Menu`, `MenuItem`) | Optional Exit command |
| – | Confirm-close / error boxes | `JOptionPane` (`Dialog`) | Dialogs |

Design rule: the doctor `JComboBox` is **dependent** on the department `JComboBox`; the slot box is dependent on the doctor box (Department → Doctor → Slot).
