import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * Task 1.2 - GUI layer only: collects input and shows results.
 * All validation and payment logic lives in FeeService.
 * Each button has its own small listener (lambda) - no if/else chain.
 */
public class FeeWindow extends JFrame {

    private final JTextField regNo = new JTextField(15);
    private final JTextField amount = new JTextField(15);
    private final JButton pay = new JButton("Pay");
    private final JButton clear = new JButton("Clear");
    private final JButton receipt = new JButton("Receipt");
    private final JLabel status = new JLabel("Enter details and press Pay.");
    private final FeeService service;
    private boolean paymentInProgress = false;

    public FeeWindow(FeeService service) {
        super("Student Fee Payment");
        this.service = service;

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("Register No:")); form.add(regNo);
        form.add(new JLabel("Amount (Rs):")); form.add(amount);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(pay); buttons.add(clear); buttons.add(receipt);
        receipt.setEnabled(false);                       // enabled only after a successful payment

        add(form, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        // One listener per button
        pay.addActionListener(e -> startPayment());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Cleared."); });
        receipt.addActionListener(e -> showReceipt());

        // Window closing: ask first if a payment is in progress
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (paymentInProgress) {
                    int choice = JOptionPane.showConfirmDialog(FeeWindow.this,
                            "A payment is in progress. Close anyway?", "Confirm close",
                            JOptionPane.YES_NO_OPTION);
                    if (choice != JOptionPane.YES_OPTION) return;
                }
                dispose();
                System.exit(0);
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    /** Runs the slow work on a background thread so the window never freezes. */
    private void startPayment() {
        final String reg = regNo.getText();
        final String amt = amount.getText();
        setBusy(true);
        status.setText("Processing payment... please wait.");

        new SwingWorker<FeeService.Receipt, Void>() {
            @Override protected FeeService.Receipt doInBackground() throws Exception {
                return service.pay(reg, amt);            // validation + bank wait + database
            }
            @Override protected void done() {            // back on the Event Dispatch Thread
                setBusy(false);
                try {
                    FeeService.Receipt r = get();
                    status.setText("Payment successful for " + r.regNo());
                    receipt.setEnabled(true);
                    JOptionPane.showMessageDialog(FeeWindow.this, "Payment successful!");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ex) {
                    showError(ex.getCause());
                }
            }
        }.execute();
    }

    private void showError(Throwable cause) {
        String msg;
        if (cause instanceof FeeService.ValidationException) msg = cause.getMessage();
        else if (cause instanceof SQLException) msg = "Database error: " + cause.getMessage();
        else msg = "Unexpected error: " + cause.getMessage();
        status.setText(msg);
        JOptionPane.showMessageDialog(this, msg, "Payment failed", JOptionPane.ERROR_MESSAGE);
    }

    private void showReceipt() {
        FeeService.Receipt r = service.getLastReceipt();
        if (r == null) {
            JOptionPane.showMessageDialog(this, "No payment has been made yet.");
        } else {
            JOptionPane.showMessageDialog(this, r.toString(), "Fee Receipt", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setBusy(boolean busy) {
        paymentInProgress = busy;
        pay.setEnabled(!busy);
        clear.setEnabled(!busy);
    }

    public static void main(String[] args) {
        // If FEE_DB_PASSWORD is set, use MySQL; otherwise use the demo repository.
        FeeService.FeeRepository repo = System.getenv("FEE_DB_PASSWORD") != null
                ? new FeeService.JdbcFeeRepository()
                : new FeeService.InMemoryFeeRepository();
        SwingUtilities.invokeLater(() -> new FeeWindow(new FeeService(repo)).setVisible(true));
    }
}
