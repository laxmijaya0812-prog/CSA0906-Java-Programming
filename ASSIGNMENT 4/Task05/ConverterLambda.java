import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Style (d): lambda expression. PDF program with TODO 1 and TODO 2 completed. */
public class ConverterLambda extends JFrame {
    /** Task 5.2: true = also show the result in a dialog box. Set to false for the plain Task 5 behaviour. */
    private static final boolean SHOW_DIALOG = true;

    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");

    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    public ConverterLambda() {
        super("Currency Converter");
        setLayout(new FlowLayout());
        JButton convertButton = new JButton("Convert");
        add(new JLabel("INR")); add(amount); add(target);
        add(convertButton); add(result);

        // TODO 1 (completed): ONE lambda listener for both the Convert button and the
        // Enter key in the amount field
        ActionListener convertListener = e -> convert();
        convertButton.addActionListener(convertListener);
        amount.addActionListener(convertListener);

        addWindowListener(new WindowAdapter() {                 // window closing with WindowAdapter
            @Override public void windowClosing(WindowEvent e) { System.out.println("Converter closing"); }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void convert() {
        // TODO 2 (completed): parse the amount and show the converted value with 2 decimals,
        // or "Invalid amount" if parsing fails
        try {
            double inr = Double.parseDouble(amount.getText().trim());
            if (inr < 0 || Double.isNaN(inr) || Double.isInfinite(inr)) throw new NumberFormatException();
            String currency = (String) target.getSelectedItem();
            String text = String.format("%.2f %s", inr * rate(currency), currency);
            result.setText(text);
            if (SHOW_DIALOG) JOptionPane.showMessageDialog(this, text, "Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
            if (SHOW_DIALOG) JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
        pack();                                                  // resize so the longer text fits
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterLambda().setVisible(true));
    }
}
