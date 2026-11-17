import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BatchPerformanceTest {
    static List<String[]> generate(int n) {
        List<String[]> list = new ArrayList<>(n);
        for (int i = 1; i <= n; i++) {
            list.add(new String[]{
                "P" + i,
                "19231" + i,
                "Student" + i,
                String.valueOf((i % 10) + 1),
                String.valueOf(3000 + (i % 2000))
            });
        }
        return list;
    }

    public static void main(String[] args) throws Exception {
        int n = args.length == 0 ? 1000 : Integer.parseInt(args[0]);

        try (Connection con = DBConnection.getConnection()) {
            con.createStatement().executeUpdate("DELETE FROM bus_pass");

            long start = System.nanoTime();
            int loaded = PassLoader.loadAll(con, generate(n));
            long end = System.nanoTime();

            System.out.printf(
                "Loaded: %d rows%nTime: %.3f ms%n",
                loaded, (end - start) / 1_000_000.0);
        }
    }
}
