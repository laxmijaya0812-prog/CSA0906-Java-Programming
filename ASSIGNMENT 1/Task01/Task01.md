# Task 1 – Understand the Problem and Identify the Objects

## Objective

To analyse the Campus Smart-Parking System and identify the objects, classes, responsibilities, inputs, outputs and business rules required for the Java implementation.

## Problem Analysis

The university has a multi-level parking facility used by students, staff and visitors. Each floor contains a fixed number of parking slots. The system supports two-wheelers, cars and electric vehicles.

When a vehicle enters, the system allocates a suitable free slot, generates a unique parking ticket and records the entry time.

When a vehicle leaves, the system releases the slot and calculates the parking fee.

## Inputs

- Vehicle registration number
- Vehicle type
- Entry time
- Exit time
- Floor number
- Slot number
- Charging units
- User type
- Monthly pass information

## Outputs

- Parking floor
- Parking slot
- Ticket number
- Parking duration
- Parking fee
- Charging fee
- Slot availability
- Error messages

## Candidate Classes

| Class | Responsibility |
|---|---|
| Vehicle | Stores common vehicle information |
| TwoWheeler | Represents two-wheelers |
| Car | Represents cars |
| ElectricCar | Represents electric vehicles |
| ParkingFacility | Manages floors and slots |
| ParkingTicket | Stores ticket details |
| User | Represents parking users |
| Student | Represents students |
| Staff | Represents staff |
| Visitor | Represents visitors |
| Chargeable | Represents charging capability |

## Business Rules

1. One slot can contain only one vehicle.
2. Ticket numbers must be unique.
3. Invalid floor numbers must be rejected.
4. Invalid slot numbers must be rejected.
5. A released slot cannot be released again.
6. Parking duration cannot be negative.
7. Every started hour is charged as a full hour.
8. Valid student and staff monthly-pass holders park free.
9. Visitors pay the applicable fee.

## Assumptions

- Registration numbers are unique.
- Floor numbering starts from 0 internally.
- Slot numbering starts from 0 internally.
- Each floor may contain a different number of slots.
- Parking duration is calculated in minutes.

## Alternative Concept

If vehicles were stored using parallel arrays:

```java
String[] regNo;
String[] type;
long[] entryTime;
