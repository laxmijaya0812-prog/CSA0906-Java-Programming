import java.io.*;

public class BufferedLineCounter {
    public static long countLines(String filename) throws IOException {
        long count = 0;

        try (BufferedInputStream in =
                     new BufferedInputStream(new FileInputStream(filename))) {
            int data;
            while ((data = in.read()) != -1) {
                if (data == '\n') {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java BufferedLineCounter <file>");
            return;
        }
        System.out.println("Lines: " + countLines(args[0]));
    }
}
