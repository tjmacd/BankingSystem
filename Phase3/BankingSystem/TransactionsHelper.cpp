#include "stdafx.h"
#include "TransactionsHelper.h"

// External Linkage
FileStreamHelper *file_stream_help;
AccountHelper *account_helper;

/*
 * @method TransactionsHelper
 * @desc Constructor of the class TransactionsHelper
 * @params <std::string accounts> input file
           <std::string output> output file
 * @return Construct the class TransactionsHelper
 */
TransactionsHelper::TransactionsHelper(std::string accounts,
                                        std::string output) {
	// Construct FileStreamHelper class
	file_stream_help = new FileStreamHelper(accounts, output);

  // Construct AccountHelper
	account_helper = new AccountHelper(accounts);

  // Set default login to false
	is_logged_in = false;
	std::cout << std::fixed << std::setprecision(2);
}

/*
 * @method getName
 * @desc prompt the name of the account holder
 * @params none
 * @return none
 */
void TransactionsHelper::getName(){
    // To store the input
    std::string input;

    // Get account holder's name
    if(is_admin) {
        std::cout << "Please enter account holder's name:" << std::endl;
    } else {
        std::cout << "Please enter your name:" << std::endl;
    }
    std::cin.ignore();
    std::getline(std::cin, input);
    account_holder_name = input.substr(0,20);
}

/*
 * @method validateName
 * @desc Validate the account name against the list of accounts
 * @params none
 * @return <bool true|false> true if the validation passes
 */
bool TransactionsHelper::validateName() {
    // Validate if the account holder name exists
    if(!account_helper->validateAccountHolderName(account_holder_name)){
        std::cout << "Account holder's name not found" << std::endl;
        return false;
    } else {
        return true;
    }
}

/*
 * @method validateAccountNumber
 * @desc Validate account number against the account name
 * @params none
 * @return <bool true|false> true if the validation passes
 */
bool TransactionsHelper::validateAccountNumber(){
  // Validate against the account name and number from the list of accounts
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

/*
 * @method getNumber
 * @desc Prompt for account number
 * @params none
 * @return <bool true|false> if the validation passes
 */
bool TransactionsHelper::getNumber(){
    std::cout << "Enter the account number:" << std::endl;
	std::cin >> account_holder_number;
    return validateAccountNumber();
}

/*
 * @method getNumber ~overload
 * @desc Promot for the account number
 * @params <std::string prompt> prompt message
 * @return <bool true|false> if the validation passes
 */
bool TransactionsHelper::getNumber(std::string prompt){
    std::cout << prompt << std::endl;
    std::cin >> account_holder_number;
    return validateAccountNumber();
}

/*
 * @method checkLoggedIn
 * @desc Check if the session is active
 * @params none
 * @return <bool true|false> true if the session is active
 */
bool TransactionsHelper::checkLoggedIn() {
    if(!is_logged_in){
        std::cout << "Must be logged in before logging out" << std::endl;
        return false;
    } else {
        return true;
    }
}

/*
 * @method checkPrivileged
 * @desc Check if the session is admin
 * @params none
 * @return <bool true|false> true if the session is admin
 */
bool TransactionsHelper::checkPrivileged() {
    if(!is_admin){
        std::cout << "Permission denied. Only admin can use this command"
				<< std::endl;
        return false;
    } else {
        return true;
    }
}

/*
 * @method verifyInputAmount
 * @desc Validate the format of the amount
 * @params <std::string input> amount input
           <float &amount_output> formatted amount output
 * @return Account structure of resulting account during search
 */
bool TransactionsHelper::verifyInputAmount(std::string input,
                                            float& amount_output){
    // Run the input string against a regex
    if(!std::regex_match(input, std::regex("[0-9]+\\.[0-9]{2}"))){
        std::cout << "Invalid amount input" << std::endl;
        return false;
    }

    // Convert the string input to float
    amount_output = std::atof(input.c_str());

    // Check against maximum amount threshold
    if(amount_output > account_helper->MAX_AMOUNT){
        std::cout << "Input amount is too high. It must be no greater than $"
                    << account_helper->MAX_AMOUNT << std::endl;
        return false;
    }
    return true;
}

/*
 * @method processLogin
 * @desc process login transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processLogin() {
	std::string session_type;

  // Check if the session is inactive
	if(!is_logged_in) {
		std::cout << "Please select session type (standard or admin):" << std::endl;
		std::cin >> session_type;

    // Prompt for account name, if the session is not admin
		if(session_type == "admin") {
			is_admin = true;
			std::cout << "Logged in as admin" << std::endl;
		} else {
			is_admin = false;
			getName();
			std::cout << "Logged in as " << account_holder_name << std::endl;
		}

    // Set the login status to true
		is_logged_in = true;

    // Log the transaction
		file_stream_help->logTransaction("10", account_holder_name, 0, 0,
                                            (is_admin ? "A" : "S"));
	} else {
		std::cout << "Already logged in!" << std::endl;
	}
}

/*
 * @method processLogout
 * @desc Process logout transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processLogout() {
  // Check if the session is active
	if(checkLoggedIn()) {
    // Set the logged in state to false
		is_logged_in = false;

    // clear the holder's name
		if(is_admin) account_holder_name = "";
        std::cout << "Logged out" << std::endl;

    // Log the transaction
		file_stream_help->logTransaction("00", account_holder_name, 0, 0, "");
	}
}

/*
 * @method processWithdrawal
 * @desc Process withdrawal transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processWithdrawal() {
	std::string toWithdraw;
  float amount;

  // Check if the session is active
	if(checkLoggedIn()) {
    // Check if the session is admin
		if(is_admin) {
      // Get the name of the account holder
			getName();
		}

    // Get number of account holder
		if(getNumber()) {
			std::cin.ignore();
			std::cout << "Enter the amount to withdraw:" << std::endl;
			std::cin >> toWithdraw;

      // Check if the account is disabled, cannot further process
      if(!account_helper->isAccountActive(account_holder_number)) {
        std::cout << "Cannot process transaction on disabled account" << std::endl;
        return;
      }
            // Validate the input amount with the limit
            if(verifyInputAmount(toWithdraw, amount)){
                if(!is_admin && amount > WITHDRAWAL_LIMIT) {
                    std::cout << "You can only withdraw amount less than $500.00 on " <<
                            "standard account" << std::endl;
                    return;
                }
                // Validate withdrawal amount against the list of accounts
                if(account_helper->validateWithdrawAmount(account_holder_number, amount, is_admin)) {
                    std::cout << "$" << toWithdraw << " withdrawn from account" << std::endl;

                    // Log the transcation
                    file_stream_help->logTransaction("01", account_holder_name, account_holder_number,
                        amount, "");
                }
			}
		}
	}
}

/*
 * @method processPaybill
 * @desc Process paybill transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processPaybill() {
	std::string company;
	std::string amount_input;
	float amount;

  // Check if the session is active
	if(checkLoggedIn()) {
    // Check if the session is admin
		if(is_admin) {
      // Get the name of the account holder
			getName();
		}

    // Get the number of the account holder
		if(getNumber())	{
      // Check if the account isn't disabled
      if(!account_helper->isAccountActive(account_holder_number)) {
        std::cout << "Cannot process transaction on disabled account" << std::endl;
        return;
      }

      // Prompt for payee company info
			std::cout << "Enter the payee company:" << std::endl;
			std::cin.ignore();
			std::cin >> company;

      // Validate the company type
			if(!(company == "EC" || company == "CQ" || company == "TV")) {
				std::cout << "Company name is not recognized" << std::endl;
				return;
			}

      // Prompt for the amount
			std::cout << "Enter the amount to pay:" << std::endl;
			std::cin >> amount_input;

      // Validate amount for correct format
			if(!verifyInputAmount(amount_input, amount)){
                return;
			}

      // Validate withdrawal amount
			if(!account_helper->validateWithdrawAmount(account_holder_number,
                                                        amount, is_admin)){
                return;
            }
      // Display the output
			std::cout << "$" << amount << " paid to " << company << std::endl;

      // Log the transaction
			file_stream_help->logTransaction("03", account_holder_name,
                                                account_holder_number, amount,
                                                company);
		}
	}
}

/*
 * @method processTransfer
 * @desc Process transfer transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processTransfer() {
  std::string amountToTransfer;
	float amount;
	int from_account_num;
	int to_account_num;

  // Check if the session is active
	if(checkLoggedIn()) {
    // Check if the session is admin
		if(is_admin) {
      // Prompt for account holder's name
			getName();
		}

    // Get account holder's number
		if(getNumber("Enter the account number to transfer from:"))	{

      // Prompt the user to enter account number to transfer to
			std::cout << "Enter the account number to transfer to:" << std::endl;
			std::cin >> to_account_num;

      // Validate the account to be transfered to
			if(!account_helper->validateAccountNumber(to_account_num)) {
				std::cout << "Invalid Account Number!" << std::endl;
				return;
			}

      // Check if the account is active and ready to transaction
      if(!account_helper->isAccountActive(account_holder_number) || !account_helper->isAccountActive(to_account_num)) {
        std::cout << "Cannot process transaction on disabled account" << std::endl;
        return;
      }

      // Prompt the amount to be transfered
			std::cout << "Enter the amount to transfer:" << std::endl;
			std::cin >> amountToTransfer;

      // Verify input amount for correct format
      if(!verifyInputAmount(amountToTransfer, amount)) return;

      // Display the log
			std::cout << "$" << amount << " transfered to account " << to_account_num << std::endl;

      // Transfer the amount
      account_helper->transferAmount(account_holder_number, to_account_num, amount, is_admin);

      // Log the transaction
			file_stream_help->logTransaction("02", account_holder_name, account_holder_number,
				amount, "");

      // Log the transaction
      file_stream_help->logTransaction("02", account_holder_name, to_account_num,
				amount, "");
		}
	}
}

/*
 * @method processDeposit
 * @desc Process deposit transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processDeposit() {
  std::string input;
	float amount;

  // Check if the session is active
	if(checkLoggedIn()) {
    // Check if the session is admin
		if(is_admin) {
      // Prompt the user for account name
			getName();

      // Validate the name against list of accounts
			if(!validateName()){
                return;
			}
		}

    // Get the account number
		if(getNumber()) {
      // Check if the account is disabled, cannot further process
            if(account_helper->isAccountActive(account_holder_number))	{
              // Prompt for the amount to be deposited
                std::cout << "Enter the amount to deposit:" << std::endl;
                std::cin >> input;

                // Validate the amount for correct format
                if(!verifyInputAmount(input, amount)) return;

                // Deposit the amount, if it fails then don't process anything
                if(!account_helper->deposit(account_holder_number, amount, is_admin)) return;

                // Display the info on the console
                std::cout << "$" << amount << " deposited to account" << std::endl;

                // Log the transaction
                file_stream_help->logTransaction("04", account_holder_name, account_holder_number,
                                                    amount, "");
            } else {
                std::cout << "Cannot process transaction on disabled account" << std::endl;
            }
        }
	}
}

/*
 * @method processCreate
 * @desc Process account creation transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processCreate() {
	float balance;
	std::string input;

  // Check if the session is active
	if(checkLoggedIn()) {
    // Check if the session is admin
		if(checkPrivileged()) {
      // Get the name of the account holder
			getName();

      // Get the initial balance to be created with
			std::cout << "Enter the initial balance:" << std::endl;
			std::cin >> input;

      // If the input fails, do not process further
			if(std::cin.fail())
                return;

            // Verify the amount for correct format
            if(!verifyInputAmount(input, balance)){
                return;
            }

			std::cout << "Account creation pending" << std::endl;

      // Log the transaction
			file_stream_help->logTransaction("05", account_holder_name, 0,
                                                balance, "");
		}
	}
}

/*
 * @method processDelete
 * @desc Process delete transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processDelete() {
  // Check if the session is active
	if(checkLoggedIn()) {
    // Check if session is admin
		if(checkPrivileged()) {
			getName();
			if(!validateName()){
                return;
			}

			if(getNumber()) {
        if(!account_helper->isAccountActive(account_holder_number)) {
          std::cout << "Cannot process transaction on disabled account" << std::endl;
          return;
        }

        if(account_helper->deleteAccount(account_holder_number)){
        std::cout << "Account has been deleted" << std::endl;
                file_stream_help->logTransaction("06", account_holder_name,
                                                account_holder_number, 0, "");
            }
          }
		}
	}
}

/*
 * @method setStatus
 * @desc Change the status of the account
 * @params <bool enabled> status of the account
 * @return none
 */
void TransactionsHelper::setStatus(bool enabled) {
  // Check if the session is login
	if(checkLoggedIn()) {
    // Check if the session is admin
		if(checkPrivileged()) {
      // Get the account holder's name
			getName();

      // Validate the name from the bank of accounts
			if(!validateName()){
                return;
			}

      // Get account number
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

        // Change the staus of the account by calling changeStatus method
        // from AccountHelper class
				if(account_helper->changeStatus(account_holder_number, enabled)) {
					std::cout << "Account has been " << state << std::endl;

          // Log the transaction
					file_stream_help->logTransaction(code, account_holder_name,
						account_holder_number, 0, "");
				} else {
					std::cout << "Account is already " << state << "!" << std::endl;
				}
			}
		}
	}
}

/*
 * @method processDisable
 * @desc Set the account status to false
 * @params none
 * @return none
 */
void TransactionsHelper::processDisable() {
      // Set the status of account to false
      setStatus(false);
}

/*
 * @method processEnable
 * @desc Set the account status to true
 * @params none
 * @return none
 */
void TransactionsHelper::processEnable() {
    setStatus(true);
}

/*
 * @method processChangePlan
 * @desc Process change plan transaction
 * @params none
 * @return none
 */
void TransactionsHelper::processChangePlan() {
  // Check if the session is active
	if(checkLoggedIn()) {
    // Check if the session is admin
		if(checkPrivileged()) {
      // Get account holder's name
			getName();

      // Get account holder's number
			if(getNumber()) {
        // Call changePlan method from AccountHelper class
				char newplan = account_helper->changePlan(account_holder_number);

        // Display the output
				std::cout << "Plan has been changed to " <<
					(newplan == 'S' ? "Student" : "Non-Student") << std::endl;

        // Log the transaction
				file_stream_help->logTransaction("08", account_holder_name, account_holder_number, 0, "");
			}
		}
	}
}

/*
 * @method ~TransactionsHelper
 * @desc Deconstructor of TransactionsHelper
 * @params none
 * @return none
 */
TransactionsHelper::~TransactionsHelper(void)
{
	delete file_stream_help;
	delete account_helper;
}
