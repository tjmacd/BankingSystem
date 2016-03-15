package helpers;

import java.util.ArrayList;

public class AccountsHelper {
	public ArrayList<Accounts> old_accounts_list = new ArrayList<Accounts>();
	public ArrayList<Transactions> merged_transaction_list = new ArrayList<Transactions>();
	
	public AccountsHelper(ArrayList<Transactions> transactions, ArrayList<Accounts> accounts) {
		old_accounts_list = accounts;
		merged_transaction_list = transactions;
	}
	
	public void printAccounts() {
		for(Accounts acc : old_accounts_list) {
			System.out.println(acc.balance + " ");
		}
	}
	
	public void processTransactions() {
		for(Transactions trans : merged_transaction_list) {
			switch(trans.code + "") {
			/*case "10":
				System.out.println("Login");
			break;*/
			case "1":
				System.out.println("Withdraw");
			break;
			case "2":
				System.out.println("Transfer");
			break;
			case "3":
				System.out.println("Paybill");
			break;
			case "4":
				System.out.println("Deposit");
			break;
			case "5":
				System.out.println("Create");
			break;
			case "6":
				System.out.println("Delete");
			break;
			case "7":
				System.out.println("Disable");
			break;
			case "8":
				System.out.println("Changeplan");
			break;
			/*case "0":
				System.out.println("Logout");
			break;*/
			}
		}
	}
}
