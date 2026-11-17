# Exception Hierarchy

```text
Exception
   |
ExamException
   |-- ExamClosedException
   |-- NotRegisteredException
   |-- SubmissionSizeException
   `-- SubmissionStorageException
```

`ExamException` is checked because the portal must explicitly handle expected business failures. The storage exception keeps the original IOException as its cause.
