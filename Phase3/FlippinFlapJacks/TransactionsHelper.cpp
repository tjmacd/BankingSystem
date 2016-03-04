#include "stdafx.h"
#include <iostream>
#include <string>
#include "TransactionsHelper.h"
#include "FileStreamHelper.h"

/*
 * @method TransactionsHelper
 * @params none
 * @return none
 * @desc TransactionHelper Constructor
 */
TransactionsHelper::TransactionsHelper() {
	is_logged_in = false;
	ah = AccountHelper();
}

/*
 * @method processLogin
 * @params none
 * @return none
 * @desc Processes login transaction when login command is selected
 */
void TransactionsHelper::processLogin() {
	std::string session_type;

	if(!is_logged_in)
	{
		std::cout << "Enter session type: (standard or admin) ";
		std::cin >> session_type;

		if(session_type == "admin")
		{
			is_admin = true;
			std::cout << "Logged in as Admin" << std::endl;
		}
		else
		{
			is_admin = false;
			std::cout << "Logged in as Standard" << std::endl;

			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;
		}

		is_logged_in = true;

		FileStreamHelper fsh;
		fsh.logTransaction("10", account_holder_name, 0, 0, 
			(is_admin ? "A" : "S"));
		fsh.readBankAccountFile();
	}
	else
	{
		std::cout << "Already Logged in" << std::endl;
	}
}

/*
 * @method processLogout
 * @params none
 * @return none
 * @desc Process logout transaction when logout command is called
 */
void TransactionsHelper::processLogout() {
	if(is_logged_in)
	{
		std::cout << "Logging out..." << std::endl;
		is_logged_in = false;
		FileStreamHelper fsh;
		fsh.logTransaction("00", account_holder_name, 0, 0, 
			(is_admin ? "A" : "S"));
	}
	else
	{
		std::cout << "Not logged in! Please login first!" << std::endl;
	}
}

/*
 * @method processWithdrawal
 * @params none
 * @return none
 * @desc Process withdrawal transaction when withdrawal command is called
 */
void TransactionsHelper::processWithdrawal() {
	float toWithdraw;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(ah.validateAccount(account_holder_number, account_holder_name))
		{
			std::cout << "Account Found! Please enter amount to withdraw: ";
			std::cin >> toWithdraw;

			ah.validateWithdrawAmount(account_holder_number, toWithdraw, is_admin);
			FileStreamHelper fsh;
			fsh.logTransaction("01", account_holder_name, account_holder_number, 
				toWithdraw, "");
		}
		else
		{
			std::cout << "Account not found!" << std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processPaybill
 * @params none
 * @return none
 * @desc Process paybill transaction when paybill command is called
 */
void TransactionsHelper::processPaybill() {
	std::string company;
	float amount;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(ah.validateAccount(account_holder_number, account_holder_name))
		{
			std::cout << "Enter the payee company: ";
			std::cin >> company;

			if(!(company == "EC" || company == "CQ" || company == "TV")) {
				std::cout << "Company name is not recognized" << std::endl;
				return;
			}

			std::cout << "Enter amount to pay: ";
			std::cin >> amount;

			FileStreamHelper fsh;
			fsh.logTransaction("03", account_holder_name, account_holder_number, 
				amount, company);
		}
		else {
			std::cout << "Account not found!" << std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processTransfer
 * @params none
 * @return none
 * @desc Process transfer transaction when transfer command is called
 */
void TransactionsHelper::processTransfer() {
	float amount;
	int fromaccount_num;
	int toaccount_num;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(ah.validateAccount(account_holder_number, account_holder_name))
		{
			std::cout << "Enter account number to transfer to: ";
			std::cin >> toaccount_num;

			if(!ah.validateaccount_number(account_holder_number))
			{
				std::cout << "Invalid Account Number!" << std::endl;
				return;
			}

			std::cout << "Enter the amount to transfer: ";
			std::cin >> amount;

			FileStreamHelper fsh;
			fsh.logTransaction("02", account_holder_name, account_holder_number, 
				amount, "");
		}
		else {
			std::cout << "Account not found!" << std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processDeposit
 * @params none
 * @return none
 * @desc Process deposit transaction when deposit command is called
 */
void TransactionsHelper::processDeposit() {
	float amount;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(ah.validateAccount(account_holder_number, account_holder_name))
		{
			std::cout << "Enter the amount to deposit: ";
			std::cin >> amount;

			FileStreamHelper fsh;
			fsh.logTransaction("04", account_holder_name, account_holder_number, 
				amount, "");
		}
		else {
			std::cout << "Account not found!" << std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processCreate
 * @params none
 * @return none
 * @desc Process creation of new account when create command is called
 */
void TransactionsHelper::processCreate() {
	float balance;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;

			std::cout << "Enter the initial balance: ";
			std::cin >> balance;

			std::cout << "Account creation pending" << std::endl;

			FileStreamHelper fsh;
			fsh.logTransaction("05", account_holder_name, 0, balance, "");
		} else {
			std::cout << "Permission Denied! Only admin can use this command" 
				<< std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processDelete
 * @params none
 * @return none
 * @desc Delete an account when delete command is called
 */
void TransactionsHelper::processDelete() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(!ah.validateAccount(account_holder_number, account_holder_name))
			{
				std::cout << "Account number is not valid for that account holder" << std::endl;
				return;
			}

			FileStreamHelper fsh;
			fsh.logTransaction("06", account_holder_name, account_holder_number, 0, "");
		} else {
			std::cout << "Permission Denied! Only admin can use this command" 
				<< std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processDisable
 * @params none
 * @return none
 * @desc Disable an account when disable command is called
 */
void TransactionsHelper::processDisable() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(ah.validateAccount(account_holder_number, account_holder_name))
			{
				if(ah.disableAccount(account_holder_number)) {
					std::cout << "Account has been disabled!" << std::endl;

					FileStreamHelper fsh;
					fsh.logTransaction("07", account_holder_name, 
						account_holder_number, 0, "");
				} else {
					std::cout << "Account is already disabled!" << std::endl;
				}
			}
			else {
				std::cout << "Account not found!" << std::endl;
			}
		} else {
			std::cout << "Permission Denied! Only admin can use this command";
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processEnable
 * @params none
 * @return none
 * @desc Enable an account when enable command is called
 */
void TransactionsHelper::processEnable() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(ah.validateAccount(account_holder_number, account_holder_name))
			{
				if(ah.enableAccount(account_holder_number)) {
					std::cout << "Account has been enabled!" << std::endl;
					FileStreamHelper fsh;
					fsh.logTransaction("09", account_holder_name, 
						account_holder_number, 0, "");
				} else {
					std::cout << "Account is already active!" << std::endl;
				}
			}
			else {
				std::cout << "Account not found!" << std::endl;
			}
		} else {
			std::cout << "Permission Denied! Only admin can use this command";
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

/*
 * @method processChangePlan
 * @params none
 * @return none
 * @desc Change account plan when changeplan command is called
 */
void TransactionsHelper::processChangePlan() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin >> account_holder_name;

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(ah.validateAccount(account_holder_number, account_holder_name))
			{
				char newplan = ah.changePlan(account_holder_number);
				std::cout << "Plan has been changed to " << 
					(newplan == 'S' ? "Student" : "Non-Student") << std::endl;

				FileStreamHelper fsh;
				fsh.logTransaction("08", account_holder_name, account_holder_number, 0, "");
			}
			else {
				std::cout << "Account not found!" << std::endl;
			}
		} else {
			std::cout << "Permission Denied! Only admin can use this command";
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}