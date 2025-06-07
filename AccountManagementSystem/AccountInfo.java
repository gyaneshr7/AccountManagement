package accountmanagement;

import accountmanagement.SavingsAccount;
import java.util.*;
import java.io.*;

/** @author Gyanesh */
public class AccountInfo {

    private static HashMap<String, SavingsAccount> tempdatabase;

    public static void viewAllAccounts() {
        tempdatabase.forEach((key, value) -> {
            viewAccount(key);
        });
    }

    public static void viewAccount(String accNo) {
        SavingsAccount acc = tempdatabase.get(accNo);
        System.out.println("---------------------------");
        System.out.println("Account number of the account holder: " + accNo);
        System.out.println("Name of the account holder: " + acc.getName());
        System.out.println("Balance of the account holder: " + acc.getBalance());
        System.out.println("Phone number of the account holder: " + acc.getPhoneNo());
        System.out.println("Date of Birth of the account holder: " + acc.getDob());
        System.out.println("---------------------------");
    }

    public static void main(String[] args) {
        // import the hashmap from the database file to the current program
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.txt"));
            tempdatabase = (HashMap) ois.readObject();
        } catch (ClassNotFoundException ex) {
            System.err.println("The bank database is empty. Please create an account first.");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
        if(tempdatabase.isEmpty()){
            System.err.println("The bank database is empty. Please create an account first.");
            return;
        }

        System.out.println("For viewing a specefic account, press 1.");
        System.out.println("For viewing the information related to all the accounts, press 2.");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();

        while (true) {
            switch (option) {
                case 1:
                    System.out.println("Enter the account number:");
                    sc.nextLine();
                    String accno = sc.nextLine();

                    while (!tempdatabase.containsKey(accno)) {
                        System.out.println("Please enter a valid account number or press -1 to exit.");
                        accno = sc.nextLine();
                        if (accno.equals("-1"))
                            System.exit(0);
                    }

                    viewAccount(accno);
                    sc.close();
                    return;

                case 2:
                    viewAllAccounts();
                    sc.close();
                    return;

                default:
                    System.out.println("Please enter a valid number else press -1 to exit.");
                    option = sc.nextInt();
                    if (option == -1) {
                        sc.close();
                        System.exit(0);
                    }
                    break;
            }
        }
    }
}
