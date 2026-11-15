# SIMATS ENGINEERING – PROGRAMMING IN JAVA (CSA09)
# Assignment 4 – CO4: Event-Driven GUI Design – Report

**Name:** Lakshmi Devi J  **Register No:** 192524300  **Course:** Programming in Java (CSA09)
**GitHub Repository Link:** https://github.com/<your-username>/CSA09-192524300-Assignment4  *(replace with your real link)*

> How to use this file: it gives the report skeleton with the required headings and the key content of every task. Copy the detailed tables, diagrams and pseudocode from the `TaskNN_Answers.md` files (and Task 2 files) where indicated, and insert your own screenshots with a one-sentence caption naming the requirement each proves.

---
## Task 1 – GUI Design Review (K4, 6 marks)
**Objective:** diagnose and repair the poorly designed `FeeWindow`.
**Problem analysis:** one listener handles every button; GUI, validation, SQL and output are mixed; the database call and `Thread.sleep(3000)` run on the Event Dispatch Thread (EDT).
**Required answer:** *1.1* twelve deficiencies with user impact and corrections (see `Task01/Task01_Answers.md`, table in 1.1). *1.2* class structure `FeeWindow → FeeService → FeeRepository (JDBC / in-memory)`; rewritten event handling (one listener per button, `SwingWorker`). *1.3* design defence (83 words).
**Class design:** `FeeWindow` (JFrame, GUI only), `FeeService` (validation, payment, receipt), `FeeRepository` interface, `JdbcFeeRepository` (PreparedStatement, try-with-resources), `InMemoryFeeRepository`, `ValidationException`, `Receipt` record.
**Code explanation:** the Pay listener disables the button, starts a `SwingWorker`; `doInBackground()` calls `service.pay()` (validate → simulated 2-second bank wait → save); `done()` runs on the EDT and shows success or the error message; Receipt is enabled only after success; `WindowAdapter` asks for confirmation if a payment is running.
**Alternative concept:** one listener + `if–else` (O(k) comparisons, O(1) space) vs per-button listeners / `Action` (O(1) dispatch, O(k) objects) – table in `Task01_Answers.md`.
**Time/space:** as above; difference immeasurable for small k – the design benefit dominates.
**Testing / expected output:** tests T1–T9 (valid payment, empty reg no, text amount, negative, Receipt, Clear, close during payment, drag window while processing).
**Screenshot evidence:** `Task01/screenshots/t1_*.png`.
**Result:** a layered, non-blocking, injection-safe fee window.

## Task 2 – Hospital Outpatient Appointment Desk (K4, 8 marks)
**Objective:** design an event-driven window from a requirement.
**Problem analysis:** receptionist enters patient ID, selects department → doctors (dependent) → slot → books; confirmation/error; confirm before close during a booking.
**Required answer:** event table (`EventTable.md`), component hierarchy (`ComponentHierarchy.md`), pseudocode for department-change refresh and for validate-and-book (`HandlerPseudocode.md`), event trace and applet comparison (`EventTrace.md`).
**Alternative concept:** `Map<String, List<Doctor>>` loaded once – lookup O(1), memory O(d + m) – versus query on every change – O(log N + m_d) plus latency, memory O(m_d), always fresh (`Task02_Answers.md`).
**Testing / expected output:** design task; check the trace against a program of your own if desired.
**Screenshot evidence:** optional rendering of the tree/table.
**Result:** complete design with correct trace `main → invokeLater → constructor → setVisible → windowOpened → windowActivated → … → windowClosing → dispose → windowClosed`.

## Task 3 – Campus Library Search Screen (K4, 9 marks)
**Objective:** choose layout managers and build a screen whose results area grows on resize.
**Required answer:** layout table and justification (3.1), nested container diagram (3.2), `LibrarySearch.java` (3.3), three resizing tests with explanations (3.4) – see `Task03/Task03_Answers.md`.
**Class design:** `LibrarySearch extends JFrame`; BorderLayout (frame, bottom panel), GridLayout 2×1 (top), FlowLayout (search row, button row), `JScrollPane` + `JList` in CENTER.
**Alternative concept:** GridBagLayout (flexible, verbose, O(n) with larger constant), absolute positioning (O(1) layout but no resizing) – comparison table.
**Testing / expected output:** `library_normal/enlarged/reduced.png`; only the results region changes size.
**Result:** requirement met (to be confirmed by your screenshots).

## Task 4 – Event-Handling Forensics (K4, 7 marks)
**Objective:** find and fix the defects in `DrawPad`.
**Required answer:** 4.1 `mouseclicked` ≠ `mouseClicked` – handler never called, label never updated; 4.2 dots drawn with `getGraphics()` vanish on repaint; 4.3 stored `List<Point>`, `paint()` redraws, `@Override mouseClicked`; 4.4 `@Override` gives the compile error *"method does not override or implement a method from a supertype"*.
**Complete the Code:** TODO 1 – `points.add(e.getPoint()); status.setText(...); repaint();` TODO 2 – `super.paint(g); for (Point p : points) g.fillOval(p.x-3, p.y-3, 6, 6);`
**Alternative concept:** `BufferedImage` – repaint O(W×H) copy independent of n, space W×H×4 bytes, versus points – repaint O(n), space O(n) (`Task04_Answers.md`).
**Testing / expected output:** five clicks → five dots, "Clicks: 5"; minimise/restore → unchanged.
**Screenshot evidence:** `t4_*.png`.
**Result:** both defects explained and repaired.

## Task 5 – Same Screen, Different Event-Handling Styles (K5, 7 marks)
**Objective:** implement and evaluate four listener styles.
**Required answer:** four programs (frame listener, named inner class, anonymous inner class, lambda), Enter key + dialog (5.2) with a change-effort table, comparison of readability / field access / reuse / ease of change (5.3), recommendation: named handler classes or `Action`s that delegate to a controller, short lambdas for trivial wiring.
**Complete the Code:** lambda TODO 1 – one `ActionListener` registered on the button and the text field; TODO 2 – parse, `%.2f`, "Invalid amount".
**Alternative concept:** shared `Action` – one object for button, menu, shortcut (O(1) objects, one line per new trigger) vs T listener objects.
**Testing:** 1000 INR → `12.00 USD`, → `11.00 EUR`, `abc` → "Invalid amount", Enter key, dialog.
**Result:** four working implementations and an evaluated comparison (measure your own diff with `git diff --stat`).

## Task 6 – Campus Event Registration Desktop Application (K5, 13 marks)
**Objective:** design and build a layered Swing–JDBC–MySQL application.
**Required answer:** Stage 1 requirements R1–R9; Stage 2 screens, menu bar and flow chart; Stage 3 decisions (validation in service, duplicate check + UNIQUE constraint, DAO-only SQL, SwingWorker); Stage 4 class diagram (Mermaid) and Register/Filter pseudocode; Stage 5 implementation and tests – see `Task06/Task06_Answers.md`.
**Class design:** `Main → RegistrationFrame → RegistrationService → RegistrationDAO → DatabaseConnection → MySQL`; models `Registration`, `EventInfo`.
**Code explanation:** `runAsync()` wraps each service call in a `SwingWorker` and updates the GUI in `done()`; the DAO uses PreparedStatements, try-with-resources and a transaction; the service maps SQL error 1062 to "Student already registered for this event."; export writes CSV in the background.
**Alternative concept:** load all 100 000 rows (O(N) time, ≈50–100 MB, sluggish) vs `LIMIT/OFFSET` pages (O(p) memory, immediate; deep pages O(offset)) – table in the answers file.
**Testing / expected output:** T1–T9 in the answers file (database tests must be run on your machine).
**Screenshot evidence:** `Task06/screenshots/t6_*.png`.
**Result:** all functional requirements implemented; fill in your observed results after running.

---
## Declaration of what was executed
Code was compiled with JDK 21 (all tasks compile without errors); the validation logic of `FeeService` and `RegistrationService` was exercised with a small headless test; GUI behaviour and MySQL-dependent behaviour **must be run and screenshotted by the student**. Expected outputs in this report are design expectations, not recorded evidence.
