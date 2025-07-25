package accountmanagement;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

/**
 * @author Gyanesh
 */
class InvalidDateException extends Exception {
}

// Created an abstract class Account of which no object can be instantiated in
// the main class but can only be inherited to other classes
// as the class Account is just an abstract concept.
abstract class Account implements Serializable {

    private String accNo;
    private String name;
    private double balance;
    private String address;
    private String phoneNo;
    private LocalDate dob;

    private static final long serialVersionUID = 1L;

    // getters and setters
    public String getAccountNo() {
        return accNo;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    void setBalance(double balance) {
        this.balance = balance;
    }

    //non-parameterized constructor initialized for Serialization
    public Account() {
    }

    // constructors
    public Account(String accNo, String name, String address, String phoneNo, LocalDate dob) {
        this.accNo = accNo;
        this.name = name;
        this.dob = dob;
        this.setAddress(address);
        this.setPhoneNo(phoneNo);
        this.balance = 0.0;
    }

    public Account(String accNo, String name, String address, String phoneNo, LocalDate dob, double balance) {
        this.accNo = accNo;
        this.name = name;
        this.dob = dob;
        this.setAddress(address);
        this.setPhoneNo(phoneNo);
        this.balance = balance;
    }

    protected Account(String accNo) {
        this.accNo = accNo;
    }

    // methods
    public void closeAccount() {
        this.accNo = null;
        this.name = null;
        this.dob = null;
        this.phoneNo = null;
        this.balance = 0;
    }
}

class SavingsAccount extends Account implements Serializable {

    // Here we have nested FixedDeposit class inside SavingsAccount as we don't want
    // any other class to create an object of FixedDeposit
    // and want to allow only SavingsAccount to create FixedDeposit object.
    private class FixedDeposit implements Serializable {

        private String accNo;
        private String name;
        private String fdID;
        private double principalAmt;
        private int maturityInDays;
        private float interestRate;
        private double finalAmt;
        private byte frequency;
        private LocalDate issueDate;
        private boolean isActive;
        private static boolean nextFlag = false;
        private static LocalDate recentFDBreakDate = null;

        private static final long serialVersionUID = 2L;

        private FixedDeposit(String accNo, String name, String fdID, double principalAmt, int maturityInDays, byte frequency) {
            this.accNo = accNo;
            this.name = name;
            this.fdID = fdID;
            this.principalAmt = principalAmt;
            this.finalAmt = 0.0;
            this.maturityInDays = maturityInDays;
            this.frequency = frequency;
            this.isActive = true;
            this.issueDate = nextFlag ? recentFDBreakDate : LocalDate.now();

            if (maturityInDays < 7) {
                this.interestRate = 0.0f;
            } else if (maturityInDays < 46) {
                this.interestRate = 3.0f;
            } else if (maturityInDays < 181) {
                this.interestRate = 4.5f;
            } else if (maturityInDays < 365) {
                this.interestRate = 5.75f;
            } else if (maturityInDays == 365) {
                this.interestRate = 6.5f;
            } else if (maturityInDays < 400) {
                this.interestRate = 7.25f;
            } else if (maturityInDays < 501) {
                this.interestRate = 7.75f;
            } else if (maturityInDays < 731) {
                this.interestRate = 7.25f;
            } else if (maturityInDays < 1096) {
                this.interestRate = 6.8f;
            } else if (maturityInDays <= 3650) {
                this.interestRate = 6.5f;
            }
        }

        private LocalDate getIssueDate() {
            return this.issueDate;
        }
    }

    private TreeMap<String, FixedDeposit> fixedDeposits;

    private static final long serialVersionUID = 2L;
    private transient boolean deserialized = false;

    //method for implementing method-access block after deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Restore fields
        deserialized = true;    // Custom logic
    }

    private void checkAccess() {
        if (deserialized) {
            throw new SecurityException("Methods cannot be accessed after deserialization.");
        }
    }

    //non-parameterized constructor initialized for Serialization
    public SavingsAccount() {
        checkAccess();
        System.out.println("Savings Account Instantiated");
    }

    // Methods for depositing or withdrawing a certain amount
    public void deposit(float amt) {
        checkAccess();
        float finalAmt = (float) getBalance() + amt;
        setBalance(finalAmt);
    }

    public void withdraw(float amt) {
        checkAccess();
        float finalAmt = (float) getBalance() - amt;
        setBalance(finalAmt);
    }

    // Constructors
    public SavingsAccount(String accNo, String name, String address, String phoneNo, LocalDate dob) {
        super(accNo, name, address, phoneNo, dob);
        checkAccess();
        super.setBalance(0.0);
        fixedDeposits = null;
    }

    public SavingsAccount(String accNo, String name, String address, String phoneNo, LocalDate dob, double balance) {
        super(accNo, name, address, phoneNo, dob, balance);
        checkAccess();
        fixedDeposits = null;
    }

    // method for validating Fixed Deposit ID
    public boolean validateID(String fdID) {
        checkAccess();
        if (!fdID.startsWith("FD")) {
            return false;
        }

        if (this.fixedDeposits == null) {
            return true;
        }

        if (fixedDeposits.containsKey(fdID) && !fixedDeposits.get(fdID).isActive) {
            return false;
        }

        return true;
    }

    public boolean ActiveFDExisits(String fdID) {
        checkAccess();
        if (this.fixedDeposits != null) {
            FixedDeposit fixedDeposit = fixedDeposits.get(fdID);
            return fixedDeposit != null && fixedDeposit.isActive == true
                    && (int) fixedDeposit.principalAmt > 0;
        }

        return false;
    }

    public void validateDate(String fdID, int year, int month, int day) throws InvalidDateException, DateTimeException {
        checkAccess();
        if (this.fixedDeposits != null) {
            FixedDeposit fixedDeposit = fixedDeposits.get(fdID);
            if (fixedDeposit != null && LocalDate.of(year, month, day).compareTo(fixedDeposit.issueDate) <= 0) {
                throw new InvalidDateException();
            }
        }
    }

    // Method for getting the details of the account
    public String toString() {
        return "Account Number:\n" + super.getAccountNo() + "\nBalance\n" + super.getBalance() + "\nAccount Holder Name:\n"
                + super.getName() + "\nAccount Holder contact number:\n" + super.getPhoneNo();
    }

    // Methods associated with creating and breaking fixed deposits.
    public void createFixedDeposit(String fdID, double principalAmt, int maturityInDays, byte frequency) {
        checkAccess();
        if (principalAmt > super.getBalance()) {
            System.out.println("Not enough balance");
            System.exit(0);
        }

        if (this.fixedDeposits == null) {
            this.fixedDeposits = new TreeMap<>();
        }

        FixedDeposit fixedDeposit = fixedDeposits.get(fdID);
        if (fixedDeposit != null && fixedDeposit.isActive == true
                && (int) fixedDeposit.principalAmt > 0) {
            System.out.println("You cannot create new FD with the same ID instead you can continue it.");
            return;
        }

        fixedDeposits.putIfAbsent(fdID, new FixedDeposit(super.getAccountNo(), super.getName(), fdID, principalAmt, maturityInDays,
                frequency));

        double currBalance = getBalance();
        setBalance(currBalance - principalAmt);
    }

    public double amountAtMaturity(String fdID) {
        checkAccess();

        FixedDeposit fixedDeposit = fixedDeposits.get(fdID);
        if (fixedDeposit != null) {
            if (fixedDeposit.isActive == false) {
                return -2.0;
            }

            return fixedDeposit.principalAmt
                    * Math.pow((1 + (fixedDeposit.interestRate / 100) / fixedDeposit.frequency),
                            (fixedDeposit.frequency * fixedDeposit.maturityInDays / 365.0));
        }
        return -1.0;
    }

    public double amountAtWithdrawal(String fdID, int year, int month, int days) {
        checkAccess();
        FixedDeposit fixedDeposit = fixedDeposits.get(fdID);

        if (fixedDeposit != null) {
            long numberOfDaysElapsed = fixedDeposit.issueDate.until(LocalDate.of(year, month, days), ChronoUnit.DAYS);

            float revisedInterestRate = setRevisedInterestRate(numberOfDaysElapsed);
            revisedInterestRate = Math.min(revisedInterestRate - 1, fixedDeposit.interestRate - 1);

            return fixedDeposit.principalAmt
                    * Math.pow((1 + (revisedInterestRate / 100) / fixedDeposit.frequency),
                            (fixedDeposit.frequency * (numberOfDaysElapsed / 365.0)));
        }
        return -1.0;
    }

    public double breakFixedDeposit(String fdID, int year, int month, int days) {
        checkAccess();
        FixedDeposit fixedDeposit = fixedDeposits.get(fdID);
        if (fixedDeposit != null) {
            if (fixedDeposit.isActive == false) {
                return -2.0;
            }

            FixedDeposit.nextFlag = true;
            FixedDeposit.recentFDBreakDate = LocalDate.of(year, month, days);

            long numberOfDaysElapsed = fixedDeposit.issueDate.until(LocalDate.of(year, month, days), ChronoUnit.DAYS);

            if (numberOfDaysElapsed < fixedDeposit.maturityInDays) {
                return -3.0;
            }

            double finalAmt = fixedDeposit.principalAmt
                    * Math.pow((1 + (fixedDeposit.interestRate / 100) / fixedDeposit.frequency),
                            (fixedDeposit.frequency * fixedDeposit.maturityInDays / 365.0));

            fixedDeposit.finalAmt = finalAmt;
            fixedDeposit.isActive = false;

            double currBalance = getBalance();
            setBalance(currBalance + finalAmt);
            return finalAmt;
        }
        return -1.0;
    }

    private float setRevisedInterestRate(long numberOfDaysElapsed) {
        checkAccess();
        if (numberOfDaysElapsed < 7) {
            return 0.0f;
        } else if (numberOfDaysElapsed < 46) {
            return 3.0f;
        } else if (numberOfDaysElapsed < 181) {
            return 4.5f;
        } else if (numberOfDaysElapsed < 365) {
            return 5.75f;
        } else if (numberOfDaysElapsed == 365) {
            return 6.5f;
        } else if (numberOfDaysElapsed < 400) {
            return 7.25f;
        } else if (numberOfDaysElapsed < 501) {
            return 7.75f;
        } else if (numberOfDaysElapsed < 731) {
            return 7.25f;
        } else if (numberOfDaysElapsed < 1096) {
            return 6.8f;
        } else if (numberOfDaysElapsed <= 3650) {
            return 6.5f;
        }

        return 7.25f;
    }

    public double liquidate(String fdID, double withdrawalAmount, int year, int month, int days) {
        checkAccess();
        FixedDeposit fixedDeposit = fixedDeposits.get(fdID);
        if (fixedDeposit != null) {

            FixedDeposit.nextFlag = true;
            FixedDeposit.recentFDBreakDate = LocalDate.of(year, month, days);

            long numberOfDaysElapsed = fixedDeposit.issueDate.until(LocalDate.of(year, month, days), ChronoUnit.DAYS);

            float revisedInterestRate = setRevisedInterestRate(numberOfDaysElapsed);
            revisedInterestRate = Math.min(revisedInterestRate - 1, fixedDeposit.interestRate - 1);

            double expectedAmt = fixedDeposit.principalAmt
                    * Math.pow((1 + (fixedDeposit.interestRate / 100) / fixedDeposit.frequency),
                            (fixedDeposit.frequency * (numberOfDaysElapsed / 365.0)));

            double realAmt = fixedDeposit.principalAmt
                    * Math.pow((1 + (revisedInterestRate / 100) / fixedDeposit.frequency),
                            (fixedDeposit.frequency * (numberOfDaysElapsed / 365.0)));

            if (withdrawalAmount > realAmt) {
                return -2.0;
            }

            fixedDeposit.principalAmt = realAmt - withdrawalAmount;
            System.out.println("-------------------------------------------------");
            System.out.printf("Remaining Principal Amount:\n%.2f\n", fixedDeposit.principalAmt);
            System.out.println("-------------------------------------------------");

            fixedDeposit.maturityInDays -= numberOfDaysElapsed;
            fixedDeposit.issueDate = LocalDate.of(year, month, days);

            if ((int) fixedDeposit.principalAmt == 0) {
                fixedDeposit.isActive = false;
            }

//                super.balance += withdrawalAmount;
            double currBalance = getBalance();
            setBalance(currBalance + withdrawalAmount);
            return withdrawalAmount;
        }

        return -1.0;
    }
}

class NotEnoughBalanceException extends Exception {

    public String toString() {
        return "Not Enough Balance";
    }
}

class NotEnoughAgeException extends Exception {

    public String toString() {
        return "Account Holder must be atleast 10 years of age.";
    }
}

class LoanAccount extends Account {

    private String LoanID;
    private String LoanType;
    private float interestRate;
    private byte frequency;
    private double principalAmt;
    private double finalAmt;
    private double loanRepayed;
    private byte tenure;
    private int monthsElapsed = 0;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private boolean isActive;
    private SavingsAccount savingsAccount;
    private double EMI;

    // getters and setters
    public String getLoanType() {
        return this.LoanType;
    }

    public double getPayableAmount() {
        return this.finalAmt;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public byte getTenure() {
        return this.tenure;
    }

    // Constructors
    private LoanAccount(String accNo, String name, String address, String phoneNo, LocalDate dob, double balance) {
        super(accNo, name, address, phoneNo, dob, balance);
    }

    private LoanAccount(String accNo, String name, String address, String phoneNo, LocalDate dob, double balance,
            String LoanType, String LoanID, byte frequency, byte tenureInYears,
            SavingsAccount savingsAccount) {
        super(accNo, name, address, phoneNo, dob, balance);
        this.LoanType = LoanType;
        this.LoanID = LoanID;
        this.frequency = frequency;
        this.tenure = tenureInYears;
        this.loanRepayed = 0.0;
        this.isActive = true;
        this.savingsAccount = savingsAccount;
        this.issueDate = LocalDate.now();
        this.principalAmt = super.getBalance();
        this.dueDate = issueDate.plusYears(tenureInYears);

        switch (LoanType) {
            case "Home Loan":
                this.interestRate = 8.25f;
                break;

            case "Personal Loan":
                this.interestRate = 14.50f;
                break;

            case "Auto Loan":
                this.interestRate = 9.20f;
                break;

            case "Education Loan":
                this.interestRate = 8.15f;
                break;

            case "Gold Loan":
                this.interestRate = 9.00f;
                break;

            default:
                this.interestRate = 8.75f;
                break;
        }

        this.finalAmt = this.principalAmt
                * Math.pow((1 + (this.interestRate / 100) / this.frequency),
                        (this.frequency * this.tenure));

        this.EMI = getEMI();
    }

    private LoanAccount(String accNo, String name, String address, String phoneNo, LocalDate dob, double balance,
            String LoanType, String LoanID, byte frequency, int downPayment, byte tenureInYears,
            SavingsAccount savingsAccount) {
        this(accNo, name, address, phoneNo, dob, balance, LoanType, LoanID, frequency, tenureInYears,
                savingsAccount);
        this.finalAmt -= downPayment;
    }

    // methods
    public static LoanAccount issueLoan(String accNo, String name, String address, String phoneNo, LocalDate dob,
            double balance,
            String LoanType, String LoanID, byte frequency, int downPayment, byte tenureInYears,
            SavingsAccount savingsAccount) {
        return new LoanAccount(accNo, name, address, phoneNo, dob, balance, LoanType, LoanID, frequency, downPayment,
                tenureInYears,
                savingsAccount);
    }

    public float returnRemainingAmount() {
        return (float) (this.getEMI() * this.tenure * 12 - this.loanRepayed);
    }

    public float getEMI() {
        return (float) (this.principalAmt * (this.interestRate / 1200) * Math.pow(1 + (this.interestRate / 1200), this.tenure * 12) / (Math.pow(1 + (this.interestRate / 1200), this.tenure * 12) - 1));
    }

    public int getMonthsElapsed() {
        return this.monthsElapsed;
    }

    public void scheduleEMI() throws NotEnoughBalanceException, InterruptedException {
        while (monthsElapsed < tenure * 12) {
            if (this.EMI > this.savingsAccount.getBalance()) {
                throw new NotEnoughBalanceException();
            }

            Thread.sleep(1000);
            monthsElapsed++;

            if (this.EMI <= this.returnRemainingAmount()) {
                this.loanRepayed += this.EMI;
                this.savingsAccount.withdraw((float) this.EMI);
            } else {
                repayRemainingLoan();
            }

            System.out.println(monthsElapsed + " month EMI paid.");
        }
    }

    public void closeEMI() {
        this.monthsElapsed = 0;
    }

    public void repayRemainingLoan() throws NotEnoughBalanceException, InterruptedException {
        if (this.returnRemainingAmount() > this.savingsAccount.getBalance()) {
            System.out.println("Cannot repay the loan altogether at once instead go for the EMI option.");
            scheduleEMI();
        }

        this.savingsAccount.withdraw((float) this.returnRemainingAmount());
        this.loanRepayed = this.finalAmt;
        this.isActive = false;
    }

    public String toString() {
        return "Type of Loan:\n" + this.LoanType + "\nLoan Amount:\n" + super.getBalance() + "\nPayable Amount:\n"
                + this.finalAmt + "\nDue Date:\n" + this.dueDate + "\nTenure of the Loan\n" + this.tenure;
    }
}

public class AccountManagement002 {

    public static boolean validateName(String name) {
        String[] arr = name.split("\\s+");
        if (arr.length < 2 || arr[0].matches("[A-Z][a-z]+") == false) {
            return false;
        }

        for (int i = 1; i < arr.length; i++) {
            if (arr[i].matches("[A-Z][a-z]*") == false) {
                return false;
            }
        }

        return true;
    }

    public static boolean validateAccountNumber(String accNo, HashMap<String, SavingsAccount> tempdatabase) {
        return accNo.matches("\\d{11,16}");
    }

    public static boolean validatePhoneNo(String phoneNo) {
        return phoneNo.matches("[6-9]\\d{9}");
    }

    public static void FDCreation(SavingsAccount acc1) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Fixed Deposit ID:");
        String fdID = sc.nextLine();

        while (acc1.validateID(fdID) == false) {
            System.err.println("Fixed Deposit is inactive or invalid. Please enter a valid ID or press 1 to exit.");
            fdID = sc.nextLine();
            if (fdID.equals("1")) {
                System.exit(0);
            }
        }

        boolean doesFDAlreadyExist = acc1.ActiveFDExisits(fdID);
        boolean skipNewLine = true;

        if (!doesFDAlreadyExist) {
            System.out.println("Enter the amount to invest:");
            skipNewLine = false;
            double investment = sc.nextDouble();

            while (investment > acc1.getBalance()) {
                System.err.println("Not enough balance");
                System.out.println("Do you want to continue?");
                String initialDecision = sc.nextLine();

                if (initialDecision.toUpperCase().equals("NO") || initialDecision.toUpperCase().equals("N")) {
                    sc.close();
                    return;
                }

                investment = sc.nextDouble();
            }

            System.out.println("Enter the maturity of the deposit(in days):");
            int maturityInDays = sc.nextInt();
            while (maturityInDays > 3650) {
                System.err.println("You can only create a fixed deposit for a maximum of 10 years.");
                System.out.println("Please enter a valid tenure, else press -1 to exit.");
                maturityInDays = sc.nextInt();

                if (maturityInDays == -1) {
                    System.exit(0);
                }
            }

            acc1.createFixedDeposit(fdID, investment, maturityInDays, (byte) 12);
            System.out.println("-------------------------------------------------");
            System.out.println("FD Created");
            System.out.println("-------------------------------------------------");
        }

        System.out.println("Amount at maturity:");
        System.out.printf("%.2f\n", acc1.amountAtMaturity(fdID));
        System.out.println("-------------------------------------------------");

        System.out.println("Do you want to break the FD?");

        if (!skipNewLine) {
            sc.nextLine();
        }

        String initialDecision = sc.nextLine();
        if (initialDecision.toUpperCase().equals("NO") || initialDecision.toUpperCase().equals("N")) {
            sc.close();
            return;
        }

        System.out.println("-------------------------------------------------");
        System.out.println("Enter the current year, month and date respectively");
        int year = sc.nextInt();
        int month = sc.nextInt();
        int day = sc.nextInt();

        //Validation to check whether the year, month and date entered are correct or not
        
        while (true) {
            try {
                acc1.validateDate(fdID, year, month, day);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please enter a valid date after the issue date.");
                year = sc.nextInt();
                month = sc.nextInt();
                day = sc.nextInt();
            }
        }

        double finalAmount = acc1.breakFixedDeposit(fdID, year, month, day);

        switch ((int) finalAmount) {
            case -2:
                System.err.println("FD not Active");
                System.exit(0);
                break;

            case -3:
                System.err.println("Cannot break FD before maturity");
                System.out.println("If you withdraw the amount now, you can withdraw upto:\n"
                        + acc1.amountAtWithdrawal(fdID, year, month, day));
                System.out.println("-------------------------------------------------");

                System.out.println("Do you want to liquidate it?");
                String buffer2 = sc.nextLine();
                String decision = sc.nextLine();

                if (decision.toUpperCase().equals("YES") || decision.toUpperCase().equals("Y")) {
                    System.out.println("Enter the amount to withdraw: ");
                    int withdrawalAmount = sc.nextInt();
                    double finalAmountAfterLiquidation = acc1.liquidate(fdID, withdrawalAmount, year, month, day);
                    System.out.println(
                            finalAmountAfterLiquidation > 0 ? "FD Broken at Amount Rs. " + finalAmountAfterLiquidation
                                    : "Not enough balance!!");
                } else if (decision.toUpperCase().equals("No") || decision.toUpperCase().equals("N")) {
                    break;
                } else {
                    System.err.println("Invalid input");
                    System.exit(0);
                }
                break;

            case -1:
                System.err.println("Some error occured");
                System.exit(0);
                break;

            default:
                System.out.println("-------------------------------------------------");
                System.out.printf("FD Broken at Amount Rs. %.2f\n", finalAmount);
                System.out.println("-------------------------------------------------");
                break;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // import the hashmap from the database file to the current program
        
        HashMap<String, SavingsAccount> tempdatabase;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("database.txt"));
            tempdatabase = (HashMap) ois.readObject();
        } catch (Exception ex) {
            tempdatabase = new HashMap<>();
        }

        System.out.println("Enter the account number:");
        String accNo = sc.nextLine();
        while (validateAccountNumber(accNo, tempdatabase) == false) {
            System.out.println("The account number entered is invalid. Please enter a valid account number or enter 1 to exit: ");
            accNo = sc.nextLine();
            if (accNo.equals("1")) {
                System.exit(0);
            }
        }

        // maintain a properties data structure for storing the account number and passwords as the key-value pairs
        
        Properties credentials = new Properties();
        try {
            credentials.loadFromXML(new FileInputStream("passwordsdb.xml"));
        } catch (Exception e) {
            /* any exception would mean either the file is not created yet or the file is empty.
            In either case the exception is ignored when the program is run for the first time. */
        }

        String psswrd;

        // check whether the account number exists in tempdatabase or not
        
        if (tempdatabase.get(accNo) != null) {
        
            /* if the account number already exists prompt the user to enter the password for the account number
            and validate it */
            
            System.out.println("Enter the password assosciated with this account number to access it.");
            int count = 3;
            psswrd = sc.nextLine();

            while (!credentials.getProperty(accNo).equals(psswrd) && count > 0) {
                System.out.println("You have " + count-- + " attempts to enter the correct password.");
                psswrd = sc.nextLine();
            }

            if (count == 0) {
                System.out.println("Please try again after some time.");
                System.exit(0);
            }
        } else {
            String confirm_psswrd;
            System.out.println("Thank you for creating an account. Please enter a new password.");

            do {
                psswrd = sc.nextLine();

                /*                Explanation:

                           ^                 # start-of-string
                           (?=.*[0-9])       # a digit must occur at least once
                           (?=.*[a-z])       # a lower case letter must occur at least once
                           (?=.*[A-Z])       # an upper case letter must occur at least once
                           (?=.*[@#$%^&+=])  # a special character must occur at least once
                           (?=\S+$)          # no whitespace allowed in the entire string
                           .{8,}             # anything, at least eight places though
                           $                 # end-of-string                                    */
                
                while (!psswrd.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
                    System.err.println("Please create a strong password that should have at least:");
                    System.err.println("A length of 8 characters.");
                    System.err.println("One lowercase letter");
                    System.err.println("One Uppercase letter");
                    System.err.println("One digit");
                    System.err.println("One special character");
                    System.err.println("No whitespaces");

                    psswrd = sc.nextLine();
                }
                
                System.out.println("Confirm new password.");
                confirm_psswrd = sc.nextLine();

                if (!psswrd.equals(confirm_psswrd)) {
                    System.out.println("Password entered didn't match. Please try again.");
                }
            } while (!psswrd.equals(confirm_psswrd));

            //set the password for the newly created account number
            credentials.setProperty(accNo, psswrd);
        }

        System.out.println("Enter the name of the account holder: ");
        String name = sc.nextLine();

        while (validateName(name) == false) {
            System.out.println("Invalid name. Please enter a valid name or press 1 to exit.");
            name = sc.nextLine();
            if (name.equals("1")) {
                System.exit(0);
            }
        }

        System.out.println("Enter the address of the account holder: ");
        String address = sc.nextLine();

        if (address.length() < 10) {
            System.out.println("Invalid address");
            System.exit(0);
        }

        System.out.println("Enter the contact number of the account holder: ");
        String phoneNo = sc.nextLine();

        while (validatePhoneNo(phoneNo) == false) {
            System.out.println("Invalid contact number");
            phoneNo = sc.nextLine();

            if (phoneNo.equals("1")) {
                System.exit(0);
            }
        }

        System.out.println("Enter the date of birth(dd-MM-yyyy) of the account holder: ");
        String dateOfBirth = sc.nextLine();
        LocalDate dob = null;

        while (true) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(java.time.format.ResolverStyle.STRICT);
                dob = LocalDate.parse(dateOfBirth, formatter);

                if (dob.until(LocalDate.now(), ChronoUnit.YEARS) < 10) {
                    throw new NotEnoughAgeException();
                }
                break;
            } catch (DateTimeException ex) {
                System.out.println("Please enter a valid date.");
                dateOfBirth = sc.nextLine();
            } catch (NotEnoughAgeException ex) {
                System.out.println(ex);
                System.exit(0);
            }
        }

        System.out.println("Enter the balance of the account holder: ");
        double balance = sc.nextLong();

        SavingsAccount acc1 = new SavingsAccount(accNo, name, address, phoneNo, dob, balance);

        System.out.println("------------------------------------------------");
        System.out.println(acc1);
        System.out.println("------------------------------------------------");

        System.out.println("Do you want to open a fixed deposit?:");
        sc.nextLine();
        String decision = sc.nextLine();

        while (decision.toUpperCase().equals("YES") || decision.toUpperCase().equals("Y")) {
            FDCreation(acc1);
            System.out.println("ACCOUNT DETAILS:");
            System.out.println("------------------------------------------------");
            System.out.println(acc1);
            System.out.println("------------------------------------------------");

            System.out.println("------------------------------------------------");
            System.out.println("Do you want to open another fixed deposit?:");

            decision = sc.nextLine();
        }

        tempdatabase.put(accNo, acc1);

        try {
            //save the tempdatabase to database.txt file
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("database.txt"));
            oos.writeObject(tempdatabase);

            //save the 'credentials' properties to the passwordsdb.xml file
            credentials.storeToXML(new FileOutputStream("passwordsdb.xml"), "Updated passwords list");

            System.out.println("Account saved successfully");
        } catch (IOException ex) {
            System.err.println("Some error occured while saving the account in the database");
            System.out.println(ex.getMessage());
            System.exit(0);
        }

        System.out.println("Do you want to issue a loan?");
        decision = sc.nextLine();

        if (decision.toUpperCase().equals("NO") || decision.toUpperCase().equals("N")) {
            System.exit(0);
        }

        System.out.println("Available Loans:");
        String[] LoanTypes = {"Gold Loan", "Education Loan", "Auto Loan", "Personal Loan", "Home Loan"};

        for (int i = 0; i < LoanTypes.length; i++) {
            System.out.println(LoanTypes[i] + " " + (i + 1));
        }

        System.out.println("Enter the index of the Loan that you wan to issue:");
        int index = sc.nextInt();

        while (index > 5 || index <= 0) {
            System.out.println("Invalid Index");
            System.out.println("Please enter a valid index or press -1 to exit.");
            index = sc.nextInt();
            if (index == -1) {
                System.exit(0);
            }
        }

        System.out.println("Enter the Loan ID:");
        sc.nextLine();
        String LoanID = sc.nextLine();

        while (!LoanID.matches("SBI-LOAN-\\d{8}")) {
            System.out.println("Invalid Loan Id. Please Enter again.");
            LoanID = sc.nextLine();
        }

        System.out.println("Enter the amount of loan you want to issue: ");
        long principalAmt = sc.nextLong();

        System.out.println("Enter downpayment amount:");
        int downPayment = sc.nextInt();

        System.out.println("Enter tenure of the loan in years:");
        byte tenureInYears = sc.nextByte();

        LoanAccount loanAccount = LoanAccount.issueLoan(accNo, name, address, phoneNo, dob, principalAmt,
                LoanTypes[index - 1],
                LoanID, (byte) 12, downPayment, tenureInYears, acc1);

        System.out.println("The payable amount after " + loanAccount.getTenure() + " years would be " + loanAccount.getPayableAmount());
        System.out.println("EMI aggregation-> " + loanAccount.getEMI() * tenureInYears * 12 + " Final amount -> " + loanAccount.getPayableAmount());

        System.out.println("How do you want to proceed with the loan?");
        System.out.println("Press 1 for the EMI option and 2 for lumpsum amount.");

        int option = sc.nextInt();

        do {
            try {
                switch (option) {
                    case 1:
                        System.out.println("Your EMI amount is " + loanAccount.getEMI());
                        loanAccount.scheduleEMI();
                        break;

                    case 2:
                        System.out.println("Amount left to pay is " + loanAccount.returnRemainingAmount());
                        loanAccount.repayRemainingLoan();
                        break;

                    default:
                        System.out.println("Enter a valid value:");
                        break;
                }
            } catch (NotEnoughBalanceException ex) {
                System.out.println(ex);
                System.out.println("Be prepared for the penalty!!");
                System.exit(0);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        } while (option != 1 && option != 2);

        System.out.println("Loan repaid successfully");
        sc.close();
    }
}
