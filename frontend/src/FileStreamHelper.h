#ifndef BANKINGSYSTEM_FILESTREAMHELPER_H
#define BANKINGSYSTEM_FILESTREAMHELPER_H

#pragma once

#include <string>
#include <regex>
#include "Accounts.h"
#include <fstream>

const bool DEBUG(false);
const std::regex accounts_regex(
    "^([0-9]{5}) ([a-zA-Z0-9 ]{20}) (D|A) ([0-9.]{8}) (S|N)");

// Contains methods to read from and write to external files
class FileStreamHelper
{
public:
    // Name of the current accounts file
	std::string accounts_file;
	// Name of the transaction log output file
	std::string outputs_file;
	// List of all the accounts
	std::vector<Account> accounts;

	// Constructs FileStreamHelper taking current accounts filename and
	// transaction log filename as parameters
	FileStreamHelper(std::string accounts, std::string output);
	~FileStreamHelper(void);
	// Parses account information given line of the accounts file
	void parseAccount(std::string line);
	// Returns a list of all accounts in the accounts file
	std::vector<Account> readBankAccountFile();
	// Trims whitespaces at beginning and end of str
	std::string trim(std::string& str);
	// Writes transaction log to transaction file given 2 digit code, account
	// holder name, account number, amount, and 2 character misc information
	void logTransaction(std::string code, std::string account_holder_name,
        int account_num, float amount, std::string misc);
};
#endif // BANKINGSYSTEM_FILESTREAMHELPER_H
