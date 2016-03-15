/**
 * 
 */
package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.*;
import java.sql.*;

// TODO: Auto-generated Javadoc
/**
 * The Class FileStreamHelper.
 *
 * @author FlippinFlapJacks
 */
public class FileStreamHelper {
	
	/** The merged_transaction_regex. */
	private Pattern TRANSACTIONS_REGEX = Pattern.compile("^([0-9]{2}) ([a-zA-Z0-9 ]{20}) ([0-9]{5}) ([0-9]{5}.[0-9]{2}) ([a-zA-Z]{1})?");
	
	private Pattern ACCOUNTS_REGEX = Pattern.compile("^([0-9]{5}) ([a-zA-Z0-9 ]{20}) ([A|D]) ([0-9]{5}.[0-9]{2}) ([0-9]{4})");
	
	/** The merged_transaction_file. */
	private String merged_transaction_file = "";
	
	/** The old_account_file. */
	private String old_account_file = "";
	
	/** The log_file. */
	private String log_file = "files/backend.log";
	
	ArrayList<Transactions> merged_transaction_list = new ArrayList<Transactions>();
	ArrayList<Accounts> old_accounts_list = new ArrayList<Accounts>();
	
	/**
	 * Read merged trans file.
	 */
	public void readMergedTransFile() {
		// Initialize BufferedReader class
		BufferedReader br;
		
		// Initialize String 
		String line;
		
		try {
			// Construct BufferedReader class
			br = new BufferedReader(new FileReader(this.merged_transaction_file));
			
			// Iterate through each line in the file
			while((line = br.readLine()) != null) {
				// Run regex match to group each set of transaction properties
				Matcher matches = TRANSACTIONS_REGEX.matcher(line);
				
				// Check if the regex matches
				if(matches.find()) {
					// Construct new Transactions class
					Transactions trans = new Transactions();
					trans.code = Integer.parseInt(matches.group(1)); // Get the code of transaction [Group 1]
					trans.name = matches.group(2); // Get the account name [Group 2]
					trans.number = Integer.parseInt(matches.group(3)); // Get the account number [Group 3]
					trans.amount = Float.parseFloat(matches.group(4)); // Get the account amount [Group 4]
					trans.misc = (char) (matches.group(5) != null ? matches.group(5).charAt(0) : ' '); // Get the misc information [Group 5]
					
					// Add the transactions data to the array list
					merged_transaction_list.add(trans);
				}
			}
		} catch(FileNotFoundException e) {
			logError(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void readOldAccFile() {
		// Initialize BufferedReader class
		BufferedReader br;
		
		// Initialize String 
		String line;
		
		try {
			// Construct BufferedReader class
			br = new BufferedReader(new FileReader(this.old_account_file));
			
			// Iterate through each line in the file
			while((line = br.readLine()) != null) {
				// Run regex match to group each set of transaction properties
				Matcher matches = ACCOUNTS_REGEX.matcher(line);
				
				// Check if the regex matches
				if(matches.find()) {
					Accounts acc = new Accounts();
					acc.number = Integer.parseInt(matches.group(1));
					acc.name = matches.group(2);
					acc.is_active = (matches.group(3).charAt(0) == 'A' ? true : false);
					acc.balance = Float.parseFloat(matches.group(4));
					acc.trans_count = Integer.parseInt(matches.group(5));
					old_accounts_list.add(acc);
				}
			}
		} catch(FileNotFoundException e) {
			logError(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logError(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Log error.
	 *
	 * @param message the message
	 */
	public void logError(String message) {
		// Initialize BufferedWriter class
		BufferedWriter bw = null;
		
		// Construct new Date class
		Date date = new Date();		
		try {
			// Construct the class
			bw = new BufferedWriter(new FileWriter(this.log_file, true));
			
			// Write the message along with the timestamp
			bw.write("[" + new Timestamp(date.getTime()) + "] ERROR: " + message);
			
			// Create new line
			bw.newLine();
			
			// Flush the stream
			bw.flush();
		} catch(Exception e) {
			System.out.print("ERROR: " + e);
		} finally {
			// Check if the class is still constructed
			if(bw != null) try {
				// Close the file
				bw.close();
			} catch(IOException e) {}
		}
	}

	/**
	 * Gets the merged_transaction_file.
	 *
	 * @return the merged_transaction_file
	 */
	public String getMerged_transaction_file() {
		return merged_transaction_file;
	}

	/**
	 * Sets the merged_transaction_file.
	 *
	 * @param merged_transaction_file the new merged_transaction_file
	 */
	public void setMerged_transaction_file(String merged_transaction_file) {
		this.merged_transaction_file = "files/" + merged_transaction_file;
	}

	/**
	 * Gets the old_account_file.
	 *
	 * @return the old_account_file
	 */
	public String getOld_account_file() {
		return old_account_file;
	}

	/**
	 * Sets the old_account_file.
	 *
	 * @param old_account_file the new old_account_file
	 */
	public void setOld_account_file(String old_account_file) {
		this.old_account_file = "files/" + old_account_file;
	}
}
