public class ParkingTicket {

    private final int ticketNo;
    private final String vehicleNo;
    private final int floor;
    private final int slot;

    public ParkingTicket() {
        this(0, "UNKNOWN", -1, -1);
    }

    public ParkingTicket(int ticketNo, String vehicleNo) {
        this(ticketNo, vehicleNo, -1, -1);
    }

    public ParkingTicket(
            int ticketNo,
            String vehicleNo,
            int floor,
            int slot) {

        this.ticketNo = ticketNo;
        this.vehicleNo = vehicleNo;
        this.floor = floor;
        this.slot = slot;
    }

    public void display() {
        System.out.println("Ticket No: " + ticketNo);
        System.out.println("Vehicle No: " + vehicleNo);
        System.out.println("Floor: " + floor);
        System.out.println("Slot: " + slot);
    }

    public static void main(String[] args) {

        ParkingTicket t1 = new ParkingTicket();
        ParkingTicket t2 =
                new ParkingTicket(1001, "TN01AB1111");
        ParkingTicket t3 =
                new ParkingTicket(
                        1002,
                        "TN02CD2222",
                        1,
                        5);

        t1.display();
        t2.display();
        t3.display();
    }
}
