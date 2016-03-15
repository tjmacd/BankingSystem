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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		fsh = new FileStreamHelper();
		fsh.setMerged_transaction_file(args[0]);
		fsh.setOld_account_file(args[1]);
		fsh.readMergedTransFile();		
		fsh.readOldAccFile();
		
		
	}

}
