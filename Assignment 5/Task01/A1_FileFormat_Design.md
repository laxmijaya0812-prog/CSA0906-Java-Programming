# Task A1 – Design a File Format

## Text Record
```text
passId,regNo,name,routeNo,boardingStop,validityDate,amountPaid
```

A name containing a comma should be quoted and handled using a CSV-aware parser.

## Binary Record
Use DataOutputStream in this order:
1. writeUTF(passId)
2. writeUTF(regNo)
3. writeUTF(name)
4. writeInt(routeNo)
5. writeUTF(boardingStop)
6. writeUTF(validityDate)
7. writeDouble(amountPaid)

## Operations
- Add: BufferedWriter
- Find: BufferedReader
- List by route: BufferedReader

## Damaged Data
Validate field count, report/skip malformed lines, handle a missing file with an appropriate message, and use a temporary-file replacement strategy for safer writes.

## Alternative
RandomAccessFile with fixed-length records can seek to a known record position using `recordIndex × recordSize`. Arbitrary ID lookup still needs an index to achieve O(1) lookup.
