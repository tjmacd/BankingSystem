package helpers;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
	
	@Test // Case 1: successful write
	public void testWriteCurrentAccounts1() throws IOException {
		ArrayList<Accounts> accounts = new ArrayList<Accounts>();
		Accounts account = new Accounts();
		account.balance = 1.0f;
		account.name = "Name";
		account.is_active = true;
		account.is_student = true;
		account.number = 1;
		account.trans_count = 1;
		accounts.add(account);
		FileStreamHelper fsh = new FileStreamHelper();
		
		fsh.writeCurrentAccounts(accounts);
		BufferedReader reader = new BufferedReader(new FileReader("files/currentAccounts.txt"));
		String line = reader.readLine();
		reader.close();
		assertEquals("00001 Name                 A 00001.00 S", line);
	}
	
	@Test // Case 2: Exception TODO
	public void testWriteCurrentAccounts2() {
		fail("Not yet implemented");
	}

	@Test // Case 1: successful write
	public void testWriteMasterAccounts1() throws IOException {
		ArrayList<Accounts> accounts = new ArrayList<Accounts>();
		Accounts account = new Accounts();
		account.balance = 1.0f;
		account.name = "Name";
		account.is_active = true;
		account.is_student = true;
		account.number = 1;
		account.trans_count = 1;
		accounts.add(account);
		FileStreamHelper fsh = new FileStreamHelper();
		
		fsh.writeMasterAccounts(accounts);
		BufferedReader reader = new BufferedReader(new FileReader("files/masterBankAccountsFile.txt"));
		String line = reader.readLine();
		reader.close();
		assertEquals("00001 Name                 A 00001.00 0001 S", line);
	}
	
	@Test // Case 2: Exception TODO
	public void testWriteMasterAccounts2() {
		fail("Not yet implemented");
	}

	@Test // Case 1: successfully log error
	public void testLogError1() throws IOException {
		String message = "This is an error";
		String filename = "files/backend.log";
		PrintStream log_file = new PrintStream(filename);
		log_file.close();
		FileStreamHelper fsh = new FileStreamHelper();
		fsh.logError(message);
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine().substring(26);
		reader.close();
		assertEquals("ERROR: " + message, line);
	}
	
	@Test // Case 2: exception TODO
	public void testLogError2() {
		fail("Not yet implemented");
	}

	@Test // Test set then get transaction file
	public void testSetGetMergedTransactionFile() {
		FileStreamHelper fsh = new FileStreamHelper();
		String filename = "mergedTransactionFile";
		fsh.setMergedTransactionFile(filename);
		String actual_filename = fsh.getMergedTransactionFile();
		assertEquals("files/" + filename, actual_filename);
	}

	@Test // Test set and get Old account file
	public void testSetGetOldAccountFile() {
		FileStreamHelper fsh = new FileStreamHelper();
		String filename = "accountFile";
		fsh.setOldAccountFile(filename);
		String actual_filename = fsh.getOldAccountFile();
		assertEquals("files/" + filename, actual_filename);
	}

}
