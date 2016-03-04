#include "stdafx.h"
#include <iostream>
#include <fstream>
#include <string>
#include <regex>
#include "FileStreamHelper.h"

/*
 * @method FileStreamHelper
 * @params none
 * @return none
 * @desc FileStreamHelper Constructor
 */
FileStreamHelper::FileStreamHelper() {
	curr_bank_account_file = "../Files/CurrentBankAccountFile.txt";
	bank_account_trans_file = "../Files/BankTransactions.txt";
}

/*
 * @method parseAccount
 * @params string line
 * @return none
 * @desc Parse account information from the line provided in the argument
 */
void FileStreamHelper::parseAccount(std::string line) {
	std::smatch match;
	std::regex exp("^([0-9]{5}) ([a-zA-Z0-9 ]{20}) (S|A) ([0-9.]{8}) (S|N)");
	if(regex_match(line, match, exp))
	{
		if(std::regex_search(line, match, exp)) 
		{
			struct Accounts account;
			account.num = stoi(match[1]);
			std::string temp = match[2];
			account.name = trim(temp);
			account.active = (match[3] == 'A') ? true : false;
			account.balance = stof(match[4]);
			account.student = (match[5] == 'S') ? true : false;

			accounts.push_back(account);
		}	
	}
}

/*
 * @method readBankAccountFile
 * @params none
 * @return vector<Accounts>
 * @desc Read the current bank account file and add each account structure to list of vector
 */
std::vector<Accounts> FileStreamHelper::readBankAccountFile() {
	std::ifstream bankAccFileStream;
	bankAccFileStream.open(curr_bank_account_file);

	std::string line;
	if(bankAccFileStream.is_open())
	{
		while(std::getline(bankAccFileStream, line))
		{
			parseAccount(line);
		}
	}
	else
	{
		std::cout << "Current Bank Accounts File not found!" << std::endl;
	}

	return accounts;
}

/*
 * @method trim
 * @params string text to be trimmed
 * @return trimmed string
 * @desc Trim whitespaces
 */
std::string FileStreamHelper::trim(std::string& str) {
    size_t first = str.find_first_not_of(' ');
    size_t last = str.find_last_not_of(' ');
    return str.substr(first, (last-first+1));
}

/*
 * @method logTransaction
 * @params string code, string account name, int account number, float amount, string misc
 * @return none
 * @desc Prints the transaction file with various transaction information
 */
void FileStreamHelper::logTransaction(std::string code, 
	std::string account_holder_name, int account_num, float amount, std::string misc) {
	FILE *transactionFile = fopen(bank_account_trans_file.c_str(), "a");
	fprintf(transactionFile, "%s %-20s %05d %08.2f %-2s\n", code.c_str(), 
		account_holder_name.c_str(), account_num, amount, misc.c_str());
	fclose(transactionFile);
}