#ifndef FLIPPINFLAPJACKS_FILESTREAMHELPER_H
#define FLIPPINFLAPJACKS_FILESTREAMHELPER_H
#include <iostream>
#include <fstream>
#include <vector>
#include "Accounts.h"

/*
 * @class FileStreamHelper
 * @desc Contains methods to read and write the file
 */
class FileStreamHelper {

private:
	std::string curr_bank_account_file;
	std::string bank_account_trans_file;

public:
	std::vector<Accounts> accounts;

	FileStreamHelper();
	std::vector<Accounts> readBankAccountFile();
	void parseAccount(std::string line);
	std::string trim(std::string& str);
	void logTransaction(std::string code, std::string account_holder_name, int account_num, float amount, std::string misc);
};
#endif // FLIPPINFLAPJACKS_FILESTREAMHELPER_H
