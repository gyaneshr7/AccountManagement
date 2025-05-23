package accountmanagement;

import accountmanagement.SavingsAccount;
import java.util.*;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Gyanesh
 */
public class AccountInfo {
    private static TreeMap<String,SavingsAccount> tempdatabase;
    
    public static void viewAllAccounts(){
        tempdatabase.forEach((key,value)->{
            System.out.println("---------------------------");
            System.out.println("Account number of the account holder: "+key);
            System.out.println("Name of the account holder: "+value.getName());
            System.out.println("Balance of the account holder: "+value.getBalance());
            System.out.println("Phone number of the account holder: "+value.getPhoneNo());
            System.out.println("Date of Birth of the account holder: "+value.getDob());
            System.out.println("---------------------------");
        });
    }
    public static void main(String[] args) {
        // import the treemap from the database file to the current program
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.txt"));
            tempdatabase = (TreeMap)ois.readObject();
            System.out.println(tempdatabase);
        }
        catch(ClassNotFoundException ex){
            System.err.println("The bank database is empty. Please create an account first.");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        
//        viewAllAccounts();
    }
    
}
