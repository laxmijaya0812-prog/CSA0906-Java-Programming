import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ResultProcessingSystem {

    public static void main(String[] args)
            throws Exception {

        int[] threadCounts = {1, 2, 4, 8};

        Path directory = Paths.get("scripts");

        if (!Files.exists(directory)) {
            System.out.println("Create the scripts folder first.");
            return;
        }

        for (int threads : threadCounts) {
            long start = System.currentTimeMillis();

            ExecutorService executor =
                    Executors.newFixedThreadPool(threads);

            List<Future<FileResult>> futures =
                    new ArrayList<>();

            try (DirectoryStream<Path> files =
                         Files.newDirectoryStream(directory)) {

                for (Path file : files) {
                    if (Files.isRegularFile(file)) {
                        futures.add(
                                executor.submit(
                                        new ResultTask(file)));
                    }
                }
            }

            int success = 0;
            int failed = 0;

            for (Future<FileResult> future : futures) {
                FileResult result = future.get();

                if (result.success) {
                    success++;
                } else {
                    failed++;
                    System.out.println(
                            "FAILED: " + result.fileName +
                            " -> " + result.message);
                }
            }

            executor.shutdown();

            long end = System.currentTimeMillis();

            System.out.println(
                    "Threads: " + threads);
            System.out.println(
                    "Successful: " + success);
            System.out.println(
                    "Failed: " + failed);
            System.out.println(
                    "Time: " + (end - start) + " ms");
            System.out.println("--------------------");
        }
    }
}
