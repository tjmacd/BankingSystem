import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.OpenOption;
import java.nio.charset.Charset;

/* This class handles all the file I/O and populates the lists of account/transaction data to be used
 * by the rest of the program. */
public class FileHandler {
    String masterFile; // The filename of the master account file
    String accountFile; // The filename of the text file full of accounts
    String transactionFile; // The filename of the text file full of transactions
    int lineNumber; // The current line number for the transaction file
    int aLineNumber; // The current line number for the account file
    List<String> transactionLines; // A list of transaction strings
    List<String> accountLines; // A list of account strings
    List<Account> accountList; // A list of accounts built from the list of account strings
    
    // Begins the process of creating lists of transactions and accounts
    public void initialize() throws IOException { 
        Path fileTransactions = Paths.get(System.getProperty("user.dir"), transactionFile);
        Path fileAccounts = Paths.get(System.getProperty("user.dir"), accountFile);
        //Filepaths based on the given filenames from the controller class
        
        transactionLines = Files.readAllLines(fileTransactions, Charset.forName("ISO-8859-1")); // Populate the list with the strings from the text file
        accountLines = Files.readAllLines(fileAccounts, Charset.forName("ISO-8859-1")); // Populate the list with the strings from the accounts file
        createAccountList(); // Populate the list with the Accounts created from the strings from the text file
    }
    
    // Reads in each transaction text line, turning it into a Transaction object, adding it to a list
    public Transaction readNextTransactionLine() { 
        Transaction currentTransaction = new Transaction(); // Initialize the transaction
        String fileName = transactionFile; // File name for the text file
        String line = null; // Initialize the line 
        try {
            FileReader fileReader = new FileReader(fileName); // Initialize the FileReader
            BufferedReader bufferedReader = new BufferedReader(fileReader); // Initalize the BufferedReader
            if(lineNumber < transactionLines.size()){ // Stop when you reach the end of the line
                line = transactionLines.get(lineNumber); // Populate line with the contents of the text line
                currentTransaction.transactionType = line.substring(0, 2); // Substring the tranactionType
                currentTransaction.accountName = line.substring(3, 23); // Substring the accountName
                currentTransaction.accountNumber = line.substring(24, 29); // Substring the accountNumber
                currentTransaction.transactionAmount = Float.parseFloat(line.substring(30, 38)); // Substring the tranactionAmount
                currentTransaction.miscInformation = line.substring(39, 40); // Substring the miscInformation
                bufferedReader.close(); // Close the BufferedReader
                lineNumber++; // Move to the next line
            } else {
            	lineNumber = 0; // If there are no more lines, reset the line count
            	currentTransaction.accountName = "END"; // End the function
            }

        } catch (
        		//Error Handling
        FileNotFoundException ex)
        {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch (
        IOException ex)
        {
            System.out.println("Error reading file '" + fileName + "'");
        }
        return currentTransaction; //Return a transaction
    }
    
    // Creates a list of Account objects which can be accessed by other functions
    public Account createAccountList() { 
        Account currentAccount = new Account(); // Initialize an account
        accountList = new ArrayList<Account>(); // Initialize a list of Accounts
        String fileName2 = accountFile; // Filename of the account text file
        String line2 = null; // Initialize a line string
        try {
            FileReader fileReader2 = new FileReader(fileName2); // Initialize the FileReader 
            BufferedReader bufferedReader2 = new BufferedReader(fileReader2); // Initialize the BufferedReader
            for (int i = 0; i < accountLines.size(); i++) { // One for every line
                line2 = accountLines.get(i); // Read the current line
                currentAccount = new Account(); // Initalize the account
                currentAccount.accountNumber = line2.substring(0, 5); // Substring for account number
                currentAccount.accountName = line2.substring(6, 26); // Substring for account name
                currentAccount.enabledOrDisabled = line2.substring(27, 28); // Substring for enabled or disabled
                currentAccount.accountBalance = Float.parseFloat(line2.substring(29, 37)); // Substring for accoount balance
                currentAccount.studentorNonStudent = line2.substring(38, 39); // Substring for student or non student
                accountList.add(currentAccount); // Add the current account to list
                bufferedReader2.close(); // Close the BufferedReader
            }
        } catch (
        // Error Handling
        FileNotFoundException ex)
        {
            System.out.println("Unable to open file '" + fileName2 + "'");
        } catch (
        IOException ex)
        {
            System.out.println("Error reading file '" + fileName2 + "'");
        }
        return currentAccount;
    }
    
    // This function writes all the data stored in the accounts list to the accounts files.
    public void writeFiles() throws IOException { 
    	
    	// These set the filepaths to the text files
        Path fileTransactions = Paths.get(System.getProperty("user.dir"), transactionFile);
        Path fileAccounts = Paths.get(System.getProperty("user.dir"), accountFile);
        Path fileMaster = Paths.get(System.getProperty("user.dir"), masterFile);

        // Delete each file (the data is already in memory, so we're just going to create new ones
        File f = new File(transactionFile);
        f.delete();
        f = new File(accountFile);
        f.delete();
        f = new File(masterFile);
        f.delete();
        
        // Creates new master account file
    	File master = new File(fileMaster.toString());
    	FileOutputStream masterOS = new FileOutputStream(master);
    	BufferedWriter masterBW = new BufferedWriter(new OutputStreamWriter(masterOS));
    	
    	// Creates new account file (empty)
    	File accounts = new File(fileAccounts.toString());
    	FileOutputStream accountsOS = new FileOutputStream(accounts);
    	BufferedWriter accountsBW = new BufferedWriter(new OutputStreamWriter(accountsOS));
    	accountsBW.write("");
    	
    	// Creates new account file (empty)
    	File transactions = new File(fileTransactions.toString());
    	FileOutputStream transactionsOS = new FileOutputStream(transactions);
    	BufferedWriter transactionsBW = new BufferedWriter(new OutputStreamWriter(transactionsOS));
        transactionsBW.write("");
    	
    	String endLine = "";
    	
    	// Loops through the accountList and writes the data to the master and accounts file.
        for (int i = 0; i < accountList.size(); i++){
        	String line = "";
        	Account writingAccount = accountList.get(i);
        	line = line.concat(writingAccount.accountNumber);
        	line = line.concat(" ");
        	line = line.concat(writingAccount.accountName);
        	line = line.concat(" ");
        	line = line.concat(writingAccount.enabledOrDisabled);
        	line = line.concat(" ");
        	DecimalFormat formatter = new DecimalFormat("00000.00"); // this makes sure our balance is in the correct format.
        	line = line.concat(formatter.format(writingAccount.accountBalance));
        	line = line.concat(" ");
        	line = line.concat(writingAccount.studentorNonStudent);
        	
        	// if it encounters the end of file line, save it for later.
        	if (writingAccount.accountName.equals("END OF FILE         ")){
        		endLine = line;
        	} else {
        		// adds new line of account data to master and current account files
        		masterBW.write(line);
        		masterBW.newLine();
        		
        		accountsBW.write(line);
        		accountsBW.newLine();
        	}
        	
        	
        }
        
        // writes the saved end of file line to the master and account files.
        masterBW.write(endLine);
        accountsBW.write(endLine);
        
        // closes the files to finalize changes.
        accountsBW.close();
        transactionsBW.close();
        masterBW.close();

    }
}
