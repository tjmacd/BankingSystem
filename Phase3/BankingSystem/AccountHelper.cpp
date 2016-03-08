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
	// boolean check if the account is admin
	if(!is_admin && amount > WITHDRAWAL_AMOUNT) {
        std::cout << "You can only withdraw amount less than $500.00 on " <<
				"standard account" << std::endl;
        return false;
    }
    // If account exists from the accounts pool,
    // get the balance and validate
    if(getAccount(id).balance < amount) {
        std::cout << "Not enough balance!" << std::endl;
        return false;
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
			// Assign values for each of the children in the strucutre
			a.name = accounts[i].name;
			a.balance = accounts[i].balance;
			a.is_active = accounts[i].is_active;
			a.number = accounts[i].number;
		}
	}

	return a; // return Accounts struct
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
		if(accounts[i].number == id)
		{
			// Check if the account type is student, set it to false. True otherwise
			if(accounts[i].is_student)
				accounts[i].is_student = false;
			else
				accounts[i].is_student = true;

			return (accounts[i].is_student ? 'S' : 'N');
		}
	}
}

float AccountHelper::getFee(Account account) {
    if(account.is_student){
        return 0.05;
    } else {
        return 0.10;
    }
}

bool AccountHelper::deposit(int id, float amount) {
    Account account = getAccount(id);
    float fee = getFee(account);
    float newBalance = account.balance + amount - fee;
    if(newBalance < 0){
        std::cout << "Insufficient funds to cover fees" << std::endl;
        return false;
    }
    if(newBalance > 99999.99){
        std::cout << "Cannot deposit; Account is full" << std::endl;
        return false;
    }
    account.balance = newBalance;
    return true;
}
