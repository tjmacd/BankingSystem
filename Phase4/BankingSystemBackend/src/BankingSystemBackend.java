import helpers.AccountsHelper;
import helpers.FileStreamHelper;

/**
 * 
 */

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
		fsh = new FileStreamHelper();
		fsh.setMerged_transaction_file(args[0]);
		fsh.setOld_account_file(args[1]);				
		ah = new AccountsHelper(fsh.readMergedTransFile(), fsh.readOldAccFile());
		ah.processTransactions();
		
		fsh.writeCurrentAccounts(ah.getAccountList());
		fsh.writeMasterAccounts(ah.getAccountList());
	}

}
