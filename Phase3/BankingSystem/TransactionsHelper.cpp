#include "stdafx.h"
#include "TransactionsHelper.h"


const float MAX_AMOUNT = 99999.99;
// External Linkage
FileStreamHelper *file_stream_help;
AccountHelper *account_helper;

TransactionsHelper::TransactionsHelper(std::string accounts,
                                        std::string output) {
	// Construct FileStreamHelper class
	file_stream_help = new FileStreamHelper(accounts, output);
	account_helper = new AccountHelper(accounts);
	is_logged_in = false;
	std::cout << std::fixed << std:: setprecision(2);
}

void TransactionsHelper::getName(){
    std::string input;
    if(is_admin) {
        std::cout << "Please enter account holder's name:" << std::endl;
    } else {
        std::cout << "Please enter your name:" << std::endl;
    }
    std::cin.ignore();
    std::getline(std::cin, input);
    account_holder_name = input.substr(0,20);
}

bool TransactionsHelper::validateName() {
    if(!account_helper->validateAccountHolderName(account_holder_name)){
        std::cout << "Account holder's name not found" << std::endl;
        return false;
    } else {
        return true;
    }
}

bool TransactionsHelper::validateAccountNumber(){
	if(!account_helper->validateAccount(account_holder_number,
                                        account_holder_name)) {
		std::cout << "Account number is not valid for that account holder"
            << std::endl;
		account_holder_number = 0;
		return false;
	} else {
        return true;
	}
}

bool TransactionsHelper::getNumber(){
    std::cout << "Enter the account number:" << std::endl;
	std::cin >> account_holder_number;
    return validateAccountNumber();
}

bool TransactionsHelper::getNumber(std::string prompt){
    std::cout << prompt << std::endl;
    std::cin >> account_holder_number;
    return validateAccountNumber();
}

bool TransactionsHelper::checkLoggedIn() {
    if(!is_logged_in){
        std::cout << "Must be logged in before logging out" << std::endl;
        return false;
    } else {
        return true;
    }
}

bool TransactionsHelper::checkPrivileged() {
    if(!is_admin){
        std::cout << "Permission denied. Only admin can use this command"
				<< std::endl;
        return false;
    } else {
        return true;
    }
}

bool TransactionsHelper::verifyInputAmount(std::string input,
                                            float& amount_output){
    if(!std::regex_match(input, std::regex("[0-9]+\\.[0-9]{2}"))){
        std::cout << "Invalid amount input" << std::endl;
        return false;
    }
    amount_output = std::atof(input.c_str());
    if(amount_output > MAX_AMOUNT){
        std::cout << "Input amount is too high. It must be no greater than $"
                    << MAX_AMOUNT << std::endl;
        return false;
    }
    return true;
}

void TransactionsHelper::processLogin() {
	std::string session_type;

	if(!is_logged_in) {
		std::cout << "Please select session type (standard or admin):" << std::endl;
		std::cin >> session_type;

		if(session_type == "admin") {
			is_admin = true;
			std::cout << "Logged in as admin" << std::endl;
		} else {
			is_admin = false;
			getName();
			std::cout << "Logged in as " << account_holder_name << std::endl;
		}

		is_logged_in = true;
		file_stream_help->logTransaction("10", account_holder_name, 0, 0,
                                            (is_admin ? "A" : "S"));
	} else {
		std::cout << "Already logged in!" << std::endl;
	}
}

void TransactionsHelper::processLogout() {
	if(checkLoggedIn()) {
		is_logged_in = false;
		if(is_admin) account_holder_name = "";
        std::cout << "Logged out" << std::endl;
		file_stream_help->logTransaction("00", account_holder_name, 0, 0, "");
	}
}

void TransactionsHelper::processWithdrawal() {
	std::string toWithdraw;
  float amount;

	if(checkLoggedIn()) {
		if(is_admin) {
			getName();
		}

		if(getNumber()) {
			std::cin.ignore();
			std::cout << "Enter the amount to withdraw:" << std::endl;
			std::cin >> toWithdraw;
      if(verifyInputAmount(toWithdraw, amount) && account_helper->validateWithdrawAmount(account_holder_number, amount, is_admin)) {
                std::cout << "$" << toWithdraw << " withdrawn from account" << std::endl;
                file_stream_help->logTransaction("01", account_holder_name, account_holder_number,
				amount, "");
			}
		}
	}
}

void TransactionsHelper::processPaybill() {
	std::string company;
	float amount;

	if(checkLoggedIn()) {
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
			std::cout << "Enter the amount to pay:" << std::endl;
			std::cin >> amount;
			std::cout << "$" << amount << " paid to " << company << std::endl;
			file_stream_help->logTransaction("03", account_holder_name,
                                                account_holder_number, amount,
                                                company);
		}
	}
}

void TransactionsHelper::processTransfer() {
	float amount;
	int from_account_num;
	int to_account_num;

	if(checkLoggedIn()) {
		if(is_admin) {
			getName();
		}
		if(getNumber("Enter the account number to transfer from:"))	{
			std::cout << "Enter the account number to transfer to:" << std::endl;
			std::cin >> to_account_num;

			if(!account_helper->validateAccountNumber(to_account_num)) {
				std::cout << "Invalid Account Number!" << std::endl;
				return;
			}

			std::cout << "Enter the amount to transfer:" << std::endl;
			std::cin >> amount;

			std::cout << "$" << amount << " transfered to account " << to_account_num << std::endl;
			file_stream_help->logTransaction("02", account_holder_name, account_holder_number,
				amount, "");
            file_stream_help->logTransaction("02", account_holder_name, to_account_num,
				amount, "");
		}
	}
}

void TransactionsHelper::processDeposit() {
    std::string input;
	float amount;

	if(checkLoggedIn()) {
		if(is_admin) {
			getName();
		}
		if(getNumber()) {
            if(account_helper->isAccountActive(account_holder_number))	{
                std::cout << "Enter the amount to deposit:" << std::endl;
                std::cin >> input;
                if(!verifyInputAmount(input, amount)){
                    return;
                }
                if(!account_helper->deposit(account_holder_number, amount)){
                    return;
                }
                std::cout << "$" << amount << " deposited to account" << std::endl;
                file_stream_help->logTransaction("04", account_holder_name, account_holder_number,
                                                    amount, "");
            } else {
                std::cout << "Account is disabled, cannot process transaction" << std::endl;
            }
        }
	}
}

void TransactionsHelper::processCreate() {
	float balance;
	std::string input;

	if(checkLoggedIn()) {
		if(checkPrivileged()) {
			getName();

			std::cout << "Enter the initial balance:" << std::endl;
			std::cin >> input;

			if(std::cin.fail())
                return;
            if(!verifyInputAmount(input, balance)){
                return;
            }

			std::cout << "Account creation pending" << std::endl;
			file_stream_help->logTransaction("05", account_holder_name, 0,
                                                balance, "");
		}
	}
}

void TransactionsHelper::processDelete() {
	if(checkLoggedIn()) {
		if(checkPrivileged()) {
			getName();
			if(!validateName()){
                return;
			}
			std::cout << "Enter Account holder's number: " << std::endl;
			std::cin >> account_holder_number;

			if(getNumber()){
                file_stream_help->logTransaction("06", account_holder_name,
                                                account_holder_number, 0, "");
            }
		}
	}
}

void TransactionsHelper::setStatus(bool enabled) {
	if(checkLoggedIn()) {
		if(checkPrivileged()) {
			getName();
			if(!validateName()){
                return;
			}
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
		}
	}
}


void TransactionsHelper::processDisable() {
    setStatus(false);
}

void TransactionsHelper::processEnable() {
    setStatus(true);
}

void TransactionsHelper::processChangePlan() {
	if(checkLoggedIn()) {
		if(checkPrivileged()) {
			getName();
			if(getNumber()) {
				char newplan = account_helper->changePlan(account_holder_number);
				std::cout << "Plan has been changed to " <<
					(newplan == 'S' ? "Student" : "Non-Student") << std::endl;
				file_stream_help->logTransaction("08", account_holder_name, account_holder_number, 0, "");
			}
		}
	}
}


TransactionsHelper::~TransactionsHelper(void)
{
	delete file_stream_help;
	delete account_helper;
}
