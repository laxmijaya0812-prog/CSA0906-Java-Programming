# Task 1 – GUI Design Review (K4 – Analyse)
**Student:** Lakshmi Devi J | **Register No:** 192524300 | **Course:** Programming in Java (CSA09)

## Objective
Diagnose the poorly designed `FeeWindow`, redesign it so the GUI only collects input and shows results, and justify the redesign.

## Task 1.1 – Diagnose the Design (2 marks)

| # | Deficiency | What is wrong / why | What the user experiences | Correction |
|---|-----------|--------------------|---------------------------|-----------|
| 1 | **No separation of interface and logic** | Validation, SQL, JDBC connection and output are all inside `actionPerformed` of the window class. | Window code cannot be unit-tested or reused; any change to payment rules means editing GUI code; bugs in one area break the other. | Move rules into `FeeService`; GUI only reads fields and displays results. |
| 2 | **String compared with `==`** | `e.getActionCommand() == "Pay"` compares object references, not contents. It may work by chance for literals but fails for strings created at run time. | The Pay button may do **nothing** (or fall into the `else` branch and clear the fields). | Use `"Pay".equals(cmd)`, or better – give each button its own listener / source check `e.getSource() == pay`. |
| 3 | **No handling of empty/invalid input** | `Double.parseDouble(amount.getText())` throws `NumberFormatException` for empty or text input; negative/zero amounts and empty register number are accepted. | Nothing visible happens (exception in console only), or a wrong record such as amount `-500` is stored. | Validate in `FeeService` and report a clear message. |
| 4 | **Database work on the Event Dispatch Thread** | JDBC connect + insert run inside the listener, i.e. on the EDT. | The whole window freezes while the database is slow or unreachable. | Run the work in a `SwingWorker` (background thread) and update the GUI in `done()`. |
| 5 | **`Thread.sleep(3000)` on the EDT** | Sleeping inside a listener blocks event processing. | For 3 seconds the window cannot repaint, buttons do not respond, and the OS may show "Not Responding". | Never sleep on the EDT; simulate/wait for confirmation in a background thread. |
| 6 | **No feedback to the user** | Result is printed with `System.out.println("Paid")` – a console message a student never sees. | The user does not know whether the payment succeeded or failed and may press Pay twice (double payment). | Show a status label / dialog; disable Pay while processing. |
| 7 | **Receipt button does nothing** | `receipt` is declared but never added to the frame and never handled; any click that reached the listener would run the `else` branch and clear the fields. | Button missing, or clicking it erases the entered data. | Add it to the layout, handle it with its own listener, enable it only after a successful payment. |
| 8 | **SQL injection** | SQL is built by concatenating `regNo.getText()`. Input like `x'); DROP TABLE fees;--` changes the statement. | A malicious or even accidental apostrophe (e.g. `O'Neil`) breaks the query or damages data. | Use `PreparedStatement` with `?` parameters. |
| 9 | **Window cannot be closed / no close handling** | No `WindowListener`, no `setVisible(true)`, no `setSize`; an AWT `Frame` ignores the close button. | The user clicks X and nothing happens; the program must be killed. | Add a `WindowAdapter.windowClosing` that disposes the frame (and asks for confirmation if a payment is running). |
| 10 | **Database connection never closed** | `Connection` and `Statement` are never closed. | Each payment leaks a connection; after many payments the database refuses new connections. | try-with-resources around `Connection` and `PreparedStatement`. |
| 11 | **Checked exceptions not handled** | `DriverManager.getConnection`, `executeUpdate` throw `SQLException`; `Thread.sleep` throws `InterruptedException`. The code does not compile as written, and a "fix" with an empty `catch` would hide errors. | Either the program does not compile or failures are silent. | Catch/propagate them, convert them to a readable message for the user. |
| 12 | **Every button shares one listener with an else-branch** | Any button other than "Pay" (including Receipt) falls into `else` and clears the fields. | Pressing Receipt wipes the form. | One listener per button (Task 1 alternative). |

(12 deficiencies identified; the assignment requires at least five.)

## Task 1.2 – Repair the Design (3 marks)

### Class structure
```
FeeWindow  (JFrame – GUI layer)
   │  uses
   ▼
FeeService (business layer: validation + payment + last receipt)
   │  uses (interface)
   ▼
FeeRepository  <<interface>>  ──implemented by──►  JdbcFeeRepository   (MySQL, PreparedStatement)
                                                └►  InMemoryFeeRepository (demo, no database)
```
Supporting types: `FeeService.ValidationException` (user-correctable input error), `FeeService.Receipt` (record).

### Rewritten event handling
```java
pay.addActionListener(e -> startPayment());          // one listener per button
clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); });
receipt.addActionListener(e -> showReceipt());

private void startPayment() {
    setBusy(true);                                   // disable Pay, show "Processing..."
    new SwingWorker<FeeService.Receipt, Void>() {
        @Override protected FeeService.Receipt doInBackground() throws Exception {
            return service.pay(reg, amt);            // validate + bank wait + save (NOT on the EDT)
        }
        @Override protected void done() {            // runs on the EDT
            setBusy(false);
            try { show success; enable Receipt; }
            catch (ExecutionException ex) { show error message from ex.getCause(); }
        }
    }.execute();
}
```
Source files: `FeeWindow.java`, `FeeService.java`, SQL script `fees.sql`.

### How the earlier deficiencies were fixed
| Deficiency | Fix in the redesign |
|---|---|
| 1 | GUI in `FeeWindow`, rules in `FeeService`, SQL in `JdbcFeeRepository`. |
| 2, 12 | No action-command strings at all – each button has its own listener. |
| 3 | `FeeService.validate()` checks empty, format (6–15 letters/digits), numeric and > 0. |
| 4, 5 | `SwingWorker`; the 2-second bank wait runs in the background thread. |
| 6 | Status label, success/error dialogs, Pay disabled while busy. |
| 7 | Receipt button added, enabled only after success, shows the stored `Receipt`. |
| 8 | `PreparedStatement` with `?`. |
| 9 | `WindowAdapter.windowClosing` with confirmation if a payment is in progress. |
| 10 | try-with-resources closes `Connection` and `PreparedStatement`. |
| 11 | Exceptions travel through `SwingWorker.get()` (as `ExecutionException`) and are shown as readable messages. |
| – | DB credentials come from environment variables (`FEE_DB_URL`, `FEE_DB_USER`, `FEE_DB_PASSWORD`), not from source code. |

## Task 1.3 – Design Defence (≤ 100 words)
> The redesigned window only reads text fields and displays messages, while `FeeService` holds all validation and payment rules. Because `FeeService` depends on a `FeeRepository` interface, I can test it without a window or a database by passing a fake repository. A rule change, such as a new minimum amount, touches only one class. Each button has its own small listener, so changing Receipt cannot break Pay. The `SwingWorker` keeps the window responsive, and `PreparedStatement` plus try-with-resources keep the database code safe and short.

(Word count: 83.)

## Alternative Concept and Complexity Analysis

**Alternative:** one listener with an `if–else` chain on the action command (original) versus **one listener object (or Swing `Action`) per button**.

*Solution with per-button listeners / Actions:*
```java
Action payAction = new AbstractAction("Pay") {
    @Override public void actionPerformed(ActionEvent e) { startPayment(); }
};
pay.setAction(payAction);            // the same Action could also be used by a menu item
```
```
PSEUDOCODE  dispatch (single listener)         PSEUDOCODE  dispatch (per-button listener)
  cmd ← event.actionCommand                      listener ← event.source.registeredListener
  IF cmd = "Pay"     THEN …                      CALL listener.actionPerformed(event)
  ELSE IF cmd = "Clear" THEN …
  ELSE IF cmd = "Receipt" THEN …
```

| Aspect | Single listener + if–else on k buttons | One listener/Action per button |
|---|---|---|
| Time to dispatch an event | Up to **k string comparisons**: O(k) worst case (each comparison also costs O(length of string)) | The button already knows its listener: **O(1)** |
| Space | 1 listener object: O(1) | k listener objects: **O(k)** (about a few dozen bytes each) |
| Maintainability | Adding a button means editing a growing chain; one mistake affects all buttons | Each handler is independent and testable |
| Reuse | Hard | An `Action` can serve button, menu item and key shortcut |

**Conclusion:** the extra O(k) memory is negligible (k is small in a GUI), while O(k)→O(1) dispatch and much better maintainability make per-button listeners the better design. In practice, for k ≤ 10 the time difference itself is unmeasurable; the real advantage is design quality.

## Testing (Task 1.2)
| # | Input | Expected result |
|---|-------|----------------|
| T1 | Reg `192524300`, amount `25000` → Pay | "Processing…" for about 2 s (window still movable), then "Payment successful!" dialog; Receipt button becomes enabled |
| T2 | Empty reg no → Pay | Error dialog "Register number must not be empty." |
| T3 | Amount `abc` → Pay | Error dialog "Amount must be a number, e.g. 25000.50" |
| T4 | Amount `-5` → Pay | "Amount must be greater than zero." |
| T5 | Press Receipt after T1 | Receipt dialog with register no, amount, time |
| T6 | Press Receipt before any payment | Button is disabled (cannot be pressed) |
| T7 | Press Clear | Both fields emptied |
| T8 | Press X while processing | Confirmation "A payment is in progress. Close anyway?" |
| T9 | Drag the window while "Processing…" is shown | Window moves smoothly – proves the EDT is not blocked |

> Expected outputs above are what the program is designed to produce; the real evidence is the screenshots you capture.

## Screenshot evidence required
See `../SCREENSHOTS_CHECKLIST.md` (files `Task01/screenshots/t1_*.png`).

## Result
All deficiencies were diagnosed and a layered, non-blocking, injection-safe `FeeWindow`/`FeeService` design was produced.
