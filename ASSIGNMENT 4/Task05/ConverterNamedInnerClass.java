import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
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

/** Style (b): a named inner class implements ActionListener. */
public class ConverterNamedInnerClass extends JFrame {
    /** Task 5.2: true = also show the result in a dialog box. Set to false for the plain Task 5 behaviour. */
    private static final boolean SHOW_DIALOG = true;

    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");
    private final JButton convertButton = new JButton("Convert");

    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    public ConverterNamedInnerClass() {
        super("Currency Converter");
        setLayout(new FlowLayout());
        add(new JLabel("INR")); add(amount); add(target);
        add(convertButton); add(result);

        ConvertListener listener = new ConvertListener();
        convertButton.addActionListener(listener);
        amount.addActionListener(listener);             // Enter key in the amount field

        addWindowListener(new WindowAdapter() {                 // window closing with WindowAdapter
            @Override public void windowClosing(WindowEvent e) { System.out.println("Converter closing"); }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    /** Named inner class - can use the frame's private fields and methods directly. */
    private class ConvertListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            convert();
        }
    }

    private void convert() {
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
        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConverterNamedInnerClass().setVisible(true));
    }
}
