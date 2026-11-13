public class StaticTicketDemo {

    static class ParkingTicket {

        private static int nextTicketNo = 1000;

        private final int ticketNo;

        ParkingTicket() {
            ticketNo = nextTicketNo++;
        }

        public void display() {
            System.out.println("Ticket Number: " + ticketNo);
        }
    }

    public static void main(String[] args) {

        ParkingTicket t1 = new ParkingTicket();
        ParkingTicket t2 = new ParkingTicket();
        ParkingTicket t3 = new ParkingTicket();

        t1.display();
        t2.display();
        t3.display();
    }
}
