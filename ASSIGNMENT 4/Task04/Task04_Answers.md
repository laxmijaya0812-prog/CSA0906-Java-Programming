# Task 4 – Event-Handling Forensics (K4 – Analyse)
**Student:** Lakshmi Devi J | **Register No:** 192524300
Files: `DrawPad.java` (repaired + Complete-the-Code), `DrawPadBuggy.java` (original faulty program, for reproducing the defects).

## Objective
Find why the original drawing pad neither draws dots nor counts clicks, find a second defect that appears after the first is fixed, repair the program and prevent the mistake from recurring.

## Task 4.1 – Locate the Defect (2 marks)
**Defect:** the method is named `mouseclicked` (lower-case **c**). `MouseAdapter` defines `mouseClicked` (capital **C**). Java is case-sensitive, so `mouseclicked(MouseEvent)` is a *new, unrelated method* – it **overloads nothing and overrides nothing**. The compiler accepts it because there is no `@Override` to check it.

* **Why the click is never handled:** when the user clicks, the event system calls `mouseClicked(e)` on the listener. That method is the empty one inherited from `MouseAdapter`, so nothing happens. `mouseclicked` is never called by anyone.
* **Why the label never changes:** the label update (and `count++`) sit inside the misspelled method, which never runs. In addition the original code never calls `status.setText(...)`, even inside `mouseclicked`, so even a correct method name would not update the label.

## Task 4.2 – Construct a Failing Interaction (2 marks)
**Interaction (after fixing the method name only):**
1. Run the program and click five times at different places – five dots are drawn.
2. **Minimise** the window and **restore** it (or drag another window over it, or resize it).
3. The dots are gone, but the label still says "Clicks: 5".

**Why:** the dot was drawn with `getGraphics()` – a temporary graphics context – directly onto the screen. Nothing remembers the dot. When the window is minimised/restored or uncovered, the system calls `paint(Graphics)`, which by default only paints the background and components (the label), so everything that was drawn earlier is erased. In AWT/Swing, *all* drawing must be re-creatable in `paint()` from stored data (the **model**). The fix is to store the points and draw them in `paint()`.

## Task 4.3 – Repair the Program (2 marks)
```java
private final List<Point> points = new ArrayList<>();

@Override public void mouseClicked(MouseEvent e) {
    points.add(e.getPoint());                       // store the point
    status.setText("Clicks: " + points.size());     // update the label
    repaint();                                      // ask the system to call paint()
}

@Override public void paint(Graphics g) {
    super.paint(g);                                 // paints the label
    for (Point p : points) g.fillOval(p.x - 3, p.y - 3, 6, 6);   // 6x6 circle centred on the point
}
```
Complete listing: `DrawPad.java`. The only code changed from the PDF's template is inside the two TODO blocks; nothing else was modified.

**Compile & run (PowerShell, in `Task04`):**
```powershell
javac DrawPad.java
java DrawPad
```
Expected: a click draws a dot centred on the pointer, label shows "Clicks: n"; after minimise/restore, all dots are still shown and the label is unchanged. Closing the window with X disposes it (the JVM ends because no windows remain).

## Task 4.4 – Prevent Recurrence (1 mark)
`@Override` tells the compiler "this method must override a method of a supertype". For `@Override public void mouseclicked(MouseEvent e)` the compiler finds no such method in `MouseAdapter` and stops with an error. **Real compiler output** (JDK 21, produced by adding `@Override` to `DrawPadBuggy.java`):
```
DrawPadBuggy.java:18: error: method does not override or implement a method from a supertype
            @Override public void mouseclicked(MouseEvent e) {
            ^
1 error
```
So the typing error is caught at **compile time**, at the exact line, instead of appearing as silent run-time misbehaviour. (**You must repeat this experiment yourself** and screenshot your own compiler message – see below.)

### Experiment to perform (required by the Complete-the-Code section)
1. In `DrawPad.java` temporarily change `@Override public void mouseClicked` to `@Override public void mouseclicked` → compile → copy the error (screenshot `Task04/screenshots/t4_override_error.png`).
2. Then remove the `@Override` as well (keep the misspelling) → compile succeeds, run → clicks do nothing (screenshot `t4_no_override_no_effect.png`).
3. Restore the correct code.

## Complete the Code – status
| TODO | Completed code |
|---|---|
| TODO 1 | `@Override public void mouseClicked(MouseEvent e) { points.add(e.getPoint()); status.setText("Clicks: " + points.size()); repaint(); }` |
| TODO 2 | `super.paint(g); for (Point p : points) g.fillOval(p.x - 3, p.y - 3, 6, 6);` |

Screenshots required: before minimising (dots + label) and after minimising/restoring (same dots + same label).

## Alternative Concept and Complexity Analysis
**Alternative:** draw each dot once onto an off-screen `BufferedImage` and copy the image in `paint()`:
```java
private BufferedImage canvas = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);

@Override public void mouseClicked(MouseEvent e) {
    Graphics2D g2 = canvas.createGraphics();
    g2.fillOval(e.getX() - 3, e.getY() - 3, 6, 6);     // O(1) drawing, done once
    g2.dispose();
    count++; status.setText("Clicks: " + count); repaint();
}
@Override public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(canvas, 0, 0, null);                    // one copy, independent of n
}
```
(The image must be re-created/enlarged if the window is resized; clicks should also be corrected for the window's title-bar/border insets.)

| Aspect | Stored points + redraw (repaired design) | Off-screen `BufferedImage` |
|---|---|---|
| Time per click | O(1) (`add`) | O(1) (draw one dot) |
| **Time per repaint** | **O(n)** – draws n circles every time | **O(W × H)** pixel copy, i.e. constant for a fixed window and **independent of n** |
| Which is faster | Faster for small n (hundreds of dots) | Faster when n is very large (tens/millions of clicks) |
| **Space** | **O(n)** – one `Point` (≈ 24–32 bytes) per click | **O(W × H)** – 4 bytes per pixel (e.g. 400×300×4 ≈ 480 KB), constant but fixed; independent of n |
| Resize | Works at any size | Image must be enlarged and old content copied |
| Undo / delete a dot / save as list | Easy (the data exists) | Hard (only pixels exist; points are lost) |
| Counting clicks | `points.size()` | Needs a separate counter |

**Break-even:** repaint cost of the points design grows with n, the image copy stays constant, so the image wins when n is large enough that n × (cost of drawing one dot) exceeds the cost of copying one image; the points design wins on memory when n × ~32 B < W × H × 4 B, i.e. n < about 15 000 for a 400×300 window. For a classroom drawing pad both are tiny, so the repaired points design is preferred because it keeps the data (model) separate from the view.

## Screenshot evidence required
`t4_before_minimise.png`, `t4_after_restore.png`, `t4_buggy_no_effect.png`, `t4_override_error.png` (see `../SCREENSHOTS_CHECKLIST.md`).

## Result
Root cause: misspelled method name (`mouseclicked`) → handler never invoked. Second defect: drawing with `getGraphics()` is not persistent. Fixed with a correctly overridden `mouseClicked`, a stored `List<Point>` and drawing in `paint()`.
