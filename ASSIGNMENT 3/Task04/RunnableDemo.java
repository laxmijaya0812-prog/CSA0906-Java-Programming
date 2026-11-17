class ScannerTask implements Runnable {
    private final String station;

    ScannerTask(String station) {
        this.station = station;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println(station + " scanned script " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class RunnableDemo {
    public static void main(String[] args)
            throws InterruptedException {

        Thread t1 = new Thread(new ScannerTask("Station 1"));
        Thread t2 = new Thread(new ScannerTask("Station 2"));
        Thread t3 = new Thread(new ScannerTask("Station 3"));
        Thread t4 = new Thread(new ScannerTask("Station 4"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        System.out.println("All stations completed.");
    }
}
