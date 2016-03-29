import helpers.AccountsHelper;
import helpers.FileStreamHelper;

/**
 * @author shankz
 *
 */
public class BankingSystemBackend {
	
	private static FileStreamHelper fsh;
	private static AccountsHelper ah;

	/**Accounts
	 * @param args
	 */
	public static void main(String[] args) {
		// Construct new FileStreamHelper() class
		fsh = new FileStreamHelper();
		
		// Take in the first argument of mergedTransactionFile
		fsh.setMergedTransactionFile(args[0]);
		
		// Take in the second argument containing account file name
		fsh.setOldAccountFile(args[1]);				
		
		// Construct AccountsHelper class with 2 parameters
		// First parameter: ArrayList<Transactions> merged transactions file
		// Second parameter: ArrayList<Accounts> old accounts file
		ah = new AccountsHelper(fsh.readMergedTransFile(), fsh.readOldAccFile());
		
		// Process all the transactions
		ah.processTransactions();
		
		// Write current account after transactions
		fsh.writeCurrentAccounts(ah.getAccountList());
		
		// write master accounts 
		fsh.writeMasterAccounts(ah.getAccountList());
	}

}
