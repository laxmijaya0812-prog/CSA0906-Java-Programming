import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

public class ATM {

    static void withdraw(double balance, double amount)
            throws InsufficientBalanceException {

        if (amount < 0) {
            throw new IllegalArgumentException(
                    "Withdrawal amount cannot be negative");
        }

        if (amount > balance) {
            throw new InsufficientBalanceException(
                    "Insufficient account balance");
        }

        System.out.println("Withdrawal successful");
        System.out.println("Amount withdrawn: Rs." + amount);
        System.out.println("Remaining balance: Rs." + (balance - amount));
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter account number: ");
            long account = sc.nextLong();

            System.out.print("Enter withdrawal amount: ");
            double amount = sc.nextDouble();

            FileInputStream file = new FileInputStream("cardreader.txt");
            file.close();

            double balance = 5000;
            withdraw(balance, amount);

        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter numeric input.");

        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (FileNotFoundException e) {
            System.out.println("Error: Card reader file not found.");

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());

        } finally {
            System.out.println("ATM transaction completed.");
            sc.close();
        }
    }
}
