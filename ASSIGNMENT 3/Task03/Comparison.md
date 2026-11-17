# Manual Close vs Try-With-Resources

| Aspect | Manual finally | Try-with-resources |
|---|---|---|
| Closing | Explicit | Automatic |
| Exception safety | More error-prone | Safer |
| Code size | Larger | Smaller |
| Suppressed exceptions | Manual handling | Automatically retained |

For `f` files with total `N` records, processing is O(N). Try-with-resources does not change the asymptotic processing complexity; it improves resource safety.
