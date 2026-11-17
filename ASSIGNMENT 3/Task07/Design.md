# Concurrent Fault-Tolerant Processor

Each file is an independent task. `ExecutorService` limits the number of worker threads. Each `Callable<FileResult>` catches file-specific failures and returns a failure result rather than terminating the complete run. `Future.get()` collects each result.

## Expected small test
There are 6 sample files:
- 4 valid files
- 1 corrupt file
- 1 out-of-range file

Expected summary:
- Successful: 4
- Failed: 2

Actual timing for 1, 2, 4 and 8 threads must be measured on the student's machine and recorded in the report.
