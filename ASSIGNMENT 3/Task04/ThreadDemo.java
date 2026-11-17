class ScannerStation extends Thread {
    private final String stationName;

    ScannerStation(String name) {
        stationName = name;
    }

    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(stationName + " scanned script " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class ThreadDemo {
    public static void main(String[] args)
            throws InterruptedException {

        ScannerStation s1 = new ScannerStation("Station 1");
        ScannerStation s2 = new ScannerStation("Station 2");
        ScannerStation s3 = new ScannerStation("Station 3");
        ScannerStation s4 = new ScannerStation("Station 4");

        s1.start();
        s2.start();
        s3.start();
        s4.start();

        System.out.println("Station 1 alive: " + s1.isAlive());

        s1.join();
        s2.join();
        s3.join();
        s4.join();

        System.out.println("All stations completed.");
        System.out.println("Station 1 alive after join: " + s1.isAlive());
    }
}
