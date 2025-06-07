package accountmanagement;

import accountmanagement.SavingsAccount;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author hp
 */
public class AccountDeletion {

    private static HashMap<String, SavingsAccount> tempdatabase;

    public static void main(String[] args) {
        // import the hashmap from the database file to the current program
        try (Scanner sc = new Scanner(System.in)) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.txt"));
            tempdatabase = (HashMap) ois.readObject();

            System.out.println("Enter the account number to delete:");
            String accno = sc.nextLine();

            while (!tempdatabase.containsKey(accno)) {
                System.out.println("Please enter a valid account number or press -1 to exit.");
                accno = sc.nextLine();

                if (accno.equals("-1")) {
                    System.exit(0);
                }
            }
            tempdatabase.remove(accno);

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("database.txt"));
            oos.writeObject(tempdatabase);
            System.out.println("Account deleted successfully!");

        } catch (ClassNotFoundException ex) {
            System.err.println("The bank database is empty. Please create an account first.");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
