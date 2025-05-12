/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package stringpractice;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 *
 * @author hp
 */
// Created an abstract class Account of which no object can be instantiated in
// the main class but can only be inherited to other classes
// as the class Account is just an abstract concept.
abstract class Account {

    private String accNo;
    private String name;
    protected double balance;
    private String address;
    private String phoneNo;
    private String dob;

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

    public String getDob() {
        return dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    // constructors
    public Account(String accNo, String name, String address, String phoneNo, String dob) {
        this.accNo = accNo;
        this.name = name;
        this.dob = dob;
        this.setAddress(address);
        this.setPhoneNo(phoneNo);
        this.balance = 0.0;
    }

    public Account(String accNo, String name, String address, String phoneNo, String dob, double balance) {
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

class SavingsAccount extends Account {

    // Here we have nested FixedDeposit class inside SavingsAccount as we don't want
    // any other class to create an object of FixedDeposit
    // and want to allow only SavingsAccount to create FixedDeposit object.
    private class FixedDeposit extends Account {

        private String fdID;
        private double principalAmt;
        private int maturityInDays;
        private float interestRate;
        private double finalAmt;
        private byte frequency;
        private Date issueDate;
        private boolean isActive;

        private FixedDeposit(String accNo) {
            super(accNo);
            this.principalAmt = 0.0;
            this.finalAmt = 0.0;
            this.maturityInDays = 0;
            this.interestRate = 0.0f;
            this.frequency = (byte) 0;
            this.isActive = false;
        }

        private FixedDeposit(String accNo, String fdID, double principalAmt, int maturityInDays, byte frequency) {
            super(accNo);
            this.fdID = fdID;
            this.principalAmt = principalAmt;
            this.finalAmt = 0.0;
            this.maturityInDays = maturityInDays;
            this.frequency = frequency;
            this.isActive = true;
            this.issueDate = new Date();

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

        private Date getIssueDate() {
            return this.issueDate;
        }
    }

    private FixedDeposit[] fixedDeposits;
    private static int size;

    // Constructors
    public SavingsAccount(String accNo, String name, String address, String phoneNo, String dob) {
        super(accNo, name, address, phoneNo, dob);
        super.balance = 0.0;
        fixedDeposits = null;
    }

    public SavingsAccount(String accNo, String name, String address, String phoneNo, String dob, double balance) {
        super(accNo, name, address, phoneNo, dob, balance);
        fixedDeposits = null;
    }

    // Methods for depositing or withdrawing a certain amount
    public void deposit(float amt) {
        super.balance += amt;
    }

    public void withdraw(float amt) {
        super.balance -= amt;
    }

    // method for getting the balance
    public double getBalance() {
        return super.balance;
    }

    // method for validating Fixed Deposit ID
    public boolean validateID(String fdID) {
        if (!fdID.startsWith("FD"))
            return false;

        if (this.fixedDeposits == null)
            return true;

        for (FixedDeposit fixedDeposit : this.fixedDeposits) {
            if (fixedDeposit != null && fixedDeposit.fdID.equals(fdID) && !fixedDeposit.isActive)
                return false;
        }

        return true;
    }

    public boolean FDExists(String fdID) {
        if (this.fixedDeposits != null) {
            for (FixedDeposit fixedDeposit : this.fixedDeposits) {
                return fixedDeposit != null && fixedDeposit.fdID.equals(fdID) && fixedDeposit.isActive == true
                        && (int) fixedDeposit.principalAmt > 0;
            }
        }

        return false;
    }

    public boolean validateDate(String fdID, int year, int month, int day) {
        if (this.fixedDeposits != null) {
            for (FixedDeposit fixedDeposit : this.fixedDeposits) {
                if (fixedDeposit.fdID.equals(fdID))
                    return new Date(year, month, day).compareTo(fixedDeposit.issueDate) > 0 ? true : false;
            }
        }
        return true;
    }

    // Method for getting the details of the account
    public String toString() {
        return "Account Number:\n" + super.getAccountNo() + "\nBalance\n" + super.balance + "\nAccount Holder Name:\n"
                + super.getName() + "\nAccount Holder contact number:\n" + super.getPhoneNo();
    }

    // Methods associated with creating and breaking fixed deposits.
    public void createFixedDeposit(String fdID, double principalAmt, int maturityInDays, byte frequency) {
        if (principalAmt > super.balance) {
            System.out.println("Not enough balance");
            System.exit(0);
        }

        if (this.fixedDeposits == null) {
            this.fixedDeposits = new FixedDeposit[100];
            size = 0;
        }

        for (FixedDeposit fixedDeposit : this.fixedDeposits) {
            if (fixedDeposit != null && fixedDeposit.fdID.equals(fdID) && fixedDeposit.isActive == true
                    && (int) fixedDeposit.principalAmt > 0) {
                System.out.println("You cannot create new FD with the same ID instead you can continue it.");
                return;
            }
        }

        this.fixedDeposits[size++] = new FixedDeposit(super.getAccountNo(), fdID, principalAmt, maturityInDays,
                frequency);
        super.balance -= principalAmt;
    }

    public double amountAtMaturity(String fdID) {
        for (FixedDeposit fixedDeposit : this.fixedDeposits) {
            if (fixedDeposit.fdID.equals(fdID)) {
                if (fixedDeposit.isActive == false) {
                    return -2.0;
                }

                return fixedDeposit.principalAmt
                        * Math.pow((1 + (fixedDeposit.interestRate / 100) / fixedDeposit.frequency),
                                (fixedDeposit.frequency * fixedDeposit.maturityInDays / 365.0));
            }
        }
        return -1.0;
    }

    public double amountAtWithdrawal(String fdID, int year, int month, int date) {
        for (FixedDeposit fixedDeposit : this.fixedDeposits) {
            if (fixedDeposit.fdID.equals(fdID)) {
                long numberOfDaysElapsed = (new Date(year, month, date).getTime()
                        - fixedDeposit.issueDate.getTime())
                        / (1000 * 60 * 60 * 24);

                float revisedInterestRate = setRevisedInterestRate(numberOfDaysElapsed);
                revisedInterestRate = Math.min(revisedInterestRate - 1, fixedDeposit.interestRate - 1);

                return fixedDeposit.principalAmt
                        * Math.pow((1 + (revisedInterestRate / 100) / fixedDeposit.frequency),
                                (fixedDeposit.frequency * (numberOfDaysElapsed / 365.0)));
            }
        }
        return -1.0;
    }

    public double breakFixedDeposit(String fdID, int year, int month, int date) {
        for (FixedDeposit fixedDeposit : this.fixedDeposits) {
            if (fixedDeposit.fdID.equals(fdID)) {
                if (fixedDeposit.isActive == false) {
                    return -2.0;
                }

                long numberOfDaysElapsed = (new Date(year, month, date).getTime()
                        - fixedDeposit.getIssueDate().getTime())
                        / (1000 * 60 * 60 * 24);

                if (numberOfDaysElapsed < fixedDeposit.maturityInDays) {
                    return -3.0;
                }

                double finalAmt = fixedDeposit.principalAmt
                        * Math.pow((1 + (fixedDeposit.interestRate / 100) / fixedDeposit.frequency),
                                (fixedDeposit.frequency * fixedDeposit.maturityInDays / 365.0));
                fixedDeposit.finalAmt = finalAmt;
                fixedDeposit.isActive = false;
                super.balance += finalAmt;

                return finalAmt;
            }
        }
        return -1.0;
    }

    private float setRevisedInterestRate(long numberOfDaysElapsed) {
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
        for (FixedDeposit fixedDeposit : this.fixedDeposits) {
            if (fixedDeposit.fdID.equals(fdID)) {
                long numberOfDaysElapsed = (new Date(year, month, days).getTime()
                        - fixedDeposit.issueDate.getTime())
                        / (1000 * 60 * 60 * 24);

                float revisedInterestRate = setRevisedInterestRate(numberOfDaysElapsed);
                revisedInterestRate = Math.min(revisedInterestRate - 1, fixedDeposit.interestRate - 1);

                double expectedAmt = fixedDeposit.principalAmt
                        * Math.pow((1 + (fixedDeposit.interestRate / 100) / fixedDeposit.frequency),
                                (fixedDeposit.frequency * (numberOfDaysElapsed / 365.0)));

                double realAmt = fixedDeposit.principalAmt
                        * Math.pow((1 + (revisedInterestRate / 100) / fixedDeposit.frequency),
                                (fixedDeposit.frequency * (numberOfDaysElapsed / 365.0)));

                if (withdrawalAmount > realAmt)
                    return -2.0;

                fixedDeposit.principalAmt = realAmt - withdrawalAmount;
                System.out.println("-------------------------------------------------");
                System.out.printf("Remaining Principal Amount:\n%.2f\n", fixedDeposit.principalAmt);
                System.out.println("-------------------------------------------------");

                fixedDeposit.maturityInDays -= numberOfDaysElapsed;
                fixedDeposit.issueDate = new Date(year, month, days);

                if ((int) fixedDeposit.principalAmt == 0)
                    fixedDeposit.isActive = false;

                super.balance += withdrawalAmount;
                return withdrawalAmount;
            }
        }
        return -1.0;
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
    private Date dueDate;
    private boolean isActive;
    private SavingsAccount savingsAccount;

    // getters and setters
    public String getLoanType() {
        return this.LoanType;
    }

    public double getPayableAmount() {
        return this.finalAmt;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    // Constructors
    private LoanAccount(String accNo, String name, String address, String phoneNo, String dob, double balance) {
        super(accNo, name, address, phoneNo, dob, balance);
    }

    private LoanAccount(String accNo, String name, String address, String phoneNo, String dob, double balance,
            String LoanType, String LoanID, byte frequency, byte tenureInYears, String dueDate,
            SavingsAccount savingsAccount) {
        super(accNo, name, address, phoneNo, dob, balance);
        this.LoanType = LoanType;
        this.LoanID = LoanID;
        this.frequency = frequency;
        this.tenure = tenureInYears;
        this.loanRepayed = 0.0;
        this.isActive = true;
        this.savingsAccount = savingsAccount;

        this.principalAmt = super.balance;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false); // Strict validation

        try {
            this.dueDate = sdf.parse(dueDate);
        } catch (ParseException ex) {
            Logger.getLogger(LoanAccount.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    }

    private LoanAccount(String accNo, String name, String address, String phoneNo, String dob, double balance,
            String LoanType, String LoanID, byte frequency, int downPayment, byte tenureInYears, String dueDate,
            SavingsAccount savingsAccount) {
        this(accNo, name, address, phoneNo, dob, balance, LoanType, LoanID, frequency, tenureInYears, dueDate,
                savingsAccount);
        this.finalAmt -= downPayment;
    }

    // methods
    public static LoanAccount issueLoan(String accNo, String name, String address, String phoneNo, String dob,
            double balance,
            String LoanType, String LoanID, byte frequency, int downPayment, byte tenureInYears, String dueDate,
            SavingsAccount savingsAccount) {
        return new LoanAccount(accNo, name, address, phoneNo, dob, balance, LoanType, LoanID, frequency, downPayment,
                tenureInYears,
                dueDate, savingsAccount);
    }

    public double returnRemainingAmount() {
        return this.finalAmt - this.loanRepayed;
    }

    public void scheduleEMI(double installment) {
        this.loanRepayed += installment;
        if (installment > this.savingsAccount.getBalance()) {
            System.out.println("You have to face a penalty as you don't have enough balance!");
            System.exit(0);
        }
        this.savingsAccount.withdraw(installment);
    }

    public int repayLoan() {
        this.loanRepayed = this.finalAmt;
        if (this.returnRemainingAmount() > this.savingsAccount.getBalance()) {
            System.out.println("Cannot repay the loan altogether at once instead go for the EMI option.");
            return -1;
        }
        this.savingsAccount.withdraw(this.returnRemainingAmount());
        this.isActive = false;
        return 0;
    }

    public String toString() {
        return "Type of Loan:\n" + this.LoanType + "\nLoan Amount:\n" + super.balance + "\nPayable Amount:\n"
                + this.finalAmt + "\nDue Date:\n" + this.dueDate + "\nTenure of the Loan\n" + this.dueDate;
    }

}

public class AccountManagement002 {

    /**
     * @param args the command line arguments
     */
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

    public static boolean validateAccountNumber(String accNo) {
        return accNo.matches("\\d{11,16}");
    }

    public static boolean validatePhoneNo(String accNo) {
        return accNo.matches("\\d{10}");
    }

    public static void FDCreation(SavingsAccount acc1) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Fixed Deposit ID:");
        // System.out.println(sc.hasNextLine());
        String fdID = sc.nextLine();

        if (acc1.validateID(fdID) == false) {
            System.err.println("Fixed Deposit is inactive or invalid.");
            System.exit(0);
        }

        boolean doesFDAlreadyExist = acc1.FDExists(fdID);

        if (!doesFDAlreadyExist) {

            System.out.println("Enter the amount to invest:");
            double investment = sc.nextDouble();

            if (investment > acc1.getBalance()) {
                System.err.println("Not enough balance");
                System.exit(0);
            }

            System.out.println("Enter the maturity of the deposit(in days):");
            int maturityInDays = sc.nextInt();
            if (maturityInDays > 3650) {
                System.err.println("You can only create a fixed deposit for a maximum of 10 years.");
                System.exit(0);
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
        sc.nextLine();
        String initialDecision = sc.nextLine();
        if (initialDecision.toUpperCase().equals("NO") || initialDecision.toUpperCase().equals("N")) {
            sc.close();
            return;
        }

        System.out.println("-------------------------------------------------");
        System.out.println("Enter the current year, month and date respectively");
        int year = sc.nextInt();
        year -= 1900;
        int month = sc.nextInt();
        int day = sc.nextInt();
        if (!acc1.validateDate(fdID, year, month, day)) {
            System.out.println("Enter a valid date after the Fixed Deposit issue date");
            System.exit(0);
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
                }

                else if (decision.toUpperCase().equals("No") || decision.toUpperCase().equals("N"))
                    break;

                else {
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

        // sc.close();

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println();

        System.out.println("Enter the account number: ");
        String accNo = sc.nextLine();
        if (validateAccountNumber(accNo) == false) {
            System.out.println("Invalid account number");
            System.exit(0);
        }

        // String buffer = sc.nextLine();
        System.out.println("Enter the name of the account holder: ");
        String name = sc.nextLine();
        if (validateName(name) == false) {
            System.out.println("Invalid name");
            System.exit(0);
        }
        // sc.nextLine();

        System.out.println("Enter the address of the account holder: ");
        String address = sc.nextLine();
        if (address.length() < 10) {
            System.out.println("Invalid address");
            System.exit(0);
        }

        System.out.println("Enter the contact number of the account holder: ");
        String phoneNo = sc.nextLine();
        if (validatePhoneNo(phoneNo) == false) {
            System.out.println("Invalid contact number");
            System.exit(0);
        }
        // sc.nextLine();

        System.out.println("Enter the date of birth of the account holder: ");
        String dob = sc.nextLine();

        System.out.println("Enter the balance of the account holder: ");
        double balance = sc.nextLong();

        SavingsAccount acc1 = new SavingsAccount(accNo, name, address, phoneNo, dob, balance);

        System.out.println("------------------------------------------------");
        System.out.println(acc1);
        System.out.println("------------------------------------------------");

        System.out.println("Do you want to open a fixed deposit:");
        sc.nextLine();
        String decision = sc.nextLine();

        if (decision.toUpperCase().equals("YES") || decision.toUpperCase().equals("Y")) {
            FDCreation(acc1);
            System.out.println("ACCOUNT DETAILS:");
            System.out.println("------------------------------------------------");
            System.out.println(acc1);
            System.out.println("------------------------------------------------");

            System.out.println("------------------------------------------------");
            System.out.println("Do you want to open another fixed deposit?:");
            
            decision = sc.nextLine();
        }

        System.out.println("Do you want to issue a loan?");
        // sc.nextLine();
        decision = sc.nextLine();

        if (decision.toUpperCase().equals("NO") || decision.toUpperCase().equals("N"))
            System.exit(0);

        System.out.println("Available Loans:");
        String[] LoanTypes = { "Gold Loan", "Education Loan", "Auto Loan", "Personal Loan", "Home Loan" };

        for (int i = 0; i < LoanTypes.length; i++) {
            System.out.println(LoanTypes[i] + " " + (i + 1));
        }

        System.out.println("Enter the index of the Loan that you wan to issue:");
        int index = sc.nextInt();

        if (index > 5 || index <= 0) {
            System.out.println("Invalid Index");
            System.exit(0);
        }

        System.out.println("Enter the Loan ID:");
        sc.nextLine();
        String LoanID = sc.nextLine();

        if (!LoanID.matches("SBI-LOAN-\\d{8}")) {
            System.out.println("Invalid Loan Id");
            System.exit(0);
        }

        System.out.println("Enter downpayment amount:");
        int downPayment = sc.nextInt();

        System.out.println("Enter tenure of the loan in years:");
        byte tenureInYears = sc.nextByte();

        LoanAccount loanAccount = LoanAccount.issueLoan(accNo, name, address, phoneNo, dob, balance,
                LoanTypes[index - 1],
                LoanID, (byte) 12, downPayment, tenureInYears, "30-02-2025", acc1);
        sc.close();

        // dueDate validation and other date refactoring into Calendar object after you
        // learn about Dates in the course.

        // methods to implement in LoanAccount -> toString() and issueDate and dueDate
        // and LoanAccount testing

    }
}
