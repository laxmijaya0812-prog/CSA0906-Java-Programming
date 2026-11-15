import java.util.*;

class Patient {

    String name;
    int severity;
    int arrivalOrder;

    Patient(String name, int severity, int arrivalOrder) {
        this.name = name;
        this.severity = severity;
        this.arrivalOrder = arrivalOrder;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

public class Triage {

    public static void main(String[] args) {

        Comparator<Patient> comparator = (a, b) -> {

            if (a.severity != b.severity) {
                return Integer.compare(
                        a.severity, b.severity);
            }

            return Integer.compare(
                    a.arrivalOrder, b.arrivalOrder);
        };

        PriorityQueue<Patient> queue =
                new PriorityQueue<>(comparator);

        queue.add(new Patient("Meena", 1, 1));
        queue.add(new Patient("Kumar", 1, 2));
        queue.add(new Patient("Lata", 2, 3));
        queue.add(new Patient("Ravi", 3, 4));
        queue.add(new Patient("John", 3, 5));
        queue.add(new Patient("Asha", 5, 6));

        System.out.println("Treatment Order:");

        while (!queue.isEmpty()) {
            System.out.print(queue.poll() + " ");
        }
    }
}

Expected Output

Treatment Order:
Meena(S1) Kumar(S1) Lata(S2) Ravi(S3) John(S3) Asha(S5)

Priority Rules

Severity 1 → Critical

Severity 2 → Very High

Severity 3 → Moderate

Severity 4 → Low

Severity 5 → Minor

Patients with the same severity are processed according to arrival order.

Complexity

Insert into PriorityQueue: O(log n)

Remove highest-priority patient: O(log n)

View highest-priority patient: O(1)
