# Task 2 – From Requirement to Event-Driven Design (K4 – Analyse)
**Student:** Lakshmi Devi J | **Register No:** 192524300

Task 2 is a **design task – no code is required by the PDF.** Required evidence: an event table, a component hierarchy, handler pseudocode and a correct event trace.

| PDF item | File |
|---|---|
| 2.1 Model the events | `EventTable.md` |
| 2.2 Component hierarchy | `ComponentHierarchy.md` |
| 2.3 Handler pseudocode | `HandlerPseudocode.md` |
| 2.4 Event trace + applet comparison | `EventTrace.md` |
| Alternative concept & complexity | this file (below) |

## Alternative Concept and Complexity Analysis
**Approach A (chosen):** load all doctors once at start into `Map<String, List<Doctor>>` and look them up.
**Approach B (alternative):** query the database every time the department changes:
```
ALGORITHM  RefreshDoctorList_DB
 1. dept ← departmentBox.selectedItem
 2. START SwingWorker:
 3.     rows ← SELECT * FROM doctors WHERE department = ?      // PreparedStatement
 4.     RETURN list of Doctor
 5. ON COMPLETION: refill doctorBox with the list
```

Let **d** = number of departments, **m** = total number of doctors, **m_d** = doctors in the selected department, **N** = rows in the doctors table.

| Aspect | A – In-memory Map | B – Query on every change |
|---|---|---|
| Time per department change | `HashMap.get` = **O(1)** average, plus O(m_d) to fill the combo box | Network + parse + execute query: O(log N + m_d) with an index on `department`, O(N) without; plus connection/round-trip latency (milliseconds), which dominates |
| Start-up cost | One query/loading step: O(m) | None |
| Memory | **O(d + m)**: d keys + m Doctor objects (all stay in RAM) | **O(m_d)** at a time: only the current department's doctors |
| Freshness of data | Stale if doctors are added after loading (needs a Refresh) | Always current |
| Responsiveness | Immediate, works without a connection | Needs `SwingWorker` and handling of failures; UI waits for the result |
| Database load | 1 query | One query per selection |

**Conclusion:** for a small, rarely changing doctor list, the Map is far faster (O(1)) and simpler for the user; the extra memory O(d+m) is tiny. The database approach is preferable only if the list is very large or changes constantly. A hybrid (cache with a Refresh button/expiry) gives the benefits of both.
