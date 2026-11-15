# Task 5 – Same Screen, Different Event-Handling Styles (K5 – Evaluate)
**Student:** Lakshmi Devi J | **Register No:** 192524300
Files: `ConverterFrameListener.java`, `ConverterNamedInnerClass.java`, `ConverterAnonymousInnerClass.java`, `ConverterLambda.java`

## Objective
Implement one currency converter (amount field, USD/EUR selector, Convert button, result label) in four event-handling styles, change the requirement, and evaluate the styles. Rates from the PDF: **USD 0.012, EUR 0.011** per INR.

## Task 5.1 – Four Implementations (3 marks)

| Style | File | How the handler is written |
|---|---|---|
| (a) Frame implements `ActionListener` | `ConverterFrameListener.java` | `class … extends JFrame implements ActionListener`; `actionPerformed` in the frame; `addActionListener(this)` |
| (b) Named inner class | `ConverterNamedInnerClass.java` | `private class ConvertListener implements ActionListener {…}`; `new ConvertListener()` |
| (c) Anonymous inner class | `ConverterAnonymousInnerClass.java` | `new ActionListener() { @Override public void actionPerformed(...) {…} }` written where it is registered |
| (d) Lambda | `ConverterLambda.java` | `ActionListener l = e -> convert();` (the PDF's program with TODO 1 and TODO 2 completed) |

All four share the same `convert()` method (parse → multiply by rate → `String.format("%.2f %s")` → show; otherwise "Invalid amount") and all use a `WindowAdapter` for `windowClosing` (prints "Converter closing", then the default close operation exits).

**Compile & run (PowerShell, in `Task05`):**
```powershell
javac Converter*.java
java ConverterFrameListener
java ConverterNamedInnerClass
java ConverterAnonymousInnerClass
java ConverterLambda
```

## Task 5.2 – Change the Requirement (2 marks)
New requirement: (1) pressing **Enter** in the amount field also converts, (2) the result is shown in a **dialog box**.

| Version | Change for Enter key | Change for dialog | Total lines changed / added |
|---|---|---|---|
| (a) Frame listener | add `amount.addActionListener(this);` – 1 line | add `JOptionPane.showMessageDialog(...)` in `convert()` – 1–2 lines (shared) | ≈ 2–3 |
| (b) Named inner class | add `amount.addActionListener(listener);` – 1 line (the same `ConvertListener` object is reused) | same shared line(s) | ≈ 2–3 |
| (c) Anonymous inner class | the anonymous object must first be **stored in a variable** (`ActionListener listener = new …`) so it can be reused – 1 line changed + 1 added | same | ≈ 3–4 (the most restructuring: if two anonymous classes were written instead the whole handler body would be duplicated) |
| (d) Lambda | **0 lines extra** if TODO 1 already registers one listener for both sources (as the PDF requires); otherwise 1 line | same | ≈ 1–2 |

Why the changes are small: every version delegates to the single `convert()` method, so the dialog change is made once in `convert()`. If the logic had been written *inside* each handler, (a) would need conditions to tell the sources apart and (c) would duplicate code.

> **Please measure it yourself:** first commit each converter *without* Enter/dialog (set `SHOW_DIALOG = false` and comment out the `amount.addActionListener(...)` line), then enable them and run `git diff --stat`. Record the real numbers in this table. The values above are my expected values from reading the code.
> The shipped files contain the final (Task 5.2) behaviour; `SHOW_DIALOG = false` restores the plain Task 5 behaviour.

## Task 5.3 – Compare the Styles (2 marks)

| Criterion | (a) Frame implements | (b) Named inner class | (c) Anonymous inner class | (d) Lambda |
|---|---|---|---|---|
| Readability | Poor when the frame has many sources: one `actionPerformed` must test `e.getSource()` | Good – the handler has a name and its own place | Good for short handlers; the code sits next to the registration, but gets cluttered when long | **Best** for one-line handlers (`e -> convert()`); less clear if the body is long |
| Access to frame's fields | Direct (it *is* the frame) | **Direct, even to private fields** (inner class) | Direct (inner class) and to `final`/effectively final locals | Direct to fields via the enclosing `this` and to effectively final locals; `this` refers to the frame |
| Reuse of the handler | Low: all components share one method; the frame cannot be reused by others without exposing the interface | **High**: one named object can be registered on many components and reused or unit-tested | Low: written inline; reuse only through a variable | Medium: reuse through a variable or method reference (`this::convert`) |
| Ease of change | Hard: every new source adds an `if` in the shared method; a frame that publicly "is an ActionListener" leaks its API | Easy: add a new inner class | Easy locally; duplication risk | Easy; very little code to alter |
| Extra files / classes | None (+ interface in the class header) | One `.class` for the inner class | One `.class` per anonymous class | None (compiled with `invokedynamic`) |
| Works for interfaces with several methods (e.g. `WindowListener`) | Must implement all methods | Must implement all (or extend the adapter) | Works with adapters | **Not possible** for multi-method interfaces – use an adapter class (that is why `WindowAdapter` is used) |

**Recommendation for a large application:** a **named inner (or top-level) handler class / Swing `Action`** that calls a method of a separate controller or service class, with short **lambdas** for one-line handlers that simply delegate (`e -> controller.convert()`).
Reasons: separate named classes can be reused, tested and read in isolation; the frame does not turn into a huge `actionPerformed` with an `if` chain; lambdas keep trivial wiring short. Having the frame implement the listener (a) does not scale.

## Complete the Code – status (lambda version)
| TODO | Completed with |
|---|---|
| TODO 1 | `ActionListener convertListener = e -> convert();` then `convertButton.addActionListener(convertListener); amount.addActionListener(convertListener);` – ONE lambda for both sources |
| TODO 2 | `Double.parseDouble(amount.getText().trim())` → `String.format("%.2f %s", inr * rate(currency), currency)`; `NumberFormatException` → "Invalid amount" (negative amounts are also treated as invalid) |

The rest of the PDF code is unchanged except for the additions required by the tasks (the `WindowAdapter`, the dialog, a `pack()` after the result so the longer text fits).

### Test cases
| # | Input | Expected |
|---|---|---|
| C1 | 1000, USD, press Convert | Label `12.00 USD` (+ dialog `12.00 USD`) |
| C2 | 1000, EUR, press Enter in the field | `11.00 EUR` |
| C3 | 2500.5, USD | `30.01 USD` (30.006 rounds to 30.01) |
| C4 | `abc` | `Invalid amount` (+ error dialog) |
| C5 | empty field | `Invalid amount` |
| C6 | `-50` | `Invalid amount` |
| C7 | close with X | console prints `Converter closing`, program ends |

## Alternative Concept and Complexity Analysis
**Alternative:** `convertButton.addActionListener(e -> convert())` as a *method reference* `this::convert` is not directly possible (the method must accept an `ActionEvent`): the shared method can be declared `private void convert(ActionEvent e)` and registered with `this::convert`. Or define **one shared `Action` object**:
```java
Action convertAction = new AbstractAction("Convert") {
    { putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control ENTER")); }
    @Override public void actionPerformed(ActionEvent e) { convert(); }
};
convertButton.setAction(convertAction);       // button
convertMenuItem.setAction(convertAction);     // menu item
amount.addActionListener(convertAction);      // Enter key
// keyboard shortcut: inputMap/actionMap of the root pane
```

| Aspect | (a) Frame | (b) Named inner | (c) Anonymous | (d) Lambda | Method reference | Shared `Action` |
|---|---|---|---|---|---|---|
| Listener objects created (space) for T triggers | 1 (the frame) | 1 if one instance is reused, otherwise T | 1 if stored in a variable, otherwise T | 1 if stored, otherwise T (non-capturing lambdas may be cached by the JVM) | 1 per reference (small) | **1 for all T triggers**: O(1) |
| Work to add a new trigger | add `addActionListener(this)` + an `if` for the source: O(1) lines but grows the method | 1 line | 1 line (variable needed) | 1 line | 1 line | **1 line** (`setAction(...)`), and enable/disable, text and icon stay synchronised automatically |
| Enabling/disabling the feature | each trigger separately | each separately | each separately | each separately | each separately | **once** (`action.setEnabled(false)` disables button, menu item and shortcut) |
| Dispatch time | O(1) | O(1) | O(1) | O(1) | O(1) | O(1) |

**Conclusion:** the four styles do the same work at run time (O(1) dispatch, a few hundred bytes per listener); they differ in maintainability. A shared `Action` has the lowest cost of adding triggers (one line, one object) and is the best fit for an application with a menu, toolbar and shortcuts.

## Screenshot evidence required
`t5_<style>_valid.png`, `t5_<style>_invalid.png` for the lambda version at least (valid conversion + invalid amount), `t5_dialog.png`, `t5_enter_key.png` (see `../SCREENSHOTS_CHECKLIST.md`).

## Result
Four working implementations, a measured/estimated change effort, and a reasoned comparison recommending named handler classes/`Action`s with lambda delegation for large applications.
