import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates JDBC connections. CONFIGURATION SECTION - read from environment variables so that
 * no password is stored in the source code:
 *   DB_URL      (default jdbc:mysql://localhost:3306/campus_events)
 *   DB_USER     (default root)
 *   DB_PASSWORD (required)
 */
public final class DatabaseConnection {
    private static final String URL  = System.getenv().getOrDefault("DB_URL",
            "jdbc:mysql://localhost:3306/campus_events");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private DatabaseConnection() { }

    /** The caller must close the connection (use try-with-resources). */
    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null) {
            throw new SQLException("Database password not set. Set the DB_PASSWORD environment variable.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
