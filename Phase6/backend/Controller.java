import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/*  This file is the one which is actually ran, first checking the time, then telling the FileHandler to begin reading in and creating
	lists of every transaction and account based on the supplied .txt files, before it begins to determine the transaction code for each
	transaction in the list, sending each transaction to complete its function in TransactionHandler
*/
public class Controller {
	static FileHandler fileHandler; // Create a FileHandler
	static TransactionHandler transactionHandler; // Create a TransactionHandler
	static Boolean isConditionAlreadyMet = false; //Used to stop multiple iterations of timeChecker from running during midnight hour.
	
	// Begins the program
	public static void main(String[] args) throws IOException, InterruptedException { 
		fileHandler = new FileHandler(); // Initialize the FileHandler
		transactionHandler = new TransactionHandler(); // Initialize the TransactionHandler
		
		fileHandler.accountFile = args[0]; // File where the accounts are stored in text form
		fileHandler.transactionFile = args[1]; // File where the transactions are stored in text form
		fileHandler.masterFile = args[2]; // File where the master accounts are to be printed to
		fileHandler.lineNumber = 0; // Start at line 0
		
		transactionHandler.fileHandler = fileHandler;
		
		fileHandler.initialize(); // Begins the reading in / list creation of the accounts and transactions
		System.out.println("Main Finished.");
		timeChecker(); // Ensures that the system is at midnight
	}
	
	// Only allows the program to be run properly at midnight (hypothetical)
	public static void timeChecker() throws IOException, InterruptedException { 		
		/*Boolean isConditionMet = false; //This will be our condition to break the latch		
		
		while (!isConditionMet) { //Loops through until the condition is met.
			isConditionMet = condition(); //Checks if our condition has been met yet.
			if (isConditionMet) {
				break; //Breaks the loop if our condition is met.
			} else {
				Thread.sleep(60000); //Puts the program to sleep,
									 //as we're only using one thread the whole program is unresponsive for this time.
			}
		}
		*/
		processTransactions(); // Begins the reading of the completed list of transactions
		fileHandler.writeFiles(); // Writes the new accounts to the supplied text file above
		System.out.println("Time Check and Processing Finished.");
		//timeChecker(); //Continues the loop.
		
	}
	
//	This function checks if the current time in EST is midnight and signals for the latch in timeChecker to open or close.
	public static boolean condition() {
		DateFormat dateFormat = new SimpleDateFormat("HH"); //We only care if the hour is midnight.
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("EST")); //This is to ensure the time zone is EST.
		String timeMidnight = "00"; //Our alloted time to execute the code.
		String time = (dateFormat.format(c.getTime())); //Stores the current time.
		
		if (!(c.getTime().equals("00"))) { //if our current time is not midnight it resets the conditional
			isConditionAlreadyMet = false; //this is so the program only runs once every night.
		}
		
		if (timeMidnight.equals(time) && isConditionAlreadyMet == false) { //Checks if our current time is midnight, and
			isConditionAlreadyMet = true;                                  //if the loop has already been ran.
			return true;
		}
		
		return false; //if the conditions aren't met, our latch won't open.
	}
	
	// This function reads the transaction object and routes it to the correct function, depending on it's contents.
	public static void processTransactions() throws InterruptedException{
		Transaction currentTransaction; // The transaction being looked at
		
		while (!(currentTransaction = fileHandler.readNextTransactionLine()).accountName.equals("END")){ // Stops when END OF FILE is reached
			
			//Cases for every transaction code, begins the function for the corresponding transaction type
			switch (currentTransaction.transactionType) {
            case "01":  TransactionHandler.withdraw(currentTransaction); // Withdraw
            		 System.out.println("Withdrawn.");
                     break;
            case "02":  TransactionHandler.transfer(currentTransaction); // Transfer
            		 System.out.println("Transfered.");
                     break;
            case "03":  TransactionHandler.payBill(currentTransaction); // PayBill
   		 			 System.out.println("Bill Paid.");
                     break;
            case "04":  TransactionHandler.deposit(currentTransaction); // Deposit
            		 System.out.println("Deposited.");
                     break;
            case "05":  TransactionHandler.create(currentTransaction); // Create
            		 System.out.println("Account Created.");
                     break;
            case "06":  TransactionHandler.delete(currentTransaction); // Delete
            		 System.out.println("Account Deleted.");
                     break;
            case "07":  TransactionHandler.disable(currentTransaction); // Disable
            		 System.out.println("Account Disabled.");
                     break;
            case "08": TransactionHandler.changePlan(currentTransaction); // ChangePlan
            		 System.out.println("Account Plan Changed.");
                     break;
            case "09": TransactionHandler.enable(currentTransaction); // Enable
            		 System.out.println("Account Enabled.");
                     break;
			}

		}

	}

}
