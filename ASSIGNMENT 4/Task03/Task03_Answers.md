# Task 3 – From Sketch to Layout (K4 – Analyse): Campus Library Search Screen
**Student:** Lakshmi Devi J | **Register No:** 192524300 | **Source:** `LibrarySearch.java` (Swing)

## Objective
Build a screen with a title at the top, a search row (text field, category selector, Search button), a results area in the centre, a Prev/Next/Reserve button row at the bottom and a status message. On resize, **only the results area grows**; the search row and buttons keep their size.

## Task 3.1 – Choose the Layout Managers (3 marks)

| Container | Layout manager | Justification (resizing requirement) |
|---|---|---|
| Frame content pane | **BorderLayout** | NORTH and SOUTH regions keep their preferred height; the CENTER region receives *all* remaining space. This exactly matches "results grow, the rest keeps its size". |
| `topPanel` (NORTH) | **GridLayout(2,1)** | Stacks title and search row in two equal rows; the panel's height stays at its preferred size because it is in NORTH. |
| `searchRow` | **FlowLayout (CENTER)** | Places label, field, combo box and button in one row at their *preferred* sizes; components are never stretched, so the row keeps its size. |
| Results area | `JScrollPane` placed in **CENTER** | Grows and shrinks with the window; scroll bars appear when the list does not fit. |
| `bottomPanel` (SOUTH) | **BorderLayout** | Button row in CENTER, status label in SOUTH – the status text always spans the full width under the buttons. |
| `buttonRow` | **FlowLayout (CENTER)** | Buttons keep preferred size and stay centred. |
| CardLayout | *Not used* | The screen has only one view, so there is nothing to switch between. (It would be useful if a separate "Reservation details" screen were added.) |

## Task 3.2 – Nested Container Diagram (2 marks)

```
JFrame  ─ BorderLayout(5,5)
├── NORTH  : JPanel topPanel ─ GridLayout(2,1)
│            ├── JLabel  titleLabel  "Campus Library"
│            └── JPanel  searchRow ─ FlowLayout(CENTER)
│                         ├── JLabel "Title:"
│                         ├── JTextField searchField
│                         ├── JComboBox  categoryBox
│                         └── JButton    searchButton
├── CENTER : JScrollPane ──► JList resultList            ◄── GROWS
└── SOUTH  : JPanel bottomPanel ─ BorderLayout
             ├── CENTER: JPanel buttonRow ─ FlowLayout(CENTER)
             │            ├── JButton Prev
             │            ├── JButton Next
             │            └── JButton Reserve
             └── SOUTH : JLabel statusLabel
```
Containers: 1 frame + 4 panels (`topPanel`, `searchRow`, `bottomPanel`, `buttonRow`) + 1 scroll pane = **6 containers**.

## Task 3.3 – Implementation (3 marks)
`LibrarySearch.java` (Swing). Besides the layout it provides: a 12-book sample catalogue, search by title text and category (Enter key also searches), paging with Prev/Next (4 results per page), Reserve for the selected book, and status messages.

**Compile & run (PowerShell, inside `Task03`):**
```powershell
javac LibrarySearch.java
java LibrarySearch
```

## Task 3.4 – Test the Resizing Behaviour (1 mark)

| Test | Window size | Expected behaviour of each region | Screenshot file |
|---|---|---|---|
| R1 Normal | 600 × 400 (initial) | Title centred on top; search row on one line; results list fills the centre; buttons and status at the bottom | `Task03/screenshots/library_normal.png` |
| R2 Enlarged | maximise / drag to about 1000 × 700 | **Results area becomes larger** (more rows visible); search row, buttons and status keep the same size and stay centred; no stretched buttons | `Task03/screenshots/library_enlarged.png` |
| R3 Reduced | drag to the minimum, 450 × 300 | Results area shrinks and shows a scroll bar if needed; search row and buttons keep size (the minimum size stops the row from being cut off) | `Task03/screenshots/library_reduced.png` |

For each screenshot, search for `java` (or leave empty and press Search) first so that the results list contains items. **Write one sentence under each screenshot in your report** stating whether each region behaved as designed (this sentence is what the PDF asks for; the screenshot alone is not enough). Expected: all three behaved as designed. If you see something different on your PC, report what you actually observed.

Extra functional test: Search `java`, category All → 1 result; Search empty, category All → 12 results over 3 pages (Next/Prev enabled correctly); Reserve with nothing selected → "Select a book first…".

## Alternative Concept and Complexity Analysis

**Alternative 1 – GridBagLayout** (one container, constraints per component):
```java
setLayout(new GridBagLayout());
GridBagConstraints c = new GridBagConstraints();
c.gridx = 0; c.gridy = 0; c.gridwidth = 4; add(titleLabel, c);                    // weight 0
c.gridy = 1; c.gridwidth = 1; add(searchField, c); /* category, search ... */
c.gridy = 2; c.gridwidth = 4; c.weightx = 1; c.weighty = 1; c.fill = BOTH; add(scroll, c); // grows
c.gridy = 3; c.weightx = 0; c.weighty = 0; c.fill = NONE; add(buttons, c);
```
**Alternative 2 – absolute positioning:** `setLayout(null); results.setBounds(10, 80, 560, 240); search.setBounds(...)`.

| Aspect | Nested BorderLayout + FlowLayout | GridBagLayout | Absolute (`null` layout) |
|---|---|---|---|
| Resizing behaviour | Results grow automatically; other rows keep size | Same, but controlled by `weightx/weighty/fill` – very flexible, easy to get wrong | **No automatic resizing**: components stay at fixed pixel positions; a larger window shows empty space, a smaller one hides components |
| Different fonts / screen DPI | Adapts to preferred sizes | Adapts | Text may be cut off |
| Number of containers | 1 frame + 4 panels (+ scroll pane) | Could be 1 frame + 1–2 panels | 1 frame, no extra panels |
| Space (objects) | More panel objects (each a few hundred bytes) – O(c) for c containers | Fewer panels but one `GridBagConstraints` per component – O(n) for n components | Fewest objects: O(n) components only |
| Layout computation time | Each manager does a single pass over its children: O(n_i) per container, O(n) overall (recursive over the tree depth, ≈ 3 levels) | O(n) per pass, but with a larger constant (computes grid rows/columns, weights; may use extra passes) | **O(1)** per component – nothing computed (`setBounds` only) |
| Maintainability | Simple, readable, each part independent | Verbose, harder to read | Fragile; every position edited by hand |

**Conclusion:** nested BorderLayout/FlowLayout is the simplest design that meets the requirement. GridBagLayout is best for complicated forms with aligned columns. Absolute positioning is fastest to compute but fails the resizing requirement, so it is unsuitable. Layout time is trivially small (n ≈ 10 components) for every option, so choose by **correctness and maintainability**, not speed.

## Evidence Expected
Justified layout choices (3.1), nested container diagram (3.2), working code (3.3) and resizing screenshots with explanations (3.4).

## Result
The screen meets the resizing requirement: only the results region grows; search row, buttons and status keep their size.
