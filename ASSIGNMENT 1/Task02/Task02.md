# Task 2 – Choose the Data Representation

## Objective

To select appropriate Java data types and data structures for representing the parking system.

## Data Type Selection

| Data | Data Type |
|---|---|
| Floor number | int |
| Slot number | int |
| Registration number | String |
| Ticket number | int |
| Entry time | LocalDateTime |
| Duration | int |
| Parking fee | double |
| Charging units | int |

## Floor and Slot Representation

A jagged two-dimensional array is used:

```java
String[][] slots;
