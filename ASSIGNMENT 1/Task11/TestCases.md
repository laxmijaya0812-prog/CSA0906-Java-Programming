# Test Cases

| Test | Input | Expected | Actual | Status |
|---|---|---|---|---|
| Two-wheeler | 60 minutes | Rs 10 | Rs 10 | PASS |
| Car | 60 minutes | Rs 30 | Rs 30 | PASS |
| Car | 90 minutes | Rs 60 | Rs 60 | PASS |
| EV | 130 minutes + 10 kWh | Rs 210 | Rs 210 | PASS |
| Full facility | No slot | null | null | PASS |
| Valid release | 0,1 | true | true | PASS |
| Repeat release | 0,1 | false | false | PASS |
| Invalid floor | 5,0 | false | false | PASS |
| Zero minutes | 0 | Rs 0 | Rs 0 | PASS |
