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
	public final void testInvalidTransaction() {
		testProcessTransactions("Transaction not Found!", 11); // Test for Logout Process
	}

	@Test // successfully get
	public final void testGetAccountStringInt1() {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to get account object given account name and number
		assertEquals(a, ah1.getAccount("Account 1", 1)); // Check if it returns account object
	}

	@Test // Account not found
	public final void testGetAccountStringInt2() {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();

		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		assertEquals(null, ah1.getAccount("Account 1", 2));  // Check if it returns null
	}
	
	@Test // Successfully get
	public final void testGetAccountInt1() {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to get account object given account number
		assertEquals(a, ah1.getAccount(1)); // Check if it returns account object
	}
	
	@Test
	public final void testGetAccountInt2() {
		ArrayList<Transactions> trans = new ArrayList<Transactions>();
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to get account object given account number
		assertEquals(null, ah1.getAccount(2));  // Check if it returns null
	}

	@Test // To student
	public final void testChangePlan1() throws IOException {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the plan of the non-student to student
		ah1.changePlan(a.name, a.number);
		assertEquals(new String("--> Plan for Account 1 is now changed to Student"), outContent.toString().trim()); 
		assertEquals(true, ah1.getAccount(a.number).is_student);  // Check if it the account plan is student
	}
	
	@Test // To non-student
	public final void testChangePlan2() throws IOException {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the plan of the non-student to student
		ah1.changePlan(a.name, a.number);
		assertEquals(new String("--> Plan for Account 1 is now changed to Non-Student"), outContent.toString().trim()); 
		assertEquals(false, ah1.getAccount(a.number).is_student);  // Check if it the account plan is non-student
	}

	@Test // enable
	public final void testChangeStatus1() {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the status of the account
		ah1.changeStatus(a.name, a.number, 'E'); // Enable the account
		assertEquals(new String("--> Account 1's account is now Enabled"), outContent.toString().trim()); 
		assertEquals(true, ah1.getAccount(a.number).is_active);  // Check if it the account plan is Enabled
	}
	
	@Test // Disable
	public final void testChangeStatus2() {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the status of the account
		ah1.changeStatus(a.name, a.number, 'D'); // Disable the account
		assertEquals(new String("--> Account 1's account is now Disabled"), outContent.toString().trim()); 
		assertEquals(false, ah1.getAccount(a.number).is_active);  // Check if it the account is disabled
	}

	@Test // Successful deposit
	public final void testDeposit1() {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test to change the status of the account
		ah1.deposit(a.name, a.number, 100); // Deposit $100 into account
		assertEquals(new String("--> Deposited $100.0 for Account 1. New balance: $99.9"), outContent.toString().trim());
	}
	
	@Test // Not enough balance
	public final void testDeposit2() {
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
		
		AccountsHelper ah1 = new AccountsHelper(trans, accs);
		
		// Test if the negative amount throws error
		ah1.deposit(a.name, a.number, -100); // Deposit negative amount
		assertEquals(new String("Not enough balance to cover fee!"), outContent.toString().trim());
		
		outContent.reset();
	}

	@Test // Successful withdrawal
	public final void testWithdraw1() {
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
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);
		
		ah.withdraw("Account 1", 1, 1.0f);
		// Test to see if the account can be withdrawn
		assertEquals(new String("--> Account 1's account balance after "
				+ "withdrawal of $1.0 is now $98.90"), 
				outContent.toString().trim());
	}
	
	@Test // Not enough balance
	public final void testWithdraw2() {
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
		
	}

	@Test // successful bill payment
	public final void testPaybill1() {
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
		
		ah.paybill("Account 1", 1, 100, "EC");
		
		// Test to make sure there is enough balance to pay the bill
		assertEquals(new String("Not enough balance to pay bill!"), outContent.toString().trim());
	}
	
	@Test // Not enough balance
	public final void testPaybill2() {
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
		ah.paybill("Account 1", 1, 50, "EC");
		
		// Test to make sure bill has been paid
		assertEquals(new String("--> Bill paid to EC by Account 1 of $50.0"), outContent.toString().trim());
	}

	@Test // successful transfer
	public final void testTransfer1() {
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
		
		ah.transfer("Account 1", 1, 50, 2);
		
		// Test to make the sure the transfer has been processed
		assertEquals(new String("--> Transfered $50.0 from Account No.1 to Account No. 2"), outContent.toString().trim());
	}
	
	@Test // not enough balance
	public final void testTransfer2() {
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
	}
	
	@Test // account not found
	public final void testTransfer3() {
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
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);		
		ah.transfer("Account 1", 1, 30, 3);
		
		// Test to make the sure account is valid for transfer
		assertEquals(new String("Account 3 not found. Unable to transfer."), outContent.toString().trim());
	}

	@Test // Successfully create account 2 between 1 and 3
	public final void testCreate1() {
		ArrayList<Accounts> accs1 = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs1.add(a);
		
		Accounts a3 = new Accounts();
		a3.number = 3;
		a3.name = "Account 3";
		a3.is_active = false;
		a3.balance = 0;
		a3.trans_count = 0;
		a3.is_student = false;
		accs1.add(a3);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);
		
		// Add a new account
		ah.create("Account 2", 0);
		
		// Test to make sure the Account is successfully created
		assertEquals(new String("--> New Account created with number 2 with balance of $0.0"), outContent.toString().trim());
		// Test if the newly created Account has the same value
		assertEquals("Account 2", ah.accounts_list.get(1).name);
		assertEquals(0, ah.accounts_list.get(1).balance, 0.001);
		assertEquals(2, ah.accounts_list.get(1).number);
	}
	
	@Test // Attempt to create with negative balance
	public final void testCreate2() {
		ArrayList<Accounts> accs1 = new ArrayList<Accounts>();
		
		Accounts a = new Accounts();
		a.number = 1;
		a.name = "Account 1";
		a.is_active = false;
		a.balance = 0;
		a.trans_count = 0;
		a.is_student = false;
		accs1.add(a);
		
		Accounts a3 = new Accounts();
		a3.number = 3;
		a3.name = "Account 3";
		a3.is_active = false;
		a3.balance = 0;
		a3.trans_count = 0;
		a3.is_student = false;
		accs1.add(a3);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs1);
		
		// Test to add account with negative amount
		ah.create("Account 3", -1);
		
		assertEquals(new String("Not enough balance to create!"), outContent.toString().trim());		
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
		assertEquals(ah1.getAccountList(), accs);
	}
	
	@Test
	public final void testLoopCoverageGetAccountZero() {
		AccountsHelper ah0 = new AccountsHelper(new ArrayList<Transactions>(), new ArrayList<Accounts>());
		
		assertEquals(ah0.getAccount("Account 0", 0), null);
	}
	
	@Test
	public final void testLoopCoverageGetAccountOne() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a1 = new Accounts();
		a1.number = 1;
		a1.name = "Account 1";
		a1.is_active = false;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs.add(a1);
		
		AccountsHelper ah0 = new AccountsHelper(new ArrayList<Transactions>(), accs);
		
		assertEquals(ah0.getAccount("Account 2", 0), null);
	}
	
	@Test
	public final void testLoopCoverageGetAccountTwo() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a1 = new Accounts();
		a1.number = 1;
		a1.name = "Account 1";
		a1.is_active = false;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs.add(a1);

		Accounts a2 = new Accounts();
		a2.number = 1;
		a2.name = "Account 2";
		a2.is_active = false;
		a2.balance = 0;
		a2.trans_count = 0;
		a2.is_student = false;
		accs.add(a2);
		
		AccountsHelper ah0 = new AccountsHelper(new ArrayList<Transactions>(), accs);
		
		assertEquals(ah0.getAccount("Account 3", 0), null);
	}
	
	@Test
	public final void testLoopCoverageGetAccountMultiple() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a1 = new Accounts();
		a1.number = 1;
		a1.name = "Account 1";
		a1.is_active = false;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs.add(a1);

		Accounts a2 = new Accounts();
		a2.number = 2;
		a2.name = "Account 2";
		a2.is_active = false;
		a2.balance = 0;
		a2.trans_count = 0;
		a2.is_student = false;
		accs.add(a2);
		
		Accounts a3 = new Accounts();
		a3.number = 3;
		a3.name = "Account 3";
		a3.is_active = false;
		a3.balance = 0;
		a3.trans_count = 0;
		a3.is_student = false;
		accs.add(a3);
		
		Accounts a4 = new Accounts();
		a4.number = 4;
		a4.name = "Account 4";
		a4.is_active = false;
		a4.balance = 0;
		a4.trans_count = 0;
		a4.is_student = false;
		accs.add(a4);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs);
		
		assertEquals(ah.getAccount("Account 4", 4), a4);
	}
	
	@Test
	public final void testDecisionCoverageOfWithdraw() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a1 = new Accounts();
		a1.number = 1;
		a1.name = "Account 1";
		a1.is_active = false;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs.add(a1);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs);
		ah.is_admin = true;
		
		ah.withdraw("Account 1", 1, 10);
		
		// Test if the account has enough balance for withdraw
		assertEquals(new String("Not enough balance to withdraw!"), outContent.toString().trim());
		
		// Add money to account
		ah.deposit("Account 1", 1, 20);
		
		outContent.reset();
		
		ah.withdraw("Account 1", 1, 10);
		
		// Test if the account is able to withdraw with enough balance
		assertEquals(new String("--> Account 1's account balance after withdrawal of $10.0 is now $10.00"), outContent.toString().trim());
	}
	
	@Test
	public final void testDecisionCoverageOfWithdraw2() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		
		Accounts a1 = new Accounts();
		a1.number = 1;
		a1.name = "Account 1";
		a1.is_active = false;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs.add(a1);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs);
		ah.is_admin = true;
		
		ah.withdraw("Account 1", 1, 10);
		
		ah.deposit("Account 10", 1, 20);
		
		outContent.reset();
		
		ah.withdraw("Account 1", 1, 10);
		
		// Test if the account is able to withdraw with enough balance
		assertEquals(new String("Not enough balance to withdraw!"), outContent.toString().trim());
	}
	
	@Test
	public final void testDecisionCoverageOfWithdrawAccNotFound() {
		ArrayList<Accounts> accs = new ArrayList<Accounts>();
		Accounts a1 = new Accounts();
		a1.number = 1;
		a1.name = "Account 1";
		a1.is_active = false;
		a1.balance = 0;
		a1.trans_count = 0;
		a1.is_student = false;
		accs.add(a1);
		
		AccountsHelper ah = new AccountsHelper(new ArrayList<Transactions>(), accs);
		ah.is_admin = false;
		
		ah.withdraw("Account 1", 1, 10);
		
		ah.deposit("Account 1", 1, 20);
		
		outContent.reset();
		
		ah.withdraw("Account 10", 1, 10);
		
		// Test if the account is able to withdraw with enough balance
		assertNotEquals(new String("--> Account 1's account balance after withdrawal of $10.0 is now $9.80"), outContent.toString().trim());
	}
	
	
}
