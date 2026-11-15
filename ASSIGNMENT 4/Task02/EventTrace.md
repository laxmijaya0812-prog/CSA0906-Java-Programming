# Task 2.4 – Trace the Event Sequence

## Trace: from opening the window to closing it
(`AppointmentFrame extends JFrame`)

| Step | User action / system | Method call(s) in order | Result |
|---|---|---|---|
| 1 | Program starts | `main()` → `SwingUtilities.invokeLater(...)` | Swing work is moved to the Event Dispatch Thread (EDT) |
| 2 | Frame created on EDT | `new AppointmentFrame()` → constructor: create components, `add(...)`, register listeners, `pack()` | Window is built but not visible |
| 3 | Shown | `setVisible(true)` | Native window created |
| 4 | Window events on open | `windowOpened(WindowEvent)` → `windowActivated(WindowEvent)`; first `paint/paintComponent` calls | Doctor data loaded by `SwingWorker`; window painted |
| 5 | Receptionist types patient ID | `DocumentListener.insertUpdate()` for each character | "Booking in progress" flag set |
| 6 | Chooses department | `itemStateChanged(DESELECTED)` (old item), `itemStateChanged(SELECTED)` (new item) → *RefreshDoctorList* | Doctor list refilled |
| 7 | Chooses doctor | `actionPerformed()` on doctorBox | Slots loaded |
| 8 | Chooses slot | `actionPerformed()` on slotBox | Book enabled |
| 9 | Presses Book | `actionPerformed()` on bookButton → `SwingWorker.execute()` → `doInBackground()` (worker thread) → `done()` (EDT) | Confirmation/error shown |
| 10 | Minimise / restore (optional) | `windowIconified()`, `windowDeactivated()` … `windowDeiconified()`, `windowActivated()`, then `paint()` | Window repainted |
| 11 | Clicks X | `windowClosing(WindowEvent)` – confirms if booking in progress | Stays open (NO) or continues (YES) |
| 12 | `dispose()` called | `windowClosed(WindowEvent)` (after native resources released) | Window gone |
| 13 | Last window gone | JVM exits (if `System.exit(0)` or no non-daemon threads remain) | Program ends |

Short form:
```
main → invokeLater → constructor → setVisible → windowOpened → windowActivated → paint
   → (item/action/document events…) → windowClosing → dispose → windowClosed
```

## Comparison with the applet life cycle

| Applet method | When called | Closest window-event equivalent | Note |
|---|---|---|---|
| `init()` | Once, when the applet is first loaded | Constructor (+ `windowOpened`) | One-time set-up of components and data |
| `start()` | After `init()` and every time the applet page becomes visible again | `windowActivated` / `windowDeiconified` | Resume work such as animation |
| `paint(Graphics)` | Whenever the applet must be redrawn | `paint()` / `paintComponent()` (same) | Called by the system, never directly |
| `stop()` | When the page is left / hidden | `windowDeactivated` / `windowIconified` | Pause work |
| `destroy()` | Once, when the applet is unloaded | `windowClosing` → `dispose()` → `windowClosed` | Release resources |

Differences: an applet is controlled by a **browser** (container-driven life cycle, no `main`), while a frame application is started by `main` and its window events are **user-driven** (close button, minimise). An applet has no window-closing event because the browser owns the window. Note also that applets are deprecated (removed in recent Java versions), so Swing/AWT frames are used in practice.
