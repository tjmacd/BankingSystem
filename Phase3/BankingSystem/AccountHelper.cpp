#include "stdafx.h"
#include "AccountHelper.h"

// Initialize FileStreamHelper class
FileStreamHelper *file_stream_helper;

/*
 * @method AccountHelper
 * @desc AccountHelper Constructor Method
 * @params <std::string accounts_file> name of the accounts file
 * @return none
 */
AccountHelper::AccountHelper(std::string accounts_file)
{
	// Construct FileStreamHelper with accounts file
	file_stream_helper = new FileStreamHelper(accounts_file, "");

	// Read the accounts file and load all the accounts into list of vector
	accounts = file_stream_helper->readBankAccountFile();
}

/*
 * @method AccountHelper Deconstructor
 * @desc AccountHelper Constructor Method
 * @params none
 * @return none
 */
AccountHelper::~AccountHelper(void)
{
	delete file_stream_helper;
}

/*
 * @method validateAccount
 * @desc Validate if account exists in the accounts bank
 * @params <int account_num> account number, <std::string account_name> account name associated with number
 * @return <boolean true|false> if the account is valid
 */
bool AccountHelper::validateAccount(int account_num, std::string account_name) {
	for(int i = 0; i < accounts.size(); i++)
	{
		// Check if the account number exists
		if(accounts[i].number == account_num && accounts[i].name == account_name) {
            return true; // boolean true if account found
		}
	}
	return false; // boolean false if it all fails
}

/*
 * @method validateWithdrawAmount
 * @desc Validate the withdrawal amount
 * @params <int id>			 id of the account to be withdrawn from,
				 <float amount>  amount to be withdrawn
				 <bool is_admin> true if the account is admin
 * @return none
 */
bool AccountHelper::validateWithdrawAmount(int id, float amount, bool is_admin)
{
	Account account = getAccount(id);
	float fee = getFee(account, is_admin);
	float toBewithdrawan = amount + fee;

    // If account exists from the accounts pool,
    // get the balance and validate
    if(getAccount(id).balance < toBewithdrawan) {
        std::cout << "Not enough balance!" << std::endl;
        return false;
    } else {
			account.balance = account.balance - toBewithdrawan;
			return true;
		}

	return true; // boolean true if all the above criteria matches
}

/*
 * @method getAccount
 * @desc Get Account Information
 * @params <int id> id of the account
 * @return Account structure of resulting account during search
 */
Account AccountHelper::getAccount(int id) {
	// Intitialize Structure
	Account a;

	// Iterate through the list of accounts
	for(int i = 0; i < accounts.size(); i++)
	{
		// Check if the account number matches the account id
		if(accounts[i].number == id)
		{
			return accounts[i];
		}
	}
}

/*
 * @method validateAccountNumber
 * @desc Validate the account number
 * @params <int id> account number of the account
 * @return <boolean true|false> if the account number is not valid
 */
bool AccountHelper::validateAccountNumber(int id)
{
	// Iterate through the list of accounts
	for(int i = 0; i < accounts.size(); i++)
	{
		// Check if the account number exists
		if(accounts[i].number == id)
		{
			return true; // true if exists
		}
	}
	return false; // false if above fails
}

/*
 * @method validateAccountHolderName
 * @desc Validate if the account name exists
 * @params <std::string name> name of the account holder
 * @return <bool true|false> if the account validates
 */
bool AccountHelper::validateAccountHolderName(std::string name) {
	// Iterate through the list of accounts
	for(int i = 0; i < accounts.size(); i++)
	{
		// Check if the account holder name exists
		if(accounts[i].name == name) {
			return true; // true if exists
		}
	}
	return false; // false if above fails
}

bool AccountHelper::isAccountActive(int id){
    // Iterate through the list of accounts
	for(int i = 0; i < accounts.size(); i++)
	{
		if(accounts[i].number == id) {
			return accounts[i].is_active;
		}
	}
}

/*
 * @method changeStatus
 * @desc Change the status of the account
 * @params <int id>					id of the account
					 <bool new_state> status of the account to be changed
 * @return <boolean true|false> if the account number is not valid
 */
bool AccountHelper::changeStatus(int id, bool new_state){
	// Iterate through the list of account
	for(int i = 0; i < accounts.size(); i++)
	{
		// Check if the account id exists
		if(accounts[i].number == id)
		{
			// Change the state of the account to the new status
			if(accounts[i].is_active != new_state) {
				accounts[i].is_active = new_state;
				return true;
			}
			else {
				return false;
			}
		}
	}
	return false; // Return false if all fails
}

/*
 * @method changePlan
 * @desc Change the plan of the account
 * @params <int id> account number of the account
 * @return char of the account status
 */
char AccountHelper::changePlan(int id) {
	for(int i = 0; i < accounts.size(); i++)
	{
		// Check if the account exists by its id
		if(accounts[i].number == id) {
			// Check if the account type is student, set it to false. True otherwise
			if(accounts[i].is_student)
				accounts[i].is_student = false;
			else
				accounts[i].is_student = true;

			return (accounts[i].is_student ? 'S' : 'N');
		}
	}
}

/*
 * @method getFee
 * @desc Calculate fee required for transactions
 * @params <Account account> account object
 					 <bool is_admin> boolean true if the session is admin
 * @return Account structure of resulting account during search
 */
float AccountHelper::getFee(Account account, bool is_admin) {
	// Return 0 if session is admin, no fee required
	if(is_admin) return 0;

	// If the session is standard, add fee
  if(!is_admin && account.is_student){
      return 0.05;
  } else {
      return 0.10;
  }
}

/*
 * @method deposit
 * @desc Process deposit transaction
 * @params <int id> id of the account
 					 <float amount> amount to be deposited
					 <bool is_admin> boolean true if the session is admin
 * @return <bool true|false> if the deposit is processed
 */
bool AccountHelper::deposit(int id, float amount, bool is_admin) {
	// Get Account object
    Account account = getAccount(id);

		// Get the fee of the transaction
    float fee = getFee(account, is_admin);

		// Calculate the new balance with the fee
    float newBalance = account.balance + amount - fee;

		// Check to make sure there is enough balance in the account
    if(newBalance < 0){
        std::cout << "Insufficient funds to cover fees" << std::endl;
        return false;
    }

		// Validated against maximum deposit amount threshold
    if(newBalance > MAX_AMOUNT){
        std::cout << "Cannot deposit; Account is full" << std::endl;
        return false;
    }

		// Update the balance
    account.balance -= fee;
    return true;
}

/*
 * @method deleteAccount
 * @desc Delete account from accounts pool
 * @params <int id> id of the account
 * @return <bool true|false> if the account is deleted
 */
bool AccountHelper::deleteAccount(int id) {
	// Iterate through each of the accounts
	for(int i = 0; i < accounts.size(); i++) {
		// If the account matches, delete it from pool
		if(accounts[i].number == id) {
			accounts.erase(accounts.begin() + i);
			return true;
		}
	}
	return false;
}

/*
 * @method transferAmount
 * @desc Transfer amount from 2 accounts
 * @params <int fromAccount> id of the account to be transferrd from
 					 <int toAccount> id of the account to be transferred to
					 <bool is_admin> true if the session is admin
 * @return Transfer the amount if the valdation passes
 */
bool AccountHelper::transferAmount(int fromAccount, int toAccount, float amount, bool is_admin) {
	// Get account object to be transferred from
	Account from_account = getAccount(fromAccount);

	// Get account object to be transferred to
	Account to_account = getAccount(toAccount);

	// Get the fee required for transaction
	float fee = getFee(from_account, is_admin);

	// Get the transfer amount with the fee
	float transfer_amount = amount + fee;

	// Validate against maximum transfer threshold
	if(amount > MAX_TRANSFER) {
		std::cout << "Max transfer amount cannot exceed $1000" << std::endl;
		return false;
	}

	// Validate to make sure the amount being transfered is sufficient
	if(transfer_amount < 0 || from_account.balance < transfer_amount) {
		std::cout << "Insufficient funds to cover fees" << std::endl;
		return false;
	}

	// Transfer the funds
	from_account.balance -= transfer_amount;
	to_account.balance += amount;
	return true;
}
