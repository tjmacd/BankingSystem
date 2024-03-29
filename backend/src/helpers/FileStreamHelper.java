package helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.*;
import java.sql.*;

/**
 * This class handles all interactions with external files
 *
 */
public class FileStreamHelper {
	
	// Transaction file pattern
	private Pattern TRANSACTIONS_REGEX = Pattern.compile(
			"^([0-9]{2}) ([a-zA-Z0-9 ]{20}) ([0-9]{5}) ([0-9]{5}.[0-9]{2}) "
			+ "([a-zA-Z]{1,2})?");
	// Accounts file pattern
	private Pattern ACCOUNTS_REGEX = Pattern.compile(
			"^([0-9]{5}) ([a-zA-Z0-9 ]{20}) ([A|D]) ([0-9]{5}.[0-9]{2}) "
			+ "([0-9]{4}) (S|N)");
	// Filename of merged transaction file
	private String merged_transaction_file = "";
	// Filename of accounts file
	private String old_account_file = "";
	// File name of error log
	private String log_file = "backend.log";
	// Filename of master bank accounts file
	private String new_master_accounts_file = 
			"masterBankAccountsFile.txt";
	// Filename of new current accounts file to be written
    private String new_current_accounts_file = "currentAccounts.txt";
    // List of transactions
	ArrayList<Transactions> merged_transaction_list = 
			new ArrayList<Transactions>();
	ArrayList<Transactions> to_be_merged = 
			new ArrayList<Transactions>();
	// List of accounts
	ArrayList<Accounts> old_accounts_list = new ArrayList<Accounts>();
	
	/**
	 * Read merged trans file.
	 */
	@SuppressWarnings({ "finally" })
	public ArrayList<Transactions> readMergedTransFile() {
		try (BufferedReader br = new BufferedReader(
				new FileReader(this.merged_transaction_file))){
			// Initialize String 
			String line;
			
			// Iterate through each line in the file
			while((line = br.readLine()) != null) {
				// Run regex match to group each set of transaction properties
				Matcher matches = TRANSACTIONS_REGEX.matcher(line);
				
				// Check if the regex matches
				if(matches.find()) {
					// Construct new Transactions class
					Transactions trans = new Transactions();
					// Get the code of transaction [Group 1]
					trans.code = Integer.parseInt(matches.group(1)); 
					// Get the account name [Group 2]
					trans.name = matches.group(2).trim();
					// Get the account number [Group 3]
					trans.number = Integer.parseInt(matches.group(3));
					// Get the account amount [Group 4]
					trans.amount = Float.parseFloat(matches.group(4)); 
					 
					if(trans.code == 2){
						// For transfer transaction, store recipient account in misc
						line = br.readLine();
						matches = TRANSACTIONS_REGEX.matcher(line);
						if(matches.find() && matches.group(1).equals("02")){
							trans.misc = matches.group(3);
						}
					} else {
						// Get the misc information [Group 5]
						trans.misc = matches.group(5) != null ? matches.group(5).trim() : "";
					}
					
					// Add the transactions data to the array list
					merged_transaction_list.add(trans);
				}
			}
		} catch (IOException e) {
			logError(e.getMessage());
			e.printStackTrace();
		} finally {
			return merged_transaction_list;
		}
	}
	
	@SuppressWarnings({ "finally" })
	public ArrayList<Accounts> readOldAccFile() {
		try (BufferedReader br = new BufferedReader(
				new FileReader(this.old_account_file))){
			// Initialize String 
			String line;
			// Iterate through each line in the file
			while((line = br.readLine()) != null) {
				// Run regex match to group each set of transaction properties
				Matcher matches = ACCOUNTS_REGEX.matcher(line);
				
				// Check if the regex matches
				if(matches.find()) {
					Accounts acc = new Accounts();
					acc.number = Integer.parseInt(matches.group(1));
					acc.name = matches.group(2).trim();
					acc.is_active = (matches.group(3).charAt(0) == 'A' 
							? true : false);
					acc.balance = Float.parseFloat(matches.group(4));
					acc.trans_count = Integer.parseInt(matches.group(5));
					acc.is_student = (matches.group(6).charAt(0) == 'S' 
							? true : false);
					old_accounts_list.add(acc);
				}
			}
		} catch (IOException e) {
			logError(e.getMessage());
			e.printStackTrace();
		} finally {
			return old_accounts_list;
		}
	}
	
	public void writeCurrentAccounts(List<Accounts> accounts){
        try(PrintStream current_accounts = 
                new PrintStream(this.new_current_accounts_file)){
            for(Accounts account : accounts){
            	String format = "%05d %-20s %s %08.2f %s\n";
                current_accounts.printf(format, account.number, account.name, 
                						account.is_active ? "A" : "D", 
                						account.balance,
                						account.is_student ? "S" : "N");
            }
            current_accounts.close();
        } catch(Exception e) {
			System.err.print("ERROR: " + e);
		} 
    }
	
	public void writeMasterAccounts(List<Accounts> accounts){
		try(PrintStream master_accounts = 
                new PrintStream(this.new_master_accounts_file)){
            for(Accounts account : accounts){
            	String format = "%05d %-20s %s %08.2f %04d %s\n";
                master_accounts.printf(format, account.number, account.name, 
                						account.is_active ? "A" : "D", 
                						account.balance, account.trans_count,
                						account.is_student ? "S" : "N");
            }
            master_accounts.close();
        } catch(Exception e) {
			System.err.print("ERROR: " + e);
		} 
	}
	
	/**
	 * Log error.
	 *
	 * @param message the message
	 */
	public void logError(String message) {
		// Construct new Date class
		Date date = new Date();		
		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter(this.log_file, true))){
			// Write the message along with the timestamp
			bw.write("[" + new Timestamp(date.getTime()) + "] ERROR: " + message);
			// Create new line
			bw.newLine();
			// Flush the stream
			bw.flush();
		} catch(Exception e) {
			System.err.print("ERROR: " + e);
		}
	}

	/**
	 * Gets the merged_transaction_file.
	 *
	 * @return the merged_transaction_file
	 */
	public String getMergedTransactionFile() {
		return merged_transaction_file;
	}

	/**
	 * Sets the merged_transaction_file.
	 *
	 * @param merged_transaction_file the new merged_transaction_file
	 */
	public void setMergedTransactionFile(String merged_transaction_file) {
		this.merged_transaction_file = merged_transaction_file;
	}

	/**
	 * Gets the old_account_file.
	 *
	 * @return the old_account_file
	 */
	public String getOldAccountFile() {
		return old_account_file;
	}

	/**
	 * Sets the old_account_file.
	 *
	 * @param old_account_file the new old_account_file
	 */
	public void setOldAccountFile(String old_account_file) {
		this.old_account_file = old_account_file;
	}
}
