package helpers;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Handles all interactions with accounts database
 */
public class AccountsHelper {
	
	// A list of all the accounts
	public ArrayList<Accounts> accounts_list = new ArrayList<Accounts>();
	// List of all transactions
	public ArrayList<Transactions> merged_transaction_list = new ArrayList<Transactions>();
	// Tracks whether session is admin
	public boolean is_admin;
	
	/**
	 * Instantiates a new accounts helper.
	 *
	 * @param transactions the transactions
	 * @param accounts the accounts
	 */
	public AccountsHelper(ArrayList<Transactions> transactions, ArrayList<Accounts> accounts) {
		accounts_list = new ArrayList<Accounts>(accounts);
		merged_transaction_list = new ArrayList<Transactions>(transactions);
		
		Collections.sort(accounts_list, new IdComparator());
	}
	
	/**
	 * Processs transactions in transaction list
	 */
	public void processTransactions() {
		for(Transactions trans : merged_transaction_list) {
			switch(trans.code + "") {
			case "10":
				System.out.println("Login");
				is_admin = (trans.misc.equals("A"));
			break;
			case "1":
				System.out.print("Withdraw ");
				withdraw(trans.name, trans.number, trans.amount);
			break;
			case "2":
				System.out.print("Transfer ");
				transfer(trans.name, trans.number, trans.amount, 
						Integer.parseInt(trans.misc));
			break;
			case "3":
				System.out.print("Paybill ");
				paybill(trans.name, trans.number, trans.amount, trans.misc);
			break;
			case "4":
				System.out.print("Deposit ");
				deposit(trans.name, trans.number, trans.amount);
			break;
			case "5":
				System.out.print("Create ");
				create(trans.name, trans.amount);
			break;
			case "6":
				System.out.print("Delete ");
				delete(trans.name, trans.number);
			break;
			case "7":
				System.out.print("Disable ");
				changeStatus(trans.name, trans.number, 'D');
			break;
			case "8":
				System.out.print("Changeplan ");
				changePlan(trans.name, trans.number);
			break;
			case "9":
				System.out.print("Enable ");
				changeStatus(trans.name, trans.number, 'E');
			break;
			case "0":
				System.out.println("Logout");
				is_admin = false;
			break;
			}
		}
	}
	
	/**
	 * Gets the index of an account given name and number
	 *
	 * @param name   the name of the account holder
	 * @param number the account number
	 * @return       the account index
	 */
	public Accounts getAccount(String name, int number){
		for(Accounts acc : accounts_list) {
			if(acc.name.equals(name) && acc.number == number){
				return acc;
			}
		}
		return null;
	}
	
	/**
	 * Gets the index of an account given a number
	 * @param number account number
	 * @return       index of the account
	 */
	public Accounts getAccount(int number) {
		for(Accounts acc : accounts_list) {
			if(acc.number == number){
				return acc;
			}
		}
		return null;
	}
	
	/**
	 * Changes plan from student to non-student and vice-versa
	 *
	 * @param name   the name of the account holder
	 * @param number the account number
	 */
	public void changePlan(String name, int number) {
		Accounts acc = getAccount(name, number);
		// = accounts_list.get(index);
		if(acc != null) {
			acc.is_student = !acc.is_student;
			System.out.println("--> Plan for " + name + " is now changed to " + (acc.is_student ? "Student" : "Non-Student"));
		}
	}
	
	/**
	 * Changes the status of the account
	 *
	 * @param name   the name of the account holder
	 * @param number the account number
	 * @param status the new status
	 */
	public void changeStatus(String name, int number, char status) {
		Accounts acc = getAccount(name, number);
		if(acc != null) {
			if(status == 'D' && acc.is_active) acc.is_active = false;
			if(status == 'E' && !acc.is_active) acc.is_active = true;
			System.out.println("--> " + name + "'s account is now " + (status == 'E' ? "Enabled" : "Disabled"));
		}
	}
	
	/**
	 * Deposits money into the account
	 *
	 * @param name   the name of the account holder
	 * @param number the account number
	 * @param amount the amount to deposit
	 */
	public void deposit(String name, int number, float amount) {
		Accounts acc = getAccount(name, number);
		if(acc != null) {
			float fee = !is_admin ? acc.getFee() : 0.0f;
			float amount_change = amount - fee;
			if(acc.balance + amount_change < 0){
				new FileStreamHelper().logError("Not enough balance to cover fee!");
			} else {
				acc.balance += amount_change;
				if(!is_admin) acc.trans_count++;
				System.out.println("--> Deposited $" + amount + " for " + name + ". New balance: $" + acc.balance);
			}
		}
	}
	
	/**
	 * Withdraws money from an account
	 * 
	 * @param name    name of account holder
	 * @param number  account number
	 * @param amount  amount to withdraw
	 */
	public void withdraw(String name, int number, float amount) {
		Accounts acc = getAccount(name, number);
		if(acc != null) {
			float fee = !is_admin ? acc.getFee() : 0.0f;
			float amount_change = amount + fee;
			if(acc.balance - amount_change < 0) {
				new FileStreamHelper().logError("Not enough balance to withdraw!");
			} else {
				acc.balance -= amount_change;
				if(!is_admin) acc.trans_count++;
				System.out.println("--> " + name + "'s account balance after withdrawal of $" + amount + " is now $" + acc.balance);
			}
		}
	}
	
	/**
	 * Pays bill to a company
	 * 
	 * @param name    name of the account holder
	 * @param number  account number
	 * @param amount  amount to pay
	 * @param company receiving company
	 */
	public void paybill(String name, int number, float amount, String company){
		Accounts acc = getAccount(name, number);
		if(acc != null) {
			float fee = !is_admin ? acc.getFee() : 0.0f;
			float amount_change = amount + fee;
			if(acc.balance - amount_change < 0){
				new FileStreamHelper().logError("Not enough balance to pay bill!");
			} else {
				acc.balance -= amount_change;
				if(!is_admin) acc.trans_count++;
			}
		}
	}
	
	/**
	 * Transfers money from one account to another
	 * 
	 * @param name         name of the account holder
	 * @param from_number  account number to transfer from
	 * @param amount       amount to transfer
	 * @param to_number    account number to transfer to
	 */
	public void transfer(String name, int from_number, float amount, int to_number){
		Accounts acc_from = getAccount(name, from_number);
		if(acc_from != null) {
			float fee = !is_admin ? acc_from.getFee() : 0.0f;
			if(acc_from.balance - amount - fee < 0){
				new FileStreamHelper().logError("Not enough balance to transfer!");
			} else {
				Accounts acc_to = getAccount(to_number);
				if(acc_to != null) {
					acc_from.balance -= (amount + fee);
					acc_to.balance += amount;
					if(!is_admin) acc_from.trans_count++;
				} else {
					new FileStreamHelper().logError("Account " + to_number + 
							" not found. Unable to transfer.");
				}
			}
		}
	}
	
	/**
	 * Creates an account
	 *
	 * @param name   the name
	 * @param amount the amount
	 */
	public void create(String name, float amount) {
		Accounts new_account = new Accounts();
		int i = 1;
		int new_number = 0;
		for(Accounts acc : accounts_list){
			if(i == acc.number){
				i++;
			} else {
				new_number = i;
				break;
			}
		}
		if(new_number == 0){
			new_number = accounts_list.size() + 1;
		}
		new_account.number = new_number;
		new_account.name = name;
		new_account.is_active = true;
		if(amount < 0) {
			new FileStreamHelper().logError("Not enough balance to create!");
		} else {
			new_account.balance = amount;
			accounts_list.add(new_account);
			Collections.sort(accounts_list, new IdComparator());
			System.out.println("--> New Account created with number " + 
			new_account.number + " with balance of $" + new_account.balance);
		}
	}
	
	/**
	 * Deletes an account
	 *
	 * @param name   the name
	 * @param number the number
	 */
	public void delete(String name, int number) {
		Accounts account = getAccount(name, number);
		if(account != null) {
			accounts_list.remove(account);
			System.out.println("--> Account number " + number + " is now deleted!");
		}
	}
	
	/**
	 * Gets the account list
	 * @return account list
	 */
	public ArrayList<Accounts> getAccountList() {
		return accounts_list;		
	}
}
