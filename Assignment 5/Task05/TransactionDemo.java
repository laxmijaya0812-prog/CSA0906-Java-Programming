import java.sql.*;

public class TransactionDemo {
    public static void transfer(Connection con, int sourceId,
                                int destinationId, double amount)
            throws SQLException {

        boolean oldAutoCommit = con.getAutoCommit();

        try {
            con.setAutoCommit(false);

            try (PreparedStatement debit = con.prepareStatement(
                    "UPDATE wallet SET balance = balance - ? WHERE id = ?");
                 PreparedStatement credit = con.prepareStatement(
                    "UPDATE wallet SET balance = balance + ? WHERE id = ?")) {

                debit.setDouble(1, amount);
                debit.setInt(2, sourceId);
                debit.executeUpdate();

                credit.setDouble(1, amount);
                credit.setInt(2, destinationId);
                credit.executeUpdate();
            }

            con.commit();
            System.out.println("Transfer committed.");

        } catch (SQLException e) {
            con.rollback();
            System.out.println("Transfer rolled back.");
            throw e;

        } finally {
            con.setAutoCommit(oldAutoCommit);
        }
    }
}
