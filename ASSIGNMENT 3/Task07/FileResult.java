public class FileResult {
    final String fileName;
    final boolean success;
    final int marks;
    final String message;

    public FileResult(String fileName, boolean success,
                      int marks, String message) {
        this.fileName = fileName;
        this.success = success;
        this.marks = marks;
        this.message = message;
    }
}
