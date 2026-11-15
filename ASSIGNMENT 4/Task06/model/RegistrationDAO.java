import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.EventInfo;
import model.Registration;

/**
 * Data-access layer: ALL SQL and JDBC code is here. Every query uses a PreparedStatement and
 * try-with-resources. No Swing classes are used, so it can be called from any thread.
 */
public class RegistrationDAO {

    private static final String SELECT_REGISTRATIONS =
        "SELECT r.registration_id, s.student_id, s.name, s.email, e.event_id, e.event_name, r.registered_at "
      + "FROM registrations r "
      + "JOIN students s ON s.student_id = r.student_id "
      + "JOIN events e   ON e.event_id   = r.event_id "
      + "WHERE (? = 0 OR r.event_id = ?) "          // 0 means "all events"
      + "ORDER BY r.registration_id ";

    public List<EventInfo> findAllEvents() throws SQLException {
        String sql = "SELECT event_id, event_name FROM events ORDER BY event_name";
        List<EventInfo> events = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) events.add(new EventInfo(rs.getInt(1), rs.getString(2)));
        }
        return events;
    }

    public boolean exists(String studentId, int eventId) throws SQLException {
        String sql = "SELECT 1 FROM registrations WHERE student_id = ? AND event_id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    /**
     * Saves the student (insert or update) and the registration in ONE transaction.
     * If the UNIQUE(student_id, event_id) constraint is violated, MySQL throws SQLException
     * with error code 1062 and the whole transaction is rolled back.
     */
    public void save(Registration reg) throws SQLException {
        String studentSql = "INSERT INTO students (student_id, name, email) VALUES (?, ?, ?) "
                          + "ON DUPLICATE KEY UPDATE name = VALUES(name), email = VALUES(email)";
        String regSql = "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";
        try (Connection c = DatabaseConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps1 = c.prepareStatement(studentSql);
                 PreparedStatement ps2 = c.prepareStatement(regSql)) {
                ps1.setString(1, reg.getStudentId());
                ps1.setString(2, reg.getStudentName());
                ps1.setString(3, reg.getEmail());
                ps1.executeUpdate();
                ps2.setString(1, reg.getStudentId());
                ps2.setInt(2, reg.getEventId());
                ps2.executeUpdate();
                c.commit();
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            }
        }
    }

    public int count(int eventId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registrations WHERE (? = 0 OR event_id = ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        }
    }

    /** One page of registrations (SQL LIMIT / OFFSET). eventId 0 = all events. */
    public List<Registration> findPage(int eventId, int limit, int offset) throws SQLException {
        return query(SELECT_REGISTRATIONS + "LIMIT ? OFFSET ?", eventId, limit, offset);
    }

    /** All registrations (used for export). eventId 0 = all events. */
    public List<Registration> findAll(int eventId) throws SQLException {
        return query(SELECT_REGISTRATIONS, eventId, -1, -1);
    }

    private List<Registration> query(String sql, int eventId, int limit, int offset) throws SQLException {
        List<Registration> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setInt(2, eventId);
            if (limit >= 0) { ps.setInt(3, limit); ps.setInt(4, offset); }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("registered_at");
                    list.add(new Registration(rs.getInt("registration_id"), rs.getString("student_id"),
                            rs.getString("name"), rs.getString("email"), rs.getInt("event_id"),
                            rs.getString("event_name"), ts == null ? null : ts.toLocalDateTime()));
                }
            }
        }
        return list;
    }
}
