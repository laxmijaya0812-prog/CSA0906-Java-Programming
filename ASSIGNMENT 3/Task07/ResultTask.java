import java.nio.file.*;

import java.util.concurrent.Callable;

public class ResultTask implements Callable<FileResult> {
    private final Path file;

    public ResultTask(Path file) {
        this.file = file;
    }

    @Override
    public FileResult call() {
        try {
            if (!Files.exists(file)) {
                return new FileResult(
                        file.getFileName().toString(),
                        false, 0, "File not found");
            }

            String content = Files.readString(file).trim();

            if (content.isEmpty()) {
                return new FileResult(
                        file.getFileName().toString(),
                        false, 0, "Empty file");
            }

            int marks = Integer.parseInt(content);

            if (marks < 0 || marks > 100) {
                return new FileResult(
                        file.getFileName().toString(),
                        false, 0, "Invalid marks");
            }

            return new FileResult(
                    file.getFileName().toString(),
                    true, marks, "Processed successfully");

        } catch (Exception e) {
            return new FileResult(
                    file.getFileName().toString(),
                    false, 0,
                    e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
