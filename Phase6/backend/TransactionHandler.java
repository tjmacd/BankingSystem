import java.text.DecimalFormat;

// This file makes the changes to the account objects based on the transaction which is passed to it

public class TransactionHandler {
    static FileHandler fileHandler;
    
    // This function handles withdrawal   
    public static void withdraw(Transaction transaction){ // Withdrawal
        Account selAccount = compareAccountNumber(transaction); // Compare each account to the account number given in the transaction
        if (!(selAccount.accountName.equals("NULL"))) {
            if (transaction.transactionAmount > selAccount.accountBalance) {
                System.out.println("ERROR: Insufficient funds");
            } else {
            selAccount.accountBalance -= transaction.transactionAmount; // Subtract the amount from the account total
            chargeFee(transaction); // Charge the fee based on the account type
            }
        } else {
            System.out.println("Error: Invalid account"); // Error handling
        }  
    }
    
 // This function handles deposits
    public static void deposit(Transaction transaction){ // Deposit
        Account selAccount = compareAccountNumber(transaction); // Compare each account to the account number given in the transaction
        if (!(selAccount.accountName.equals("NULL"))) {
        	selAccount.accountBalance += transaction.transactionAmount; // Add the amount to the account total
        	chargeFee(transaction); // Charge fee based on account type
        } else {
        	System.out.println("Error: Invalid account"); // Error Handling
        }                
    }
    
 // This function handles transfers
    public static void transfer(Transaction transaction){ // Transfer
        Account selAccount = compareAccountNumber(transaction); // Compare each account to the account number given in the transaction
        Transaction secondTransaction = new Transaction(); // Create a second transaction
        secondTransaction = fileHandler.readNextTransactionLine(); // Read in the next transaction
        Account selAccountReceiver = compareAccountNumber(secondTransaction);  // Compare each account to the account number given in the transaction
        
        if ((!(selAccount.accountName.equals("NULL"))) && (!(selAccountReceiver.accountName.equals("NULL")))) {
        	withdraw(transaction); // Perform withdraw on the first account 
        	selAccountReceiver.accountBalance += secondTransaction.transactionAmount; // Add the transaction amount to the second account
        } else {
        	System.out.println("Error: Invalid account"); // Error handling
        }      
    }
    
 // This function handles paying bills
    public static void payBill(Transaction transaction){ // Paybill
        Account selAccount = compareAccountNumber(transaction); // Compare each account to the account number given in the transaction
        if (!(selAccount.accountName.equals("NULL"))) {
        	//selAccount.accountBalance -= transaction.transactionAmount;
        	withdraw(transaction); // Preform withdraw on the account
        	//deposit into company account
        } else {
        	System.out.println("Error: Invalid account"); // Error Handling
        }  
    }
    
    // This function handles changing plans
    public static void changePlan(Transaction transaction){ // Change Plan
        Account selAccount = compareAccountNumber(transaction); // Compare each account to the account number given in the transaction
        if (!(selAccount.accountName.equals("NULL"))) {
	        if (selAccount.studentorNonStudent.equals("N")) { // If the account is non student
	            selAccount.studentorNonStudent = "S"; // Change it to student
	        } else {
	            selAccount.studentorNonStudent = "N"; // Otherwise set it to non-student
	        }
        } else {
        	System.out.println("Error: Invalid account"); // Error Handling
        } 
    }
    
    // This function handles creating accounts
    public static void create(Transaction transaction){ // Create
        Account newAccount = new Account(); // Initialize an account
        
        //newAccount.accountNumber = "00000"; // Set the account number
        
        int accNum = 0;
        
        for (int i = 0; i < fileHandler.accountList.size(); i++){
            for (int j = 0; j < fileHandler.accountList.size(); j++){
                if (accNum == Integer.parseInt(fileHandler.accountList.get(j).accountNumber)){
                    accNum++;
                }
            }
        }        
        
        DecimalFormat formatter = new DecimalFormat("00000"); 
        newAccount.accountNumber = formatter.format(accNum);
        
        newAccount.accountName = transaction.accountName; // Set the account name
        newAccount.enabledOrDisabled = "A"; // Set the account to active
        newAccount.accountBalance = transaction.transactionAmount; // Set the account balance
        newAccount.studentorNonStudent = "N"; // Set the student status
        
        fileHandler.accountList.add(newAccount); // Add the new account to the list
        
    }
    // This function handles deleting accounts 
    public static void delete(Transaction transaction){ // Delete
        Account selAccount = compareAccountNumber(transaction); // Set the account number
        if (!(selAccount.accountName.equals("NULL"))) {
	        for (int i = 0; i < fileHandler.accountList.size() - 1; i++) {
	        	if (selAccount.accountNumber.equals(fileHandler.accountList.get(i).accountNumber)) { // Compare the required account number to each account number in the list
	        		fileHandler.accountList.remove(i); // If they are equal, remove it from the list
	        	}
	        }
	    } else {
	    	System.out.println("Error: Invalid account"); // Error Handling
	    } 
    }
    
    // This function handles enabling accounts
    public static void enable(Transaction transaction){ // Enable
        //S = A or D, (Active or Disabled)
        Account selAccount = compareAccountNumber(transaction);  // Compare each account to the account number given in the transaction
        if (!(selAccount.accountName.equals("NULL"))) {
        	selAccount.enabledOrDisabled = "A"; // Set the account status to Active
        } else {
        	System.out.println("Error: Invalid account"); // Error Handling
        }                       
    }
    // This function handles disabling accounts
    public static void disable(Transaction transaction){
        //S = A or D, (Active or Disabled)
        Account selAccount = compareAccountNumber(transaction); // Compare each account to the account number given in the transaction
        if (!(selAccount.accountName.equals("NULL"))) {
        	selAccount.enabledOrDisabled = "D"; // Set the account status to Disabled
        } else {
        	System.out.println("Error: Invalid account"); // Error Handling
        }        
    }
    
    // Used to compare each account number to the one given by the Transaction object
    public static Account compareAccountNumber(Transaction transactionFile) {  
        Account returnAccount = new Account(); // Initialize an account
        
        for (int i=0; i<fileHandler.accountList.size(); i++) {
            returnAccount = new Account();
            returnAccount = fileHandler.accountList.get(i);
            if (transactionFile.accountNumber.equals(returnAccount.accountNumber)) { // Compare the two account numbers
                return returnAccount; // If they are equal, return the current account, otherwise continue 
            }
        }
        returnAccount.accountName = "NULL";
        return returnAccount;
    }
    
    // Charge the fee based on account type
    // Charge the fee based on account type
    public static void chargeFee(Transaction transaction) { 
        Account selAccount = compareAccountNumber(transaction); // Compare each account to the account number given in the transaction
        
       if (transaction.miscInformation.equals("A ")) { // Admins do not pay transaction fees.
           return;
       } else if (transaction.miscInformation.equals("TV") | transaction.miscInformation.equals("CQ") | transaction.miscInformation.equals("EC")){
           for (int countBack = fileHandler.lineNumber; countBack >= 0; countBack--){
               String line = fileHandler.transactionLines.get(countBack);
               if (line.substring(0, 2).equals("10")){
                   if (line.substring(39, 41).equals("A ")){
                       return;
                   } else {
                       break;
                   }
               }
           }
       }
                
        if (selAccount.studentorNonStudent.equals("N")) { // If the account is non student
            selAccount.accountBalance = (float) (selAccount.accountBalance - 0.1); // Charge them 10 cents
        } else if (selAccount.studentorNonStudent.equals("S")){
            selAccount.accountBalance = (float) (selAccount.accountBalance - 0.05); // Charge them 5 cents
        } 
        
    }
}