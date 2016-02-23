#include <iostream>
#include <fstream>
#include <vector>
#include "Accounts.h"

class FileStreamHelper {

private:
	std::string currBankAccountFile;
	std::string bankAccountTransFile;

public:
	std::vector<Accounts> accounts;

	FileStreamHelper();
	std::vector<Accounts> readBankAccountFile();
	void parseAccount(std::string line);
	std::string trim(std::string& str);
};
