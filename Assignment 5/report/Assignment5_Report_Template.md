# Assignment 5 – Data Persistence Engineering

## Student Details
Name: Lakshmi Devi J.
Register Number: 192524300
Course: Programming in Java
CO: CO5

## Part A
### A1 – File Format Design
[Use Task01 content.]

### A2 – Buffered I/O Design
[Use Task02 analysis and actual measurements.]

### A3 – DAO Pattern
[Use Task03 content.]

### A4 – Serialization
[Use Task04 content.]

### A5 – Transactions and Injection
[Use Task05 content.]

## Part B
### B1 – File Store
Attach actual execution screenshots and measured timing tables.

### B2 – Adversarial Inputs
Attach screenshots showing unsafe/safe login behaviour and malformed file handling.

### B3 – JDBC Comparison
Attach actual timing tables for 1,000, 10,000 and 100,000 rows, plus transaction failure evidence.

### B4 – Final Decision
Use the database recommendation and include actual DatabaseMetaData output.

## Conclusion
The experiments demonstrate the trade-off between simple file persistence, object serialization and database persistence. For the expanded transport-office scenario, MySQL with JDBC, PreparedStatement, batching and transactions is the recommended solution.
