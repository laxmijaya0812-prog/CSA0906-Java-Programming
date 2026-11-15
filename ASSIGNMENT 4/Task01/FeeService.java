import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Business layer for the fee-payment window (Task 1.2).
 * Contains NO Swing/AWT code: it validates input and performs the payment.
 *
 * Layers:  FeeWindow (GUI)  ->  FeeService (logic)  ->  FeeRepository (database)
 */
public class FeeService {

    /** Database layer abstraction - makes FeeService testable without MySQL. */
    public interface FeeRepository {
        void save(String regNo, double amount) throws SQLException;
    }

    /** Problem the user can fix (bad input). */
    public static class ValidationException extends Exception {
        public ValidationException(String message) { super(message); }
    }

    /** Immutable receipt returned after a successful payment. */
    public record Receipt(String regNo, double amount, String time) {
        @Override public String toString() {
            return "Register No : " + regNo + "\nAmount Paid : Rs. "
                    + String.format("%.2f", amount) + "\nTime        : " + time;
        }
    }

    /** Stores payments in MySQL using a PreparedStatement (no SQL injection). */
    public static class JdbcFeeRepository implements FeeRepository {
        // Configuration comes from environment variables - nothing hard-coded.
        private final String url  = System.getenv().getOrDefault("FEE_DB_URL",
                                    "jdbc:mysql://localhost:3306/fee_db");
        private final String user = System.getenv().getOrDefault("FEE_DB_USER", "root");
        private final String pwd  = System.getenv("FEE_DB_PASSWORD");

        @Override public void save(String regNo, double amount) throws SQLException {
            if (pwd == null) throw new SQLException("Environment variable FEE_DB_PASSWORD is not set");
            String sql = "INSERT INTO fees (reg_no, amount) VALUES (?, ?)";
            // try-with-resources closes the connection and statement automatically
            try (Connection c = DriverManager.getConnection(url, user, pwd);
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, regNo);
                ps.setDouble(2, amount);
                ps.executeUpdate();
            }
        }
    }

    /** Demo repository used when no database is configured. */
    public static class InMemoryFeeRepository implements FeeRepository {
        @Override public void save(String regNo, double amount) {
            System.out.println("[demo repository] saved " + regNo + " : " + amount);
        }
    }

    private final FeeRepository repository;
    private Receipt lastReceipt;

    public FeeService(FeeRepository repository) { this.repository = repository; }

    /** Validates the raw text typed by the user and returns the amount. */
    public double validate(String regNo, String amountText) throws ValidationException {
        if (regNo == null || regNo.isBlank())
            throw new ValidationException("Register number must not be empty.");
        if (!regNo.trim().matches("[A-Za-z0-9]{6,15}"))
            throw new ValidationException("Register number must be 6-15 letters/digits.");
        if (amountText == null || amountText.isBlank())
            throw new ValidationException("Amount must not be empty.");
        double amount;
        try {
            amount = Double.parseDouble(amountText.trim());
        } catch (NumberFormatException ex) {
            throw new ValidationException("Amount must be a number, e.g. 25000.50");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0)
            throw new ValidationException("Amount must be greater than zero.");
        return amount;
    }

    /**
     * Validates, waits for (simulated) bank confirmation and saves the payment.
     * It is slow, so the GUI must call it from a background thread (SwingWorker).
     */
    public synchronized Receipt pay(String regNo, String amountText)
            throws ValidationException, SQLException, InterruptedException {
        double amount = validate(regNo, amountText);
        Thread.sleep(2000);                       // simulated bank confirmation (background thread)
        repository.save(regNo.trim(), amount);
        lastReceipt = new Receipt(regNo.trim(), amount,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        return lastReceipt;
    }

    /** Last successful receipt, or null if nothing has been paid yet. */
    public synchronized Receipt getLastReceipt() { return lastReceipt; }
}
