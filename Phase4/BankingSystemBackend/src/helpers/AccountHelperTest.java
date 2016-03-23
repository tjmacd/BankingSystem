package helpers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

public class AccountHelperTest {

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

	/*@Test
	public final void testProcessTransactions() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetAccountStringInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetAccountInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testChangePlan() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testChangeStatus() {
		fail("Not yet implemented"); // TODO
	}

	@Test
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
