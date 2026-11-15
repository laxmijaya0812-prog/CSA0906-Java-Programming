# CSA09-192524300-Assignment4 – Event-Driven GUI Design

| | |
|---|---|
| **Student** | Lakshmi Devi J |
| **Register Number** | 192524300 |
| **Course** | Programming in Java (CSA09) |
| **Institution** | SIMATS Engineering |
| **Assignment** | Assignment 4 – CO4: Event-Driven GUI Design (50 marks) |
| **Java version** | Java 17 or above (uses records and `String.isBlank`; compiled successfully with JDK 21) |
| **GitHub repository** | `https://github.com/<your-username>/CSA09-192524300-Assignment4` ← replace with your real link |

## Objectives
Interpret a requirement → identify components and events → design layouts → design handlers → implement → trace → challenge → refine, keeping the user interface separate from application logic (AWT/Swing, event delegation model, layout managers, menus and dialogs, Swing + JDBC).

## Technologies
Java 17+, Swing/AWT, JDBC, MySQL 8 (Task 6 only), MySQL Connector/J (JDBC driver, Task 6 only), Mermaid (diagrams), Git/GitHub.

## Project structure
```
CSA09-192524300-Assignment4/
├── README.md, REPORT.md, SCREENSHOTS_CHECKLIST.md, GIT_COMMANDS.md, FINAL_CHECKLIST.md
├── Task01/   FeeWindow.java, FeeService.java, fees.sql, Task01_Answers.md, screenshots/
├── Task02/   EventTable.md, ComponentHierarchy.md, HandlerPseudocode.md, EventTrace.md, Task02_Answers.md, screenshots/
├── Task03/   LibrarySearch.java, Task03_Answers.md, screenshots/
├── Task04/   DrawPad.java, DrawPadBuggy.java, Task04_Answers.md, screenshots/
├── Task05/   ConverterFrameListener.java, ConverterNamedInnerClass.java,
│             ConverterAnonymousInnerClass.java, ConverterLambda.java, Task05_Answers.md, screenshots/
└── Task06/   src/ (Main, RegistrationFrame, RegistrationService, RegistrationDAO,
│                   DatabaseConnection, model/Registration, model/EventInfo)
              database/schema.sql, lib/ (put the MySQL Connector/J jar here), Task06_Answers.md, screenshots/
```

## Task-wise description (and result)
| Task | What it contains | Result note |
|---|---|---|
| 1 – GUI design review (6) | 12 deficiencies diagnosed; `FeeWindow` (GUI) + `FeeService` (logic) + repository; SwingWorker, PreparedStatement, validation, Receipt, window closing; 83-word defence; listener-vs-Action analysis | *(write your observed result after running)* |
| 2 – Appointment desk design (8) | Event table, component hierarchy, pseudocode, event trace + applet comparison, Map vs database analysis | Design task – no program |
| 3 – Library search layout (9) | Layout choices, nested container diagram, `LibrarySearch.java`, 3 resizing tests, GridBag/absolute comparison | *(write observation)* |
| 4 – Event forensics (7) | Defect `mouseclicked`, failing interaction, repaired `DrawPad.java` (TODO 1 & 2), `@Override` compiler message, BufferedImage analysis | *(write observation)* |
| 5 – Event-handling styles (7) | Four converter versions (+ Enter key + dialog), lambda TODO 1 & 2, change effort table, comparison, Action analysis | *(write observation)* |
| 6 – Event registration app (13) | Requirements, screens, flow, class diagram, pseudocode, Swing + JDBC + MySQL application, SQL script, tests, pagination analysis | *(write observation)* |

## How to compile and run (Windows 11, PowerShell)
Check the tools once: `java -version` and `javac -version` must show 17 or higher.

```powershell
# Task 1  (uses an in-memory demo repository unless FEE_DB_PASSWORD is set)
cd Task01 ; javac *.java ; java FeeWindow ; cd ..

# Task 3
cd Task03 ; javac LibrarySearch.java ; java LibrarySearch ; cd ..

# Task 4  (repaired program; DrawPadBuggy shows the original defect)
cd Task04 ; javac *.java ; java DrawPad ; java DrawPadBuggy ; cd ..

# Task 5  (run each of the four)
cd Task05 ; javac Converter*.java
java ConverterFrameListener ; java ConverterNamedInnerClass
java ConverterAnonymousInnerClass ; java ConverterLambda ; cd ..
```
Tasks 1 with MySQL: run `Task01/fees.sql`, put the Connector/J jar on the classpath (`javac -cp "." …` / `java -cp ".;mysql-connector-j-8.x.x.jar" FeeWindow`) and set `$env:FEE_DB_PASSWORD="..."` (optional `FEE_DB_URL`, `FEE_DB_USER`).

## Task 6 – MySQL setup, configuration, compile and run
1. Install MySQL Server 8 and MySQL Workbench. Open `Task06/database/schema.sql` in Workbench and execute it (creates `campus_events` with sample data).
2. Download Connector/J (<https://dev.mysql.com/downloads/connector/j/>, "Platform Independent") and copy `mysql-connector-j-8.x.x.jar` into `Task06/lib/`.
3. Configure the connection with environment variables (no password is stored in code):
```powershell
cd Task06
$env:DB_PASSWORD = "your_mysql_password"                 # required
$env:DB_URL  = "jdbc:mysql://localhost:3306/campus_events"   # optional (this is the default)
$env:DB_USER = "root"                                    # optional (this is the default)
```
4. Compile and run:
```powershell
mkdir out -Force
javac -d out -cp "lib/*" src/model/*.java src/*.java
java -cp "out;lib/*" Main
```
VS Code: open the folder `CSA09-192524300-Assignment4`, install the *Extension Pack for Java*, and use the integrated PowerShell terminal for the commands above. (Environment variables set with `$env:` last only for that terminal window.)

## Test cases and expected results (summary)
| Task | Test | Expected result |
|---|---|---|
| 1 | Valid payment / empty reg no / text amount / Receipt / drag window while processing | success dialog / validation errors / receipt dialog / window stays responsive |
| 3 | normal, enlarged, reduced window | only the results area resizes |
| 4 | click ×5, minimise, restore | five dots and "Clicks: 5" remain |
| 5 | 1000 INR → USD / EUR, `abc`, Enter key | `12.00 USD` / `11.00 EUR`, "Invalid amount", Enter converts, dialog shows result |
| 6 | valid, invalid, duplicate, view, filter, export | "Registration successful", clear error messages, "Student already registered for this event.", rows in JTable, only the chosen event, "Registration data exported successfully" |

Full test tables are in each `TaskNN_Answers.md`. *Expected* results are derived from the code design; the real evidence is your screenshots in each `screenshots/` folder (see `SCREENSHOTS_CHECKLIST.md`).

## GitHub commit instructions
Commit progressively, at least once per task – see `GIT_COMMANDS.md`. Share the repository with the course faculty (public, or private with the faculty added as collaborator) and paste the link above and on the report cover page.

## Conclusion
The assignment demonstrates event-driven design with Swing/AWT: separation of GUI and logic, correct listener use, suitable layout managers, repair of event-handling defects, comparison of listener styles and a layered Swing–JDBC application whose long-running work stays off the Event Dispatch Thread.
