# Race Condition

Two threads can both execute:

1. Check that the seat is free.
2. Both observe `false`.
3. Both mark the seat as booked.
4. Both increment the booking count.

The check-then-book operation is the critical section. Synchronizing the operation makes it atomic with respect to other threads using the same object.
