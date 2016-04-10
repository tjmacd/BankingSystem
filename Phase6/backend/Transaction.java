//Object used to store the data from one line of Transactions.txt, containing all necessary information
public class Transaction {
	String transactionType; // Two digit code that determines which transaction will occur
	String accountName; // The name of the account holder associated with this transaction
	String accountNumber; // The account number associated with this transaction
	float transactionAmount; // The monetary value for the transaction (if applicable)
	String miscInformation; // Any additional information regarding the transaction (eg. Company Code)
}
