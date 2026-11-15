package model;

import java.time.LocalDateTime;

/** Model: one row of the registrations table joined with student and event details. */
public class Registration {
    private final int registrationId;
    private final String studentId;
    private final String studentName;
    private final String email;
    private final int eventId;
    private final String eventName;
    private final LocalDateTime registeredAt;

    public Registration(int registrationId, String studentId, String studentName, String email,
                        int eventId, String eventName, LocalDateTime registeredAt) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
        this.eventId = eventId;
        this.eventName = eventName;
        this.registeredAt = registeredAt;
    }

    /** Factory for a new registration typed in the form (no id / timestamp yet). */
    public static Registration forNewEntry(String studentId, String studentName, String email, int eventId) {
        return new Registration(0, studentId, studentName, email, eventId, null, null);
    }

    public int getRegistrationId() { return registrationId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getEmail() { return email; }
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
}
