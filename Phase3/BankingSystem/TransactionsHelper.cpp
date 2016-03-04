#include "StdAfx.h"
#include "TransactionsHelper.h"

// External Linkage
FileStreamHelper *file_stream_help;
AccountHelper *account_helper;

TransactionsHelper::TransactionsHelper(std::string accounts, std::string output)
{
	// Construct FileStreamHelper class
	file_stream_help = new FileStreamHelper(accounts, output);
	account_helper = new AccountHelper(accounts);
	is_logged_in = false;
}

void TransactionsHelper::processLogin() {
	std::string session_type;

	if(!is_logged_in) {
		std::cout << "Enter session type: (standard or admin) ";
		std::cin >> session_type;

		if(session_type == "admin") {
			is_admin = true;
			std::cout << "Logged in as Admin" << std::endl;
		} else {
			is_admin = false;
			std::cout << "Logged in as Standard" << std::endl;

			std::cout << "Enter Account Holder's Name: ";
			std::cin >> account_holder_name;
		}

		is_logged_in = true;
		file_stream_help->logTransaction("10", account_holder_name, 0, 0, (is_admin ? "A" : "S"));
	} else {
		std::cout << "Already Logged In!" << std::endl;
	}
}

void TransactionsHelper::processLogout() {
	if(is_logged_in) {
		std::cout << "Logging out..." << std::endl;
		is_logged_in = false;

		file_stream_help->logTransaction("00", account_holder_name, 0, 0, "");
	} else {
		std::cout << "Not Logged in! Please login first!" << std::endl;
	}
}

void TransactionsHelper::processWithdrawal() {
	float toWithdraw;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(account_helper->validateAccount(account_holder_number, account_holder_name))
		{
			std::cin.ignore();
			std::cout << "Account Found! Please enter amount to withdraw: ";
			std::cin >> toWithdraw;

			account_helper->validateWithdrawAmount(account_holder_number, toWithdraw, is_admin);
			

			file_stream_help->logTransaction("01", account_holder_name, account_holder_number, 
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

void TransactionsHelper::processPaybill() {
	std::string company;
	float amount;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(account_helper->validateAccount(account_holder_number, account_holder_name))
		{
			std::cout << "Enter the payee company: ";
			std::cin.ignore();
			std::cin >> company;

			if(!(company == "EC" || company == "CQ" || company == "TV")) {
				std::cout << "Company name is not recognized" << std::endl;
				return;
			}

			std::cout << "Enter amount to pay: ";
			std::cin >> amount;

			file_stream_help->logTransaction("03", account_holder_name, account_holder_number, 
				amount, company);
		}
		else {
			std::cout << "Account not found!" << std::endl;
			return;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processTransfer() {
	float amount;
	int fromaccount_num;
	int toaccount_num;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(account_helper->validateAccount(account_holder_number, account_holder_name))
		{
			std::cout << "Enter account number to transfer to: ";
			std::cin >> toaccount_num;

			if(!account_helper->validateaccount_number(toaccount_num))
			{
				std::cout << "Invalid Account Number!" << std::endl;
				return;
			}

			std::cout << "Enter the amount to transfer: ";
			std::cin >> amount;

			file_stream_help->logTransaction("02", account_holder_name, account_holder_number, 
				amount, "");
		}
		else {
			std::cout << "Account not found!" << std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processDeposit() {
	float amount;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);
		}

		std::cout << "Enter Account holder's number: ";
		std::cin >> account_holder_number;

		if(account_helper->validateAccount(account_holder_number, account_holder_name))
		{
			std::cout << "Enter the amount to deposit: ";
			std::cin >> amount;

			file_stream_help->logTransaction("04", account_holder_name, account_holder_number, 
				amount, "");
		}
		else {
			std::cout << "Account not found!" << std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processCreate() {
	float balance;

	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);

			std::cout << "Enter the initial balance: ";
			std::cin >> balance;

			std::cout << "Account creation pending" << std::endl;

			file_stream_help->logTransaction("05", account_holder_name, 0, balance, "");
		} else {
			std::cout << "Permission Denied! Only admin can use this command" 
				<< std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processDelete() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(!account_helper->validateAccount(account_holder_number, account_holder_name))
			{
				std::cout << "Account number is not valid for that account holder" << std::endl;
				return;
			}

			file_stream_help->logTransaction("06", account_holder_name, account_holder_number, 0, "");
		} else {
			std::cout << "Permission Denied! Only admin can use this command" 
				<< std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processDisable() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(account_helper->validateAccount(account_holder_number, account_holder_name))
			{
				if(account_helper->disableAccount(account_holder_number)) {
					std::cout << "Account has been disabled!" << std::endl;

					file_stream_help->logTransaction("07", account_holder_name, 
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

void TransactionsHelper::processEnable() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(account_helper->validateAccount(account_holder_number, account_holder_name))
			{
				if(account_helper->enableAccount(account_holder_number)) {
					std::cout << "Account has been enabled!" << std::endl;
	
					file_stream_help->logTransaction("09", account_holder_name, 
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

void TransactionsHelper::processChangePlan() {
	if(is_logged_in) {
		if(is_admin) {
			std::cout << "Enter Account holder's name: ";
			std::cin.ignore();
			std::getline(std::cin, account_holder_name);

			std::cout << "Enter Account holder's number: ";
			std::cin >> account_holder_number;

			if(account_helper->validateAccount(account_holder_number, account_holder_name))
			{
				char newplan = account_helper->changePlan(account_holder_number);
				std::cout << "Plan has been changed to " << 
					(newplan == 'S' ? "Student" : "Non-Student") << std::endl;

				file_stream_help->logTransaction("08", account_holder_name, account_holder_number, 0, "");
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


TransactionsHelper::~TransactionsHelper(void)
{
	delete file_stream_help;
	delete account_helper;
}
