package helpers;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountHelperTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}

	@Test
	public final void testAccountsHelper() {		
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Transactions t = new Transactions();
		t.code = 1;
		t.name = "Account 1";
		t.number = 1;
		t.amount = 0;
		trans.add(t);
		
		Accounts a = new Accounts();
		a.number = 2;
		a.name = "Account 1";
		a.is_active = true;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = true;
		accs.add(a);
		
		Accounts a2 = new Accounts();
		a2.number = 1;
		a2.name = "Account 2";
		a2.is_active = true;
		a2.balance = 0;
		a2.trans_count = 0;
		a2.is_student = true;
		accs.add(a2);	
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		assertNotEquals(ah1.accounts_list, accs); // Test to make sure the account list is same and its not sorted		
		Collections.sort(accs, new IdComparator()); // Sort the accounts collection
		assertEquals(ah1.accounts_list, accs); // Test to make sure the account list is same and its sorted
		
		assertEquals(ah1.merged_transaction_list, trans); // Test to make sure the transaction list is same
		
	}

	private void testProcessTransactions(String output, int code) {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = true;
		a.balance = 1000;
		a.trans_count = 0;
		a.is_student = true;
		accs.add(a);
		
		Transactions t = new Transactions();
		t.code = code;
		t.name = "Account 1";
		t.number = 1;
		t.amount = 100;
		t.misc = "1";
		trans.add(t);
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);

		ah1.processTransactions();

		assertEquals(new String(output), outContent.toString().trim()); 
	}
	
	@Test
	public final void testLoginTransaction() {
		testProcessTransactions("Login", 10); // Test for Login Process
	}
	
	@Test
	public final void testWithdrawTransaction() {
		testProcessTransactions("Withdraw --> Account 1's account balance after withdrawal of $100.0 is now $899.95", 1); // Test for Withdraw Process
	}
	
	@Test
	public final void testTransferTransaction() {
		testProcessTransactions("Transfer --> Transfered $100.0 from Account No.1 to Account No. 1", 2); // Test for Transfer Process
	}
	
	@Test
	public final void testPaybillTransaction() {
		testProcessTransactions("Paybill --> Bill paid to 1 by Account 1 of $100.0", 3); // Test for Paybill Process
	}
	
	@Test
	public final void testDepositTransaction() {
		testProcessTransactions("Deposit --> Deposited $100.0 for Account 1. New balance: $1099.95", 4); // Test for Deposit Process
	}
	
	@Test
	public final void testCreateTransaction() {
		testProcessTransactions("Create --> New Account created with number 2 with balance of $100.0", 5); // Test for Create Process
	}
	
	@Test
	public final void testDeleteTransaction() {
		testProcessTransactions("Delete --> Account number 1 is now deleted!", 6); // Test for Delete Process
	}
	
	@Test
	public final void testDisableTransaction() {
		testProcessTransactions("Disable --> Account 1's account is now Disabled", 7); // Test for Disable Process
	}
	
	@Test
	public final void testChangeplanTransaction() {
		testProcessTransactions("Changeplan --> Plan for Account 1 is now changed to Non-Student", 8); // Test for Changeplan Process
	}
	
	@Test
	public final void testEnableTransaction() {
		testProcessTransactions("Enable --> Account 1's account is now Enabled", 9); // Test for Enable Process
	}
	
	@Test
	public final void testLogoutTransaction() {
		testProcessTransactions("Logout", 0); // Test for Logout Process
	}

	@Test
	public final void testGetAccountStringInt() {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = true;
		a.balance = 1000;
		a.trans_count = 0;
		a.is_student = true;
		accs.add(a);
		
		Transactions t = new Transactions();
		t.code = 1;
		t.name = "Account 1";
		t.number = 1;
		t.amount = 100;
		t.misc = "1";
		trans.add(t);
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to get account object given account name and number
		assertEquals(a, ah1.getAccount("Account 1", 1)); // Check if it returns account object
		assertEquals(null, ah1.getAccount("Account 1", 2));  // Check if it returns null
	}

	@Test
	public final void testGetAccountInt() {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = true;
		a.balance = 1000;
		a.trans_count = 0;
		a.is_student = true;
		accs.add(a);
		
		Transactions t = new Transactions();
		t.code = 1;
		t.name = "Account 1";
		t.number = 1;
		t.amount = 100;
		t.misc = "1";
		trans.add(t);
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to get account object given account number
		assertEquals(a, ah1.getAccount(1)); // Check if it returns account object
		assertEquals(null, ah1.getAccount(2));  // Check if it returns null
	}

	@Test
	public final void testChangePlan() throws IOException {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = true;
		a.balance = 1000;
		a.trans_count = 0;
		a.is_student = false;
		accs.add(a);
		
		Transactions t = new Transactions();
		t.code = 8;
		t.name = "Account 1";
		t.number = 1;
		t.amount = 100;
		t.misc = "1";
		trans.add(t);
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the plan of the non-student to student
		assertNotEquals(true, ah1.getAccount(a.number).is_student); // Check if the account plan is non-student
		ah1.changePlan(a.name, a.number);
		assertEquals(new String("--> Plan for Account 1 is now changed to Student"), outContent.toString().trim()); 
		assertEquals(true, ah1.getAccount(a.number).is_student);  // Check if it the account plan is student
		outContent.reset();
		ah1.changePlan(a.name, a.number);
		assertEquals(new String("--> Plan for Account 1 is now changed to Non-Student"), outContent.toString().trim()); 
		assertEquals(false, ah1.getAccount(a.number).is_student);  // Check if it the account plan is non-student
	}

	@Test
	public final void testChangeStatus() {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 1000;
		a.trans_count = 0;
		a.is_student = false;
		accs.add(a);
		
		Transactions t = new Transactions();
		t.code = 8;
		t.name = "Account 1";
		t.number = 1;
		t.amount = 100;
		t.misc = "1";
		trans.add(t);
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the status of the account
		assertNotEquals(true, ah1.getAccount(a.number).is_active); // Check if the account plan is not enabled
		ah1.changeStatus(a.name, a.number, 'E'); // Enable the account
		assertEquals(new String("--> Account 1's account is now Enabled"), outContent.toString().trim()); 
		assertEquals(true, ah1.getAccount(a.number).is_active);  // Check if it the account plan is Enabled
		outContent.reset();
		ah1.changeStatus(a.name, a.number, 'D'); // Disable the account
		assertEquals(new String("--> Account 1's account is now Disabled"), outContent.toString().trim()); 
		assertEquals(false, ah1.getAccount(a.number).is_active);  // Check if it the account is disabled
	}

	@Test
	public final void testDeposit() {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs.add(a);
		
		Accounts a2 = new Accounts();
		a2.number = 2;
		a2.name = "Account 2";
		a2.is_active = false;
		a2.balance = 1000;
		a2.trans_count = 0;
		a2.is_student = false;
		accs.add(a2);
		
		Transactions t = new Transactions();
		t.code = 4;
		t.name = "Account 1";
		t.number = 1;
		t.amount = 100;
		t.misc = "1";
		trans.add(t);
		
		Transactions t2 = new Transactions();
		t2.code = 4;
		t2.name = "Account 1";
		t2.number = 1;
		t2.amount = -100;
		t2.misc = "1";
		trans.add(t2);
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the status of the account
		ah1.deposit(t.name, t.number, t.amount); // Deposit $100 into account
		assertEquals(new String("--> Deposited $100.0 for Account 1. New balance: $99.9"), outContent.toString().trim());
		outContent.reset(); // Reset the console
		ah1.deposit(t2.name, t2.number, t2.amount); // Deposit negative amount
		assertEquals(new String("Not enough balance to cover fee!"), outContent.toString().trim());
		
		outContent.reset();
	}

	@Test
	public final void testWithdraw() {
		outContent.reset(); // Reset the console
		ArrayList<Accounts> accs1 = new ArrayList<Accounts>();
		accs1.clear();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs1.add(a);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);
		
		ah.withdraw("Account 1", 1, 100);
		// Test to see if the account can be withdrawn with 0 balance
		assertEquals(new String("Not enough balance to withdraw!"), outContent.toString().trim());
		
		// Add balance for more tests
		ah.deposit(a.name, a.number, 100);
		
		outContent.reset(); // Reset the console
		
		ah.withdraw("Account 1", 1, 100);
		// Test to see if the account can be withdrawn with 0 balance
		assertEquals(new String("Not enough balance to withdraw!"), outContent.toString().trim());
		
		// Add balance for more tests
		ah.deposit(a.name, a.number, 100);
				
		outContent.reset(); // Reset the console
		
		ah.withdraw("Account 1", 1, 100);
		// Test to see if the account can be withdrawn with 0 balance
		assertEquals(new String("--> Account 1's account balance after withdrawal of $100.0 is now $99.70"), outContent.toString().trim());
	}

	@Test
	public final void testPaybill() {
		ArrayList<Accounts> accs1 = new ArrayList<Accounts>();
		accs1.clear();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 100;
		a.trans_count = 0;
		a.is_student = false;
		accs1.add(a);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);
		
		outContent.reset(); // Reset the console
		
		ah.paybill("Account 1", 1, 100, "EC");
		
		// Test to make sure there is enough balance to pay the bill
		assertEquals(new String("Not enough balance to pay bill!"), outContent.toString().trim());
		
		outContent.reset(); // Reset the console
		
		ah.paybill("Account 1", 1, 50, "EC");
		
		// Test to make sure bill has been paid
		assertEquals(new String("--> Bill paid to EC by Account 1 of $50.0"), outContent.toString().trim());
	}

	@Test
	public final void testTransfer() {
		outContent.reset(); // Reset the console
		ArrayList<Accounts> accs1 = new ArrayList<Accounts>();
		accs1.clear();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 100;
		a.trans_count = 0;
		a.is_student = false;
		accs1.add(a);
		
		Accounts a1 = new Accounts();
		a1.number = 2;
		a1.name = "Account 2";
		a1.is_active = false;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs1.add(a1);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);
		
		ah.transfer("Account 1", 1, 100, 2);
		
		// Test to make sure that account does not have enough balance
		assertEquals(new String("Not enough balance to transfer!"), outContent.toString().trim());
		
		outContent.reset(); // Reset the console
		
		ah.transfer("Account 1", 1, 50, 2);
		
		// Test to make the sure the transfer has been processed
		assertEquals(new String("--> Transfered $50.0 from Account No.1 to Account No. 2"), outContent.toString().trim());
		
		outContent.reset(); // Reset the console
		
		ah.transfer("Account 1", 1, 30, 3);
		
		// Test to make the sure account is valid for transfer
		assertEquals(new String("Account 3 not found. Unable to transfer."), outContent.toString().trim());
	}

	@Test
	public final void testCreate() {
		outContent.reset(); // Reset the console
		ArrayList<Accounts> accs1 = new ArrayList<Accounts>();
		accs1.clear();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs1.add(a);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);
		
		// Check if there is one account
		assertEquals(ah.getAccountList(), accs1);	
		
		// Add a new account
		ah.create("Account 2", 0);
		
		// Test to make sure the Account is successfully created
		assertEquals(new String("--> New Account created with number 2 with balance of $0.0"), outContent.toString().trim());
		
		// Test to add account with negative amount
		outContent.reset(); // Reset the console
		ah.create("Account 3", -1);
		
		assertEquals(new String("Not enough balance to create!"), outContent.toString().trim());
		
		// Test to make sure the objects are same after new account
		
		Accounts a1 = new Accounts();
		a1.number = 2;
		a1.name = "Account 2";
		a1.is_active = true;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs1.add(a1);
		
		Collections.sort(accs1, new IdComparator()); // Sort the accounts collection
		
		// Test if the newly created Account has the same value
		assertEquals(ah.getAccountList().get(1).name, accs1.get(1).name);
		assertEquals(ah.getAccountList().get(1).balance, accs1.get(1).balance, 0.001);
	}

	@Test
	public final void testDelete() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs.add(a);
		
		Accounts a2 = new Accounts();
		a2.number = 2;
		a2.name = "Account 2";
		a2.is_active = false;
		a2.balance = 1000;
		a2.trans_count = 0;
		a2.is_student = false;
		accs.add(a2);
		
		AccountsHelper ah1 = new AccountsHelper(new ArrayList<Transactions>(), accs);
		
		// Reset the console window
		outContent.reset();
		
		// Delete first account
		ah1.delete(a.name, a.number);
		assertEquals(new String("--> Account number 1 is now deleted!"), outContent.toString().trim());
		
		outContent.reset();
		
		// Delete first account again, which does not exist
		ah1.delete(a.name, a.number);
		assertNotEquals(new String("--> Account number 1 is now deleted!"), outContent.toString().trim());
		
		outContent.reset();
		
		// Delete second account
		ah1.delete(a2.name, a2.number);
		assertEquals(new String("--> Account number 2 is now deleted!"), outContent.toString().trim()); 		
	}

	@Test
	public final void testGetAccountList() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs.add(a);
		
		// Test to see if getAccountList returns the same object
		AccountsHelper ah1 = new AccountsHelper(new ArrayList<Transactions>(), accs);
		// Test to see if the accs list is the same as the list returned from AccountHelper
		assertEquals(accs, ah1.getAccountList());
		
		Accounts a1 = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs.add(a1);
		
		// test to see if the newly added account is the same in AccountsHelper
		assertNotEquals(accs, ah1.getAccountList());
	}

}
