package helpers;

import java.util.ArrayList;
import java.util.Collections;

// TODO: Auto-generated Javadoc
/**
 * The Class AccountsHelper.
 */
public class AccountsHelper {
	
	/** The old_accounts_list. */
	public ArrayList<Accounts> old_accounts_list = new ArrayList<Accounts>();
	
	/** The merged_transaction_list. */
	public ArrayList<Transactions> merged_transaction_list = new ArrayList<Transactions>();
	
	/**
	 * Instantiates a new accounts helper.
	 *
	 * @param transactions the transactions
	 * @param accounts the accounts
	 */
	public AccountsHelper(ArrayList<Transactions> transactions, ArrayList<Accounts> accounts) {
		old_accounts_list = new ArrayList<Accounts>(accounts);
		merged_transaction_list = new ArrayList<Transactions>(transactions);
		
		Collections.sort(old_accounts_list, new IdComparator());
	}
	
	/**
	 * Prints the accounts.
	 */
	public void printAccounts() {
		for(Accounts acc : old_accounts_list) {
			System.out.println(acc.name + " ");
		}
	}
	
	/**
	 * Process transactions.
	 */
	public void processTransactions() {
		for(Transactions trans : merged_transaction_list) {
			switch(trans.code + "") {
			/*case "10":
				System.out.println("Login");
			break;*/
			case "1":
				System.out.println("Withdraw");
				withdraw(trans.name, trans.number, trans.amount);
			break;
			case "2":
				System.out.println("Transfer");
			break;
			case "3":
				System.out.println("Paybill");
			break;
			case "4":
				System.out.println("Deposit");
				deposit(trans.name, trans.number, trans.amount);
			break;
			case "5":
				System.out.println("Create");
				create(trans.name, trans.amount);
			break;
			case "6":
				System.out.println("Delete");
				delete(trans.name, trans.number);
			break;
			case "7":
				System.out.println("Disable");
				changeStatus(trans.name, trans.number, 'D');
			break;
			case "8":
				System.out.println("Changeplan");
				changePlan(trans.name, trans.number);
			break;
			case "9":
				System.out.println("Enable");
				changeStatus(trans.name, trans.number, 'E');
			break;
			/*case "0":
				System.out.println("Logout");
			break;*/
			}
		}
	}
	
	/**
	 * Gets the account.
	 *
	 * @param name the name
	 * @param number the number
	 * @return the account
	 */
	public int getAccount(String name, int number) {
		for(Accounts acc : old_accounts_list) {
			if(acc.name.contains(name) && acc.number == number) {
				return old_accounts_list.indexOf(acc);
			}
		}
		return -1;
	}
	
	/**
	 * Change plan.
	 *
	 * @param name the name
	 * @param number the number
	 */
	public void changePlan(String name, int number) {
		int index = getAccount(name, number);
		Accounts acc = old_accounts_list.get(index);
		if(index != -1) {
			acc.is_student = !acc.is_student;
		}
	}
	
	/**
	 * Change status.
	 *
	 * @param name the name
	 * @param number the number
	 * @param status the status
	 */
	public void changeStatus(String name, int number, char status) {
		int index = getAccount(name, number);
		Accounts acc = old_accounts_list.get(index);
		if(index != -1) {
			if(status == 'D' && acc.is_active) acc.is_active = false;
			if(status == 'E' && !acc.is_active) acc.is_active = true;
		}
	}
	
	/**
	 * Deposit.
	 *
	 * @param name the name
	 * @param number the number
	 * @param amount the amount
	 */
	public void deposit(String name, int number, float amount) {
		int index = getAccount(name, number);
		Accounts acc = old_accounts_list.get(index);
		if(index != -1) {
			acc.balance += amount;
		}
	}
	
	public void withdraw(String name, int number, float amount) {
		int index = getAccount(name, number);
		Accounts acc = old_accounts_list.get(index);
		if(index != -1) {
			if(acc.balance - amount < 0) {
				new FileStreamHelper().logError("Not enough balance to withdraw!");
			} else {
				acc.balance -= amount;
			}
		}
	}
	
	/**
	 * Creates the.
	 *
	 * @param name the name
	 * @param amount the amount
	 */
	public void create(String name, float amount) {
		Accounts acc = new Accounts();
		acc.number = (old_accounts_list.get(old_accounts_list.size() - 1)).number + 1;
		acc.name = name;
		acc.is_active = true;
		if(amount < 0) {
			new FileStreamHelper().logError("Not enough balance to create!");
		} else {
			acc.balance = amount;
		}
		old_accounts_list.add(acc);
	}
	
	/**
	 * Delete.
	 *
	 * @param name the name
	 * @param number the number
	 */
	public void delete(String name, int number) {
		int index = getAccount(name, number);
		if(index != -1) {
			old_accounts_list.remove(index);
		}
	}
	
	public ArrayList<Accounts> getAccountList() {
		return old_accounts_list;
		
	}
}
