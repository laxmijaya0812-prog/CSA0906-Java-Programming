import java.util.HashSet;

class Student {

    private final String regNo;
    private final String name;

    public Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Student))
            return false;

        Student other = (Student) obj;

        return regNo.equals(other.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }

    @Override
    public String toString() {
        return regNo + " - " + name;
    }
}

public class Attendance {

    public static void main(String[] args) {

        HashSet<Student> students = new HashSet<>();

        students.add(new Student("S101", "Arun"));
        students.add(new Student("S102", "Meena"));
        students.add(new Student("S101", "Arun"));

        System.out.println("Unique students: "
                + students.size());

        for (Student student : students) {
            System.out.println(student);
        }
    }
}

Expected Result

Unique students: 2

Key Points

equals() determines whether two objects are logically equal.

hashCode() provides the hash value used by hash-based collections.

If two objects are equal using equals(), they must have the same hashCode().

HashSet uses both hashCode() and equals() to avoid duplicate objects.
