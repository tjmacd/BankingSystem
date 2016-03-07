#include "stdafx.h"
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

void TransactionsHelper::getName(){
    std::cout << "Enter Account holder's name:" << std::endl;
    std::cin.ignore();
    std::getline(std::cin, account_holder_name);
}

bool TransactionsHelper::getNumber(){
    std::cout << "Enter account number:" << std::endl;
	std::cin >> account_holder_number;

	if(!account_helper->validateAccount(account_holder_number, account_holder_name))
	{
		std::cout << "Account number is not valid for that account holder" << std::endl;
		account_holder_number = 0;
		return false;
	} else {
        return true;
	}
}

void TransactionsHelper::processLogin() {
	std::string session_type;

	if(!is_logged_in) {
		std::cout << "Enter session type: (standard or admin) " << std::endl;
		std::cin >> session_type;

		if(session_type == "admin") {
			is_admin = true;
			std::cout << "Logged in as Admin" << std::endl;
		} else {
			is_admin = false;
			getName();
			std::cout << "Logged in as " << account_holder_name << std::endl;
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
			getName();
		}

		if(getNumber()) {
			std::cin.ignore();
			std::cout << "Please enter amount to withdraw:" << std::endl;
			std::cin >> toWithdraw;
			if(account_helper->validateWithdrawAmount(account_holder_number, toWithdraw, is_admin)) {
                std::cout << toWithdraw << " withdrawn from account" << std::endl;
                file_stream_help->logTransaction("01", account_holder_name, account_holder_number,
				toWithdraw, "");
			}
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
			getName();
		}
		if(getNumber())	{
			std::cout << "Enter the payee company:" << std::endl;
			std::cin.ignore();
			std::cin >> company;

			if(!(company == "EC" || company == "CQ" || company == "TV")) {
				std::cout << "Company name is not recognized" << std::endl;
				return;
			}
			std::cout << "Enter amount to pay:" << std::endl;
			std::cin >> amount;
			std::cout << "$" << amount << " paid to " << company << std::endl;
			file_stream_help->logTransaction("03", account_holder_name, account_holder_number,
				amount, company);
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processTransfer() {
	float amount;
	int from_account_num;
	int to_account_num;

	if(is_logged_in) {
		if(is_admin) {
			getName();
		}
		if(getNumber())	{
			std::cout << "Enter account number to transfer to: " << std::endl;
			std::cin >> to_account_num;

			if(!account_helper->validateAccountNumber(to_account_num))
			{
				std::cout << "Invalid Account Number!" << std::endl;
				return;
			}

			std::cout << "Enter the amount to transfer: " << std::endl;
			std::cin >> amount;

			file_stream_help->logTransaction("02", account_holder_name, account_holder_number,
				amount, "");
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processDeposit() {
	float amount;

	if(is_logged_in) {
		if(is_admin) {
			getName();
		}
		if(getNumber())	{
			std::cout << "Enter the amount to deposit: " << std::endl;
			std::cin >> amount;
            std::cout << "$" << amount << " deposited to account" << std::endl;
			file_stream_help->logTransaction("04", account_holder_name, account_holder_number,
				amount, "");
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::processCreate() {
	float balance;

	if(is_logged_in) {
		if(is_admin) {
			getName();

			std::cout << "Enter the initial balance: " << std::endl;
			std::cin >> balance;

			if(std::cin.fail())
                return;

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
			getName();

			std::cout << "Enter Account holder's number: " << std::endl;
			std::cin >> account_holder_number;

			if(getNumber()){
                file_stream_help->logTransaction("06", account_holder_name, account_holder_number, 0, "");
            }
		} else {
			std::cout << "Permission Denied! Only admin can use this command"
				<< std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}

void TransactionsHelper::setStatus(bool enabled) {
	if(is_logged_in) {
		if(is_admin) {
			getName();
			if(getNumber())	{
                std::string state;
                std::string code;
                if(enabled) {
                    state = "enabled";
                    code = "09";
                } else {
                    state = "disabled";
                    code = "07";
                }
				if(account_helper->changeStatus(account_holder_number, enabled)) {
					std::cout << "Account has been " << state << std::endl;

					file_stream_help->logTransaction(code, account_holder_name,
						account_holder_number, 0, "");
				} else {
					std::cout << "Account is already " << state << "!" << std::endl;
				}
			}
		} else {
			std::cout << "Permission Denied! Only admin can use this command"
			<< std::endl;
		}
	} else {
		std::cout << "Not logged in! Please login!" << std::endl;
	}
}


void TransactionsHelper::processDisable() {
    setStatus(false);
}

void TransactionsHelper::processEnable() {
    setStatus(true);
}

void TransactionsHelper::processChangePlan() {
	if(is_logged_in) {
		if(is_admin) {
			getName();
			if(getNumber()) {
				char newplan = account_helper->changePlan(account_holder_number);
				std::cout << "Plan has been changed to " <<
					(newplan == 'S' ? "Student" : "Non-Student") << std::endl;
				file_stream_help->logTransaction("08", account_holder_name, account_holder_number, 0, "");
			}
		} else {
			std::cout << "Permission Denied! Only admin can use this command" << std::endl;
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
