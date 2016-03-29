import static org.junit.Assert.*;

import org.junit.Test;

public class BankingSystemBackendTest {

	@Test
	public void testMain() {
		String[] args = {"mergedTransactionFile.txt", "oldMasterBankAccount.txt"};
		BankingSystemBackend.main(args);
		//fail("Not yet implemented");
	}

}
