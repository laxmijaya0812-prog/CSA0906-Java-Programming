import java.io.*;
import java.nio.file.*;
import java.sql.*;

public class Migration {
    public static void migrate(Path file, Connection con) throws Exception {
        String sql =
            "INSERT INTO bus_pass(pass_id, reg_no, name, route_no, amount) " +
            "VALUES (?, ?, ?, ?, ?)";

        con.setAutoCommit(false);
        int success = 0;
        int failed = 0;

        try (BufferedReader br = Files.newBufferedReader(file);
             PreparedStatement ps = con.prepareStatement(sql)) {

            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    failed++;
                    System.out.println("Failed record: empty line");
                    continue;
                }

                try {
                    String[] p = line.split(",", -1);

                    if (p.length != 5) {
                        throw new IllegalArgumentException(
                            "Invalid number of fields");
                    }

                    ps.setString(1, p[0]);
                    ps.setString(2, p[1]);
                    ps.setString(3, p[2]);
                    ps.setInt(4, Integer.parseInt(p[3]));
                    ps.setDouble(5, Double.parseDouble(p[4]));

                    ps.addBatch();
                    success++;

                    if (success % 1000 == 0) {
                        ps.executeBatch();
                    }

                } catch (Exception e) {
                    failed++;
                    System.out.println("Failed record: " + line);
                    System.out.println("Reason: " + e.getMessage());
                }
            }

            ps.executeBatch();
            con.commit();

            System.out.println("Successful records: " + success);
            System.out.println("Failed records: " + failed);

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java Migration <csv-file>");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            migrate(Path.of(args[0]), con);
        }
    }
}
