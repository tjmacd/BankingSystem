package helpers;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileStreamHelperTest {
	
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

	@Test // Case 1: transaction code 2 - execute lines 73-77
	public void testReadMergedTransFile1() {
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.setMergedTransactionFile("transferTransaction.txt");
		ArrayList<Transactions> list = fsh.readMergedTransFile();
		assertEquals(1, list.size());
		
		assertEquals("00002", list.get(0).misc);
	}

	@Test // Case 2: transaction code not 2 - execute line 80
	public void testReadMergedTransFile2() {
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.setMergedTransactionFile("paybillTransaction.txt");
		ArrayList<Transactions> list = fsh.readMergedTransFile();
		assertEquals(1, list.size());
		
		assertEquals("TV", list.get(0).misc);
	}
	
	@Test // Case 3: file not found - line 87-89
	public void testReadMergedTransFile3() {
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.setMergedTransactionFile("nofile");
		ArrayList<Transactions> list = fsh.readMergedTransFile();
		
		String exception = errContent.toString().split(" ")[0];
		assertEquals("java.io.FileNotFoundException:", exception);
	}
	
	@Test // Case 4: io exception - lines 90-92
	public void testReadMergedTransFile4() {
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.setMergedTransactionFile("../bin/BankingSystemBackend.class");
		ArrayList<Transactions> list = fsh.readMergedTransFile();
		
		String exception = errContent.toString().split(" ")[0];
		
		assertEquals("java.io.IOException:", exception);
	}
	
	@Test // Case 1: 100-122 successful try block
	public void testReadOldAccFile1() {
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.setOldAccountFile("masterOneAccount.txt");
		ArrayList<Accounts> list = fsh.readOldAccFile();
		assertEquals(1, list.size());
		Accounts account = list.get(0);
		assertEquals(1, account.number);
		assertEquals("Name", account.name);
		assertTrue(account.is_active);
		assertFalse(account.is_student);
		assertEquals(1.0, account.balance, 0.001);
		assertEquals(1, account.trans_count);
	}
	
	@Test // Case 2: FileNotFoundException
	public void testReadOldAccFile2() {
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.setOldAccountFile("nofile");
		ArrayList<Accounts> list = fsh.readOldAccFile();
		String exception = errContent.toString().split(" ")[0];
		assertEquals("java.io.FileNotFoundException:", exception);
	}

	@Test // Case 3: IOException
	public void testReadOldAccFile3() {
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.setOldAccountFile("");
		ArrayList<Accounts> list = fsh.readOldAccFile();
		String exception = errContent.toString().split(" ")[0];
		assertEquals("java.io.IOException:", exception);
	}
	
	@Test
	public void testWriteCurrentAccounts() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteMasterAccounts() {
		fail("Not yet implemented");
	}

	@Test
	public void testLogError() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMerged_transaction_file() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMerged_transaction_file() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetOld_account_file() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetOld_account_file() {
		fail("Not yet implemented");
	}

}
