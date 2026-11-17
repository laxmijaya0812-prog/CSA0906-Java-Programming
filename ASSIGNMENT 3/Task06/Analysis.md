# Deadlock and Producer-Consumer Analysis

## Deadlock prevention
Acquire multiple wallet locks in one consistent global order. This removes circular wait.

## Producer-consumer
`wait()` must be called while holding the object's monitor and inside a `while` condition. `notifyAll()` wakes waiting producers/consumers so they can re-check the condition.

## sleep vs wait
- `sleep()` pauses a thread for a time and does not release an intrinsic lock.
- `wait()` releases the monitor and waits for notification.
