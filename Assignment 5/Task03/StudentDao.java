import java.sql.*;
import java.util.*;

public class StudentDao implements Dao<Student, Integer> {
    private final Connection con;

    public StudentDao(Connection con) {
        this.con = con;
    }

    public void insert(Student s) throws SQLException {
        String sql = "INSERT INTO student(id,name,register_no,department) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getRegisterNo());
            ps.setString(4, s.getDepartment());
            ps.executeUpdate();
        }
    }

    public Student findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM student WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("register_no"),
                        rs.getString("department"));
                }
            }
        }
        return null;
    }

    public List<Student> findAll() throws SQLException {
        List<Student> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM student");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("register_no"),
                    rs.getString("department")));
            }
        }
        return list;
    }

    public void update(Student s) throws SQLException {
        String sql = "UPDATE student SET name=?, register_no=?, department=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getRegisterNo());
            ps.setString(3, s.getDepartment());
            ps.setInt(4, s.getId());
            ps.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        try (PreparedStatement ps =
                 con.prepareStatement("DELETE FROM student WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
