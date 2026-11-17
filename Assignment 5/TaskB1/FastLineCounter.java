import java.io.*;

public class FastLineCounter {
    public static long countLines(String filename) throws IOException {
        long count = 0;

        try (FileInputStream in = new FileInputStream(filename)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    if (buffer[i] == '\n') {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java FastLineCounter <file>");
            return;
        }
        System.out.println("Lines: " + countLines(args[0]));
    }
}
