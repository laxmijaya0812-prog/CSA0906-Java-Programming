import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Task 3 - Campus Library Search Screen.
 *
 * Container structure (layout manager in brackets):
 * JFrame [BorderLayout]
 *   NORTH : topPanel [GridLayout 2x1]
 *             titleLabel
 *             searchRow [FlowLayout]  -> label, text field, category combo, Search button
 *   CENTER: JScrollPane -> JList (results)        <- the only region that grows
 *   SOUTH : bottomPanel [BorderLayout]
 *             CENTER: buttonRow [FlowLayout] -> Prev, Next, Reserve
 *             SOUTH : statusLabel
 */
public class LibrarySearch extends JFrame {

    private static final int PAGE_SIZE = 4;

    /** Small sample catalogue: {title, category}. */
    private static final String[][] CATALOGUE = {
        {"Programming in Java", "Computer Science"}, {"Data Structures in C", "Computer Science"},
        {"Operating System Concepts", "Computer Science"}, {"Database System Concepts", "Computer Science"},
        {"Engineering Mathematics", "Mathematics"}, {"Linear Algebra", "Mathematics"},
        {"Discrete Mathematics", "Mathematics"}, {"Engineering Physics", "Physics"},
        {"Basic Electrical Engineering", "Electronics"}, {"Digital Electronics", "Electronics"},
        {"Wings of Fire", "General"}, {"The Discovery of India", "General"}
    };

    private final JTextField searchField = new JTextField(15);
    private final JComboBox<String> categoryBox = new JComboBox<>(
            new String[]{"All", "Computer Science", "Mathematics", "Physics", "Electronics", "General"});
    private final JButton searchButton = new JButton("Search");
    private final JButton prevButton = new JButton("Prev");
    private final JButton nextButton = new JButton("Next");
    private final JButton reserveButton = new JButton("Reserve");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> resultList = new JList<>(listModel);
    private final JLabel statusLabel = new JLabel("Enter a title and press Search.");

    private List<String> matches = new ArrayList<>();
    private int page = 0;

    public LibrarySearch() {
        super("Campus Library Search");
        setLayout(new BorderLayout(5, 5));                       // frame: BorderLayout

        // ---- NORTH: title + search row --------------------------------------
        JLabel titleLabel = new JLabel("Campus Library", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));   // keeps natural size
        searchRow.add(new JLabel("Title:"));
        searchRow.add(searchField);
        searchRow.add(categoryBox);
        searchRow.add(searchButton);
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(titleLabel);
        topPanel.add(searchRow);
        add(topPanel, BorderLayout.NORTH);

        // ---- CENTER: results area (grows with the window) -------------------
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(resultList);
        scroll.setBorder(BorderFactory.createTitledBorder("Results"));
        add(scroll, BorderLayout.CENTER);

        // ---- SOUTH: button row + status -------------------------------------
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        buttonRow.add(prevButton);
        buttonRow.add(nextButton);
        buttonRow.add(reserveButton);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonRow, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 4, 8));
        add(bottomPanel, BorderLayout.SOUTH);

        // ---- Listeners ---------------------------------------------------------
        searchButton.addActionListener(e -> search());
        searchField.addActionListener(e -> search());            // Enter key
        prevButton.addActionListener(e -> { page--; showPage(); });
        nextButton.addActionListener(e -> { page++; showPage(); });
        reserveButton.addActionListener(e -> reserve());

        prevButton.setEnabled(false);
        nextButton.setEnabled(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private void search() {
        String text = searchField.getText().trim().toLowerCase();
        String category = (String) categoryBox.getSelectedItem();
        matches = new ArrayList<>();
        for (String[] book : CATALOGUE) {
            boolean titleOk = text.isEmpty() || book[0].toLowerCase().contains(text);
            boolean categoryOk = "All".equals(category) || book[1].equals(category);
            if (titleOk && categoryOk) matches.add(book[0] + "  [" + book[1] + "]");
        }
        page = 0;
        showPage();
        if (matches.isEmpty()) statusLabel.setText("No books found.");
    }

    private void showPage() {
        listModel.clear();
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, matches.size());
        for (int i = start; i < end; i++) listModel.addElement(matches.get(i));
        int totalPages = Math.max(1, (matches.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        prevButton.setEnabled(page > 0);
        nextButton.setEnabled(page < totalPages - 1);
        if (!matches.isEmpty())
            statusLabel.setText(matches.size() + " result(s) - page " + (page + 1) + " of " + totalPages);
    }

    private void reserve() {
        String selected = resultList.getSelectedValue();
        if (selected == null) {
            statusLabel.setText("Select a book first, then press Reserve.");
        } else {
            statusLabel.setText("Reserved: " + selected);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearch().setVisible(true));
    }
}
