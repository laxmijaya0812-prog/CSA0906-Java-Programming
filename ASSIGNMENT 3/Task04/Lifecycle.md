# Thread Life Cycle

```text
NEW -> RUNNABLE -> RUNNING/WAITING/TIMED_WAITING -> RUNNABLE -> TERMINATED
```

`start()` creates/schedules a new thread. Calling `run()` directly only executes the method on the current thread. `join()` makes the calling thread wait for another thread to finish.
