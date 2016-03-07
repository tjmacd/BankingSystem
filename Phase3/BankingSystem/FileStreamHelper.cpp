#include "stdafx.h"
#include "FileStreamHelper.h"
#include <iostream>

FileStreamHelper::FileStreamHelper(std::string accounts, std::string output) : accounts_file(""), outputs_file("")
{
	//if(DEBUG) std::cout << "[DEBUG]: FileStreamHelper Constructed" << std::endl;
	if(accounts != "") accounts_file = accounts;
	if(output != "") outputs_file = output;
}

void FileStreamHelper::parseAccount(std::string line)
{
	// Initialize smatch variable to hold match results for string object
	std::smatch match;

	// Match sequence against each line to extract data
	if(regex_match(line, match, accounts_regex))
	{
		struct Accounts account;
		account.number = stoi(match[1]);
		std::string temp = match[2];
		account.name = trim(temp);
		account.is_active = (match[3] == 'A') ? true : false;
		account.balance = stof(match[4]);
		account.is_student = (match[5] == 'S') ? true : false;

		// Push each account structure into accounts vector
		accounts.push_back(account);
	}
}

std::vector<Accounts> FileStreamHelper::readBankAccountFile()
{
	// Initialize ifstream variable
	std::ifstream bank_acc_file_stream;

	// Initialize string variable to hold each line
	std::string line;

	// Open the file for reading
	bank_acc_file_stream.open(accounts_file);

	// Check if stream is currently associated to a file
	if(bank_acc_file_stream.is_open()) {
		// Get the line from stream into string and parse each line
		while(std::getline(bank_acc_file_stream, line)) parseAccount(line);
	} else {
		std::cout << "[ERROR]: Accounts File Not Found!" << std::endl;
	}

	return accounts;
}

FileStreamHelper::~FileStreamHelper(void)
{
	accounts_file.clear();
	outputs_file.clear();
	//std::cout << "[DEBUG]: FileStreamHelper Deconstructed" << std::endl;
}

std::string FileStreamHelper::trim(std::string& str) {
    size_t first = str.find_first_not_of(' ');
    size_t last = str.find_last_not_of(' ');
    return str.substr(first, (last-first+1));
}

void FileStreamHelper::logTransaction(std::string code,
	std::string account_holder_name, int account_num, float amount, std::string misc) {
	if(outputs_file != "") {
		FILE *transactionFile = fopen(outputs_file.c_str(), "a");
		fprintf(transactionFile, "%s %-20s %05d %08.2f %-2s\n", code.c_str(),
			account_holder_name.c_str(), account_num, amount, misc.c_str());
		fclose(transactionFile);
	}
}
