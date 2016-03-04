#ifndef BANKINGSYSTEM_FILESTREAMHELPER_H
#define BANKINGSYSTEM_FILESTREAMHELPER_H

#include <string>
#include <regex>
#include "Accounts.h"
#include <fstream>

const bool DEBUG(true);
const std::regex accounts_regex("^([0-9]{5}) ([a-zA-Z0-9 ]{20}) (S|A) ([0-9.]{8}) (S|N)");

#pragma once
class FileStreamHelper
{
public:
	std::string accounts_file;
	std::string outputs_file;
	std::vector<Accounts> accounts;

	FileStreamHelper(std::string accounts, std::string output);
	~FileStreamHelper(void);
	void parseAccount(std::string line);
	std::vector<Accounts> readBankAccountFile();
	std::string trim(std::string& str);
	std::vector<std::string> readInputFile();
	void logTransaction(std::string code, std::string account_holder_name, int account_num, float amount, std::string misc);
};
#endif // BANKINGSYSTEM_FILESTREAMHELPER_H
