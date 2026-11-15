import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.EventInfo;
import model.Registration;

/**
 * GUI layer. Contains NO SQL: it reads the form, calls RegistrationService and shows results.
 * Every database call is run in a SwingWorker (background thread) so the window stays responsive;
 * the results are shown back on the Event Dispatch Thread.
 */
public class RegistrationFrame extends JFrame {

    private final RegistrationService service;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ---- Student registration tab ----
    private final JTextField studentIdField = new JTextField(20);
    private final JTextField nameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JComboBox<EventInfo> eventBox = new JComboBox<>();
    private final JButton registerButton = new JButton("Register");
    private final JButton clearButton = new JButton("Clear");
    private final JLabel formStatus = new JLabel(" ");

    // ---- Committee tab ----
    private final JComboBox<EventInfo> filterBox = new JComboBox<>();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Student ID", "Name", "Email", "Event", "Registered At"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);
    private final JButton prevButton = new JButton("< Prev");
    private final JButton nextButton = new JButton("Next >");
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton exportButton = new JButton("Export CSV");
    private final JLabel pageLabel = new JLabel("Page 1");
    private final JLabel tableStatus = new JLabel(" ");

    private final JTabbedPane tabs = new JTabbedPane();
    private int currentPage = 0;
    private int totalRows = 0;
    private boolean updatingFilter = false;          // true while the filter combo box is being refilled
    private boolean registrationInProgress = false;  // used by the close-window confirmation

    public RegistrationFrame(RegistrationService service) {
        super("Campus Event Registration");
        this.service = service;

        setJMenuBar(buildMenuBar());
        tabs.addTab("Student Registration", buildStudentPanel());
        tabs.addTab("Committee View", buildCommitteePanel());
        add(tabs, BorderLayout.CENTER);

        // window closing: ask for confirmation if a registration is being saved
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { exitApplication(); }
        });

        setSize(820, 480);
        setLocationRelativeTo(null);
        loadEventsAndFirstPage();
    }

    // ===================================================================== building the screens

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem export = new JMenuItem("Export to CSV...");
        JMenuItem exit = new JMenuItem("Exit");
        export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        export.addActionListener(e -> exportRegistrations());
        exit.addActionListener(e -> exitApplication());
        file.add(export); file.addSeparator(); file.add(exit);

        JMenu view = new JMenu("View");
        JMenuItem student = new JMenuItem("Student Registration");
        JMenuItem committee = new JMenuItem("Committee View");
        JMenuItem refresh = new JMenuItem("Refresh");
        student.addActionListener(e -> tabs.setSelectedIndex(0));
        committee.addActionListener(e -> tabs.setSelectedIndex(1));
        refresh.addActionListener(e -> loadPage());
        view.add(student); view.add(committee); view.addSeparator(); view.add(refresh);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Campus Event Registration\nLakshmi Devi J - 192524300\nProgramming in Java (CSA09)",
                "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        bar.add(file); bar.add(view); bar.add(help);
        return bar;
    }

    private JPanel buildStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        addRow(panel, c, 0, "Student ID:", studentIdField);
        addRow(panel, c, 1, "Student Name:", nameField);
        addRow(panel, c, 2, "Email:", emailField);
        addRow(panel, c, 3, "Event:", eventBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(registerButton);
        buttons.add(clearButton);
        c.gridx = 1; c.gridy = 4;
        panel.add(buttons, c);

        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        panel.add(formStatus, c);

        registerButton.addActionListener(e -> registerStudent());
        clearButton.addActionListener(e -> clearForm());
        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row, String label, java.awt.Component field) {
        c.gridx = 0; c.gridy = row; c.gridwidth = 1; panel.add(new JLabel(label), c);
        c.gridx = 1; panel.add(field, c);
    }

    private JPanel buildCommitteePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Filter Event:"));
        top.add(filterBox);
        top.add(refreshButton);
        top.add(exportButton);
        panel.add(top, BorderLayout.NORTH);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        table.setAutoCreateRowSorter(true);

        JPanel bottom = new JPanel(new BorderLayout());
        JPanel paging = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paging.add(prevButton); paging.add(pageLabel); paging.add(nextButton);
        bottom.add(paging, BorderLayout.CENTER);
        bottom.add(tableStatus, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);

        // selecting an event in the filter reloads the table from page 1
        filterBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && !updatingFilter) {
                currentPage = 0;
                loadPage();
            }
        });
        refreshButton.addActionListener(e -> loadPage());
        exportButton.addActionListener(e -> exportRegistrations());
        prevButton.addActionListener(e -> { currentPage--; loadPage(); });
        nextButton.addActionListener(e -> { currentPage++; loadPage(); });
        return panel;
    }

    // ===================================================================== actions

    /** Reads the events (background thread), fills both combo boxes, then loads page 1. */
    private void loadEventsAndFirstPage() {
        formStatus.setText("Loading events...");
        runAsync(service::loadEvents, events -> {
            eventBox.removeAllItems();
            eventBox.addItem(new EventInfo(0, "-- Select an event --"));
            updatingFilter = true;
            filterBox.removeAllItems();
            filterBox.addItem(new EventInfo(0, "All Events"));
            for (EventInfo ev : events) { eventBox.addItem(ev); filterBox.addItem(ev); }
            updatingFilter = false;
            formStatus.setText(" ");
            loadPage();
        }, this::showFormError);
    }

    /** Register action: the service validates; the result is shown as success or error. */
    private void registerStudent() {
        final String id = studentIdField.getText();
        final String name = nameField.getText();
        final String email = emailField.getText();
        final EventInfo event = (EventInfo) eventBox.getSelectedItem();

        registrationInProgress = true;
        registerButton.setEnabled(false);
        setStatus(formStatus, "Saving registration...", Color.DARK_GRAY);

        runAsync(() -> service.register(id, name, email, event), message -> {
            registrationInProgress = false;
            registerButton.setEnabled(true);
            setStatus(formStatus, message, new Color(0, 130, 0));
            clearFields();
            loadPage();                              // refresh the committee table
        }, error -> {
            registrationInProgress = false;
            registerButton.setEnabled(true);
            showFormError(error);
        });
    }

    /** Filter / paging action: reads the selected event and loads one page into the JTable. */
    private void loadPage() {
        EventInfo selected = (EventInfo) filterBox.getSelectedItem();
        final int eventId = selected == null ? 0 : selected.getEventId();
        final int page = currentPage;
        setStatus(tableStatus, "Loading...", Color.DARK_GRAY);

        runAsync(() -> service.loadPage(eventId, page), result -> {
            tableModel.setRowCount(0);
            for (Registration r : result.rows()) {
                tableModel.addRow(new Object[]{r.getRegistrationId(), r.getStudentId(), r.getStudentName(),
                        r.getEmail(), r.getEventName(),
                        r.getRegisteredAt() == null ? "" : r.getRegisteredAt().format(TIME_FORMAT)});
            }
            totalRows = result.total();
            int pages = Math.max(1, (totalRows + RegistrationService.PAGE_SIZE - 1) / RegistrationService.PAGE_SIZE);
            if (currentPage >= pages) { currentPage = pages - 1; }
            pageLabel.setText("Page " + (currentPage + 1) + " of " + pages);
            prevButton.setEnabled(currentPage > 0);
            nextButton.setEnabled(currentPage < pages - 1);
            setStatus(tableStatus, totalRows + " registration(s) found.", Color.DARK_GRAY);
        }, error -> setStatus(tableStatus, error, Color.RED));
    }

    private void exportRegistrations() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("registrations.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) file = new File(file.getPath() + ".csv");
        if (file.exists() && JOptionPane.showConfirmDialog(this, file.getName() + " exists. Overwrite?",
                "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        EventInfo selected = (EventInfo) filterBox.getSelectedItem();
        final int eventId = selected == null ? 0 : selected.getEventId();
        final File target = file;
        setStatus(tableStatus, "Exporting...", Color.DARK_GRAY);
        runAsync(() -> service.exportCsv(eventId, target), count -> {
            setStatus(tableStatus, "Registration data exported successfully (" + count + " rows).",
                    new Color(0, 130, 0));
            JOptionPane.showMessageDialog(this, "Registration data exported successfully\n"
                    + target.getAbsolutePath() + "\nRows: " + count, "Export", JOptionPane.INFORMATION_MESSAGE);
        }, error -> {
            setStatus(tableStatus, error, Color.RED);
            JOptionPane.showMessageDialog(this, error, "Export failed", JOptionPane.ERROR_MESSAGE);
        });
    }

    private void exitApplication() {
        if (registrationInProgress && JOptionPane.showConfirmDialog(this,
                "A registration is being saved. Close anyway?", "Confirm close",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        dispose();
        System.exit(0);
    }

    // ===================================================================== helpers

    /**
     * Runs the work in a background thread (SwingWorker) and delivers the result - or the error
     * message - on the Event Dispatch Thread. A long database call on the EDT would freeze the window.
     */
    private <T> void runAsync(Callable<T> work, Consumer<T> onSuccess, Consumer<String> onError) {
        new SwingWorker<T, Void>() {
            @Override protected T doInBackground() throws Exception { return work.call(); }
            @Override protected void done() {
                try {
                    onSuccess.accept(get());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException ex) {
                    onError.accept(ex.getCause().getMessage());
                }
            }
        }.execute();
    }

    private void showFormError(String message) { setStatus(formStatus, "Error: " + message, Color.RED); }

    private void setStatus(JLabel label, String text, Color color) {
        label.setForeground(color);
        label.setText(text);
    }

    private void clearFields() {
        studentIdField.setText(""); nameField.setText(""); emailField.setText("");
        eventBox.setSelectedIndex(0);
    }

    private void clearForm() {
        clearFields();
        formStatus.setText(" ");
    }
}
