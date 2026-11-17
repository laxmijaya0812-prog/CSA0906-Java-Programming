# Task B4 – Final Engineering Decision

The migration reads the existing CSV file, validates records, loads valid records using PreparedStatement batches, and commits the transaction.

DatabaseMetaData reports database and JDBC driver information.

For the expanded 12-campus system with concurrent counters, a relational database is the recommended production persistence layer because it supports concurrent access, transactions, indexing, integrity constraints and scalable querying.

Run:
```bash
javac -cp mysql-connector-j.jar DBConnection.java Migration.java DatabaseInfo.java
java -cp ".;mysql-connector-j.jar" Migration migration_input.csv
java -cp ".;mysql-connector-j.jar" DatabaseInfo
```
