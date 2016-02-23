#include "stdafx.h"
#include <iostream>
#include <fstream>
#include <string>
#include <regex>
#include "FileStreamHelper.h"

using namespace std;

FileStreamHelper::FileStreamHelper() {
	currBankAccountFile = "C:/Users/100490143/Documents/Visual Studio 2010/Projects/FlippinFlapJacks/Files/CurrentBankAccountFile.txt";
	//bankAccountTransFile = accTransFile;
}

void FileStreamHelper::parseAccount(string line) {
	smatch match;
	regex exp("^([0-9]{5}) ([a-zA-Z0-9 ]{20}) (S|A) ([0-9.]{8})");
	if(regex_match(line, match, exp))
	{
		if(regex_search(line, match, exp)) 
		{
			struct Accounts account;
			account.num = stoi(match[1]);
			string temp = match[2];
			account.name = trim(temp);
			account.active = (match[3] == 'A') ? true : false;
			account.balance = stof(match[4]);

			accounts.push_back(account);
		}	
	}
}

std::vector<Accounts> FileStreamHelper::readBankAccountFile() {
	ifstream bankAccFileStream;
	bankAccFileStream.open(currBankAccountFile);

	string line;
	if(bankAccFileStream.is_open())
	{
		while(getline(bankAccFileStream, line))
		{
			parseAccount(line);
		}
	}
	else
	{
		cout << "Current Bank Accounts File not found!" << endl;
	}

	return accounts;
}

std::string FileStreamHelper::trim(string& str) {
    size_t first = str.find_first_not_of(' ');
    size_t last = str.find_last_not_of(' ');
    return str.substr(first, (last-first+1));
}