package model;

/** Model: an event shown in the combo boxes. */
public class EventInfo {
    private final int eventId;
    private final String eventName;

    public EventInfo(int eventId, String eventName) {
        this.eventId = eventId;
        this.eventName = eventName;
    }

    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }

    /** JComboBox shows toString(). */
    @Override public String toString() { return eventName; }
}
