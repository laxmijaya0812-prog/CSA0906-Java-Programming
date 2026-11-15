import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import model.EventInfo;
import model.Registration;

/**
 * Service layer: business rules (validation, duplicate check), paging and CSV export.
 * It contains no Swing code and no SQL. Every problem is reported to the GUI as a
 * RegistrationException carrying a user-friendly message.
 */
public class RegistrationService {

    public static final int PAGE_SIZE = 10;

    private static final Pattern STUDENT_ID = Pattern.compile("[A-Za-z0-9]{3,20}");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /** The one exception type the GUI has to handle. */
    public static class RegistrationException extends Exception {
        public RegistrationException(String message) { super(message); }
        public RegistrationException(String message, Throwable cause) { super(message, cause); }
    }

    /** One page of rows plus the total number of matching rows. */
    public record PageResult(List<Registration> rows, int total) { }

    private final RegistrationDAO dao;

    public RegistrationService(RegistrationDAO dao) { this.dao = dao; }

    public List<EventInfo> loadEvents() throws RegistrationException {
        try {
            return dao.findAllEvents();
        } catch (SQLException ex) {
            throw new RegistrationException("Could not load events: " + ex.getMessage(), ex);
        }
    }

    /** Validates the input, rejects duplicates and saves the registration. Returns the success message. */
    public String register(String studentId, String name, String email, EventInfo event)
            throws RegistrationException {
        studentId = studentId == null ? "" : studentId.trim();
        name = name == null ? "" : name.trim();
        email = email == null ? "" : email.trim();

        if (studentId.isEmpty()) throw new RegistrationException("Student ID is required.");
        if (!STUDENT_ID.matcher(studentId).matches())
            throw new RegistrationException("Student ID must be 3-20 letters or digits.");
        if (name.isEmpty()) throw new RegistrationException("Student name is required.");
        if (name.length() > 100) throw new RegistrationException("Student name is too long (max 100).");
        if (email.isEmpty()) throw new RegistrationException("Email is required.");
        if (!EMAIL.matcher(email).matches()) throw new RegistrationException("Email address is not valid.");
        if (event == null || event.getEventId() <= 0) throw new RegistrationException("Please select an event.");

        try {
            if (dao.exists(studentId, event.getEventId()))
                throw new RegistrationException("Student already registered for this event.");
            dao.save(Registration.forNewEntry(studentId, name, email, event.getEventId()));
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062)       // duplicate key: two people registered at the same moment
                throw new RegistrationException("Student already registered for this event.", ex);
            throw new RegistrationException("Database error: " + ex.getMessage(), ex);
        }
        return "Registration successful: " + name + " -> " + event.getEventName();
    }

    /** Page numbers start at 0. eventId 0 = all events. */
    public PageResult loadPage(int eventId, int page) throws RegistrationException {
        try {
            int total = dao.count(eventId);
            List<Registration> rows = dao.findPage(eventId, PAGE_SIZE, page * PAGE_SIZE);
            return new PageResult(rows, total);
        } catch (SQLException ex) {
            throw new RegistrationException("Could not load registrations: " + ex.getMessage(), ex);
        }
    }

    /** Writes all registrations of the event (0 = all) to a CSV file; returns the number of rows. */
    public int exportCsv(int eventId, File file) throws RegistrationException {
        try {
            List<Registration> rows = dao.findAll(eventId);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            try (BufferedWriter w = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                w.write("Registration ID,Student ID,Name,Email,Event,Registered At");
                w.newLine();
                for (Registration r : rows) {
                    w.write(String.join(",", String.valueOf(r.getRegistrationId()), csv(r.getStudentId()),
                            csv(r.getStudentName()), csv(r.getEmail()), csv(r.getEventName()),
                            r.getRegisteredAt() == null ? "" : r.getRegisteredAt().format(fmt)));
                    w.newLine();
                }
            }
            return rows.size();
        } catch (SQLException ex) {
            throw new RegistrationException("Could not read registrations: " + ex.getMessage(), ex);
        } catch (IOException ex) {
            throw new RegistrationException("Could not write the file: " + ex.getMessage(), ex);
        }
    }

    /** Quotes a CSV field when needed. */
    private static String csv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n"))
            return "\"" + value.replace("\"", "\"\"") + "\"";
        return value;
    }
}
