import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Entry point: wires the layers together (GUI -> Service -> DAO -> MySQL) and starts Swing on the EDT. */
public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // the default look and feel is fine
        }
        SwingUtilities.invokeLater(() -> {
            RegistrationDAO dao = new RegistrationDAO();
            RegistrationService service = new RegistrationService(dao);
            new RegistrationFrame(service).setVisible(true);
        });
    }
}
