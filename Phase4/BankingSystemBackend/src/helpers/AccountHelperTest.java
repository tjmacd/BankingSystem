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
		testProcessTransactions("Transfer", 2); // Test for Transfer Process
	}
	
	@Test
	public final void testPaybillTransaction() {
		testProcessTransactions("Paybill", 3); // Test for Paybill Process
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
		
		// Test to change the plan of the non-student to student
		assertNotEquals(true, ah1.getAccount(a.number).is_active); // Check if the account plan is non-student
		ah1.changeStatus(a.name, a.number, 'E');
		assertEquals(new String("--> Account 1's account is now Enabled"), outContent.toString().trim()); 
		assertEquals(true, ah1.getAccount(a.number).is_active);  // Check if it the account plan is student
		outContent.reset();
		ah1.changeStatus(a.name, a.number, 'D');
		assertEquals(new String("--> Account 1's account is now Disabled"), outContent.toString().trim()); 
		assertEquals(false, ah1.getAccount(a.number).is_active);  // Check if it the account plan is non-student
	}

	/*@Test
	public final void testDeposit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWithdraw() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testPaybill() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testTransfer() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testCreate() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testDelete() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetAccountList() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testObject() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetClass() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testHashCode() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testEquals() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testClone() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNotify() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNotifyAll() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWaitLong() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWaitLongInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWait() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFinalize() {
		fail("Not yet implemented"); // TODO
	}*/

}
