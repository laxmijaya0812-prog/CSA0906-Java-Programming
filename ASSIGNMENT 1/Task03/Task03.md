# Task 3 – Design the Control Logic

## Objective

To design and implement the control logic for allocating and releasing parking slots.

## Allocation Logic

The program searches each floor and each slot.

If a slot is null, it is free.

The vehicle number is stored in the slot and the floor and slot numbers are returned.

## Pseudocode

```text
FOR each floor
    FOR each slot
        IF slot is free
            Store vehicle
            Return floor and slot
        END IF
    END FOR
END FOR

Return null
