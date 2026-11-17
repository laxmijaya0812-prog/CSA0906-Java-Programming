import java.sql.*;

public class DatabaseInfo {
    public static void showMetadata(Connection con) throws SQLException {
        DatabaseMetaData meta = con.getMetaData();

        System.out.println("Database Product: " +
                meta.getDatabaseProductName());
        System.out.println("Database Version: " +
                meta.getDatabaseProductVersion());
        System.out.println("Driver Name: " +
                meta.getDriverName());
        System.out.println("Driver Version: " +
                meta.getDriverVersion());

        System.out.println("Tables:");

        try (ResultSet rs = meta.getTables(
                null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try (Connection con = DBConnection.getConnection()) {
            showMetadata(con);
        }
    }
}
