# Task 11 – Implement, Test and Defend the Design

## Normal Operation

The system was tested with:

- Two-wheelers
- Cars
- Electric cars

## Boundary Cases

The following cases were tested:

- Zero-minute parking
- Exactly one hour
- 90-minute parking
- Full parking facility
- Last available slot

## Invalid Cases

The system was tested with:

- Invalid floor
- Invalid slot
- Releasing an already-free slot
- Duplicate registration number
- Unknown vehicle type

## Daily Collection Example

Two-wheeler = Rs 10

Car = Rs 60

EV parking = Rs 60

EV charging = Rs 120

Total:

Rs 10 + Rs 60 + Rs 60 + Rs 120 = Rs 250

## OOP Evaluation

The implementation demonstrates:

- Encapsulation
- Inheritance
- Polymorphism
- Abstraction
- Interfaces
- Constructors
- Static members
- Packages

## Limitation

Adding many pricing rules directly inside vehicle classes can make the system difficult to maintain. A separate PricingRule abstraction can improve extensibility.

## Result

The system was implemented and tested successfully against normal, boundary and invalid cases.
