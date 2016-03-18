package helpers;

import java.util.ArrayList;
import java.util.Collections;

// TODO: Auto-generated Javadoc
/**
 * The Class AccountsHelper.
 */
public class AccountsHelper {
	
	/** The old_accounts_list. */
	public ArrayList<Accounts> accounts_list = new ArrayList<Accounts>();
	
	/** The merged_transaction_list. */
	public ArrayList<Transactions> merged_transaction_list = new ArrayList<Transactions>();
	
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
	 * Prints the accounts.
	 */
	public void printAccounts() {
		for(Accounts acc : accounts_list) {
			System.out.println(acc.name + " ");
		}
	}
	
	/**
	 * Process transactions.
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
	 * Gets the account.
	 *
	 * @param name the name
	 * @param number the number
	 * @return the account
	 */
	public int getAccount(String name, int number) {
		for(Accounts acc : accounts_list) {
			if(acc.name.contains(name) && acc.number == number) {
				return accounts_list.indexOf(acc);
			}
		}
		return -1;
	}
	
	public int getAccount(int number) {
		for(Accounts acc : accounts_list) {
			if(acc.number == number){
				return accounts_list.indexOf(acc);
			}
		}
		return -1;
	}
	
	/**
	 * Changes plan from student to non-student and vice-versa
	 *
	 * @param name the name
	 * @param number the number
	 */
	public void changePlan(String name, int number) {
		int index = getAccount(name, number);
		Accounts acc = accounts_list.get(index);
		if(index != -1) {
			acc.is_student = !acc.is_student;
			System.out.println("--> Plan for " + name + " is now changed to " + (acc.is_student ? "Student" : "Non-Student"));
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
		Accounts acc = accounts_list.get(index);
		if(index != -1) {
			if(status == 'D' && acc.is_active) acc.is_active = false;
			if(status == 'E' && !acc.is_active) acc.is_active = true;
			System.out.println("--> " + name + "'s account is now " + (status == 'E' ? "Enabled" : "Disabled"));
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
		if(index != -1) {
			Accounts acc = accounts_list.get(index);
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
	
	public void withdraw(String name, int number, float amount) {
		int index = getAccount(name, number);
		if(index != -1) {
			Accounts acc = accounts_list.get(index);
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
	
	public void paybill(String name, int number, float amount, String company){
		int index = getAccount(name, number);
		if(index != -1) {
			Accounts acc = accounts_list.get(index);
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
	
	public void transfer(String name, int from_number, float amount, int to_number){
		int index1 = getAccount(name, from_number);
		if(index1 != -1) {
			Accounts acc_from = accounts_list.get(index1);
			float fee = !is_admin ? acc_from.getFee() : 0.0f;
			if(acc_from.balance - amount - fee < 0){
				new FileStreamHelper().logError("Not enough balance to transfer!");
			} else {
				int index2 = getAccount(to_number);
				if(index2 != -1) {
					Accounts acc_to = accounts_list.get(index2);
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
	 * @param name the name
	 * @param amount the amount
	 */
	public void create(String name, float amount) {
		Accounts acc = new Accounts();
		acc.number = (getAccount(name, (accounts_list.get(accounts_list.size() - 1)).number + 1) == -1 ? (accounts_list.get(accounts_list.size() - 1)).number + 1 : null);
		acc.name = name;
		acc.is_active = true;
		if(amount < 0) {
			new FileStreamHelper().logError("Not enough balance to create!");
		} else {
			acc.balance = amount;
			System.out.println("--> New Account created with name " + acc.name + " with balance of $" + acc.balance);
		}
		accounts_list.add(acc);
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
			accounts_list.remove(index);
			System.out.println("--> Account named " + name + " is now deleted!");
		}
	}
	
	public ArrayList<Accounts> getAccountList() {
		return accounts_list;		
	}
}
