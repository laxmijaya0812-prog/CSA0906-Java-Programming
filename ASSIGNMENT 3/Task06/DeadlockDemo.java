class Wallet {
    int balance;

    Wallet(int balance) {
        this.balance = balance;
    }
}

public class DeadlockDemo {

    static void transfer(Wallet from, Wallet to, int amount) {
        synchronized (from) {
            System.out.println("Locked source wallet");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (to) {
                from.balance -= amount;
                to.balance += amount;
                System.out.println("Transfer completed");
            }
        }
    }

    public static void main(String[] args) {
        Wallet a = new Wallet(1000);
        Wallet b = new Wallet(1000);

        Thread t1 = new Thread(() -> transfer(a, b, 100));
        Thread t2 = new Thread(() -> transfer(b, a, 200));

        t1.start();
        t2.start();
    }
}
