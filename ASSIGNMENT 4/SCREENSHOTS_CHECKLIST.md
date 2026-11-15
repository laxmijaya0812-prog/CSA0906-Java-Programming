# Screenshot Checklist (capture these yourself – none are provided or faked)

**How:** run the program, click the window title bar, press **Alt + Print Screen** (captures only the active window) or **Win + Shift + S → Window snip**. Save as PNG with exactly the filename shown. Under each screenshot in the report write one sentence saying which requirement/test it proves.

## Task 1 – `Task01/screenshots/`
| File | Visible | Proves |
|---|---|---|
| `t1_window.png` | `FeeWindow` at start | Task 1.2 – Receipt button present, status message |
| `t1_processing.png` | "Processing payment… please wait." with Pay disabled (take it within 2 s after Pay) | EDT not blocked / feedback |
| `t1_success.png` | "Payment successful!" dialog | Valid payment |
| `t1_receipt.png` | Receipt dialog | Receipt handler |
| `t1_invalid_amount.png` | Error dialog for amount `abc` (and/or empty reg no) | Input validation |
| `t1_console_or_db.png` | Console line `[demo repository] saved …` or MySQL Workbench `SELECT * FROM fees;` | Payment stored via service/repository |
| `t1_compile.png` | PowerShell: `javac *.java` without errors | Program compiles |

## Task 2 – `Task02/screenshots/` (design task; optional)
| File | Visible | Proves |
|---|---|---|
| `t2_hierarchy.png` | Rendered tree/table from `ComponentHierarchy.md` (VS Code preview) | 2.2 |

## Task 3 – `Task03/screenshots/`
| File | Visible | Proves |
|---|---|---|
| `library_normal.png` | 600×400 window after searching so results are listed | Initial layout |
| `library_enlarged.png` | Window enlarged / maximised, same results | Results area grows; rows/buttons keep size |
| `library_reduced.png` | Window dragged to its smallest | Results shrink, rows keep size |
| `library_search.png` | Search `java` / category filter with status text | Functional search + status message |

## Task 4 – `Task04/screenshots/`
| File | Visible | Proves |
|---|---|---|
| `t4_buggy_no_effect.png` | `DrawPadBuggy` after several clicks: no dots (label stays "Clicks: 0") | 4.1 defect |
| `t4_override_error.png` | PowerShell compile error "method does not override or implement a method from a supertype" after adding `@Override` to the misspelled method | 4.4 |
| `t4_before_minimise.png` | Repaired `DrawPad` with several dots and label "Clicks: n" | 4.3 / Complete the Code |
| `t4_after_restore.png` | Same window after minimise + restore, same dots and label | Dots survive repaint |
| `t4_failing_interaction.png` *(optional)* | Version with only the method name fixed (drawing through `getGraphics()`) after restore: dots gone | 4.2 |

## Task 5 – `Task05/screenshots/`
| File | Visible | Proves |
|---|---|---|
| `t5_frame_valid.png`, `t5_named_valid.png`, `t5_anonymous_valid.png`, `t5_lambda_valid.png` | Each version showing e.g. `12.00 USD` | 5.1 – four implementations work |
| `t5_lambda_invalid.png` | Lambda version with `abc` → "Invalid amount" | Complete the Code (invalid amount) |
| `t5_dialog.png` | Result dialog box | 5.2 dialog |
| `t5_enter_key.png` | Conversion triggered by Enter in the field (mention in caption) | 5.2 Enter |
| `t5_git_diff.png` | `git diff --stat` for the Enter/dialog change | 5.2 change effort |

## Task 6 – `Task06/screenshots/`
| File | Visible | Proves |
|---|---|---|
| `t6_schema.png` | MySQL Workbench: tables and `SHOW CREATE TABLE registrations;` showing UNIQUE key | SQL script / duplicate rule |
| `t6_student_form.png` | Student Registration tab with menu bar | Screen, JMenuBar |
| `t6_valid.png` | Green "Registration successful…" | T1 / R1 |
| `t6_invalid_empty.png`, `t6_invalid_email.png`, `t6_invalid_event.png` | Red validation errors | T2 / R2 |
| `t6_duplicate.png` | "Student already registered for this event." | T3 / R3 |
| `t6_table.png` | Committee View with rows | T4 / R4 |
| `t6_filter.png` | Filter = one event, only matching rows | T5 / R5 |
| `t6_paging.png` | "Page 2 of N" (after T8 data) | Paging |
| `t6_export_dialog.png` | File chooser + success dialog | T6 / R6 |
| `t6_export_csv.png` | Exported CSV opened in Notepad/Excel | T6 / R6 |
| `t6_responsive.png` *(optional)* | Error message shown while MySQL is stopped, window still movable | R7 |
| `t6_class_diagram.png` | Rendered Mermaid class diagram | Stage 4 |
| `t6_github_commits.png` | GitHub commit history | Progressive commits |
