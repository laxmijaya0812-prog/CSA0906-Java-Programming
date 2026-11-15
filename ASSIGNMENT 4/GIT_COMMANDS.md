# Git / GitHub Commands (PowerShell)

## One-time setup
```powershell
git config --global user.name "Lakshmi Devi J"
git config --global user.email "your-email@example.com"
cd path\to\CSA09-192524300-Assignment4
git init
git branch -M main
```
Create an empty repository named **CSA09-192524300-Assignment4** on github.com (no README), then:
```powershell
git remote add origin https://github.com/<your-username>/CSA09-192524300-Assignment4.git
```
Share it with the faculty (Settings → Collaborators) if it is private.

## Progressive commits (do each one after you have *run* the task and saved screenshots)
```powershell
git add Task01
git commit -m "Complete Task 1 GUI design review: diagnosis, FeeWindow/FeeService redesign, analysis"

git add Task02
git commit -m "Complete Task 2 hospital appointment desk design: events, hierarchy, pseudocode, trace"

git add Task03
git commit -m "Complete Task 3 library search screen with layout analysis and resize tests"

git add Task04
git commit -m "Complete Task 4 DrawPad forensics: repaired program, TODOs and @Override evidence"

git add Task05
git commit -m "Complete Task 5 four event-handling styles of currency converter and comparison"

git add Task06
git commit -m "Complete Task 6 campus event registration app: Swing, JDBC, MySQL schema"

git add README.md REPORT.md SCREENSHOTS_CHECKLIST.md FINAL_CHECKLIST.md GIT_COMMANDS.md .gitignore
git commit -m "Add README, report and submission checklists"

git push -u origin main
```
Tip: Task 5.2 evidence – commit the converters first with `SHOW_DIALOG = false` and the `amount.addActionListener(...)` lines commented out ("Task 5.1 four styles"), then enable them and commit again ("Task 5.2 Enter key and dialog") and run `git diff HEAD~1 --stat`.
Tip: if a task needs fixes, make a new commit (`git commit -am "Fix …"`) – the history must show progressive work, so do **not** upload everything on the last day.

Check: `git log --oneline` should show at least 7 commits.
