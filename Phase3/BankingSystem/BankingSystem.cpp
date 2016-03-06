// BankingSystem.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "TransactionsHelper.h"
#include "FileStreamHelper.h"
#include <iostream>

typedef enum {
	tLogin,
	tLogout,
	tWithdrawal,
	tPaybill,
	tTransfer,
	tCreate,
	tChangeplan,
	tEnable,
	tDisable,
	tDeposit,
	tDelete
} trans_enum;

std::string trim(std::string str) {
    size_t first = str.find_first_not_of(' ');
    size_t last = str.find_last_not_of(' ');
    return str.substr(first, (last-first+1));
}

trans_enum hascode(std::string const& job) {
	if(trim(job) == "login") return tLogin;
	if(trim(job) == "logout") return tLogout;
	if(trim(job) == "withdrawal") return tWithdrawal;
	if(trim(job) == "paybill") return tPaybill;
	if(trim(job) == "transfer") return tTransfer;
	if(trim(job) == "deposit") return tDeposit;
	if(trim(job) == "create") return tCreate;
	if(trim(job) == "changeplan") return tChangeplan;
	if(trim(job) == "enable") return tEnable;
	if(trim(job) == "disable") return tDisable;
	if(trim(job) == "delete") return tDelete;
}


int main(int argc, char* argv[])
{
	TransactionsHelper *trans_helper;

	// TransactionsHelper class init
	if(argc == 3)
		trans_helper = new TransactionsHelper(argv[1], argv[2]);
	else {
		std::cout << "Usage: BankingSystem <accounts file> <transaction file>" << std::endl;
		return 0;
	}

	std::string transactions;

		while(std::cin >> transactions) {


			switch(hascode(trim(transactions))) {
			case tLogin:
				trans_helper->processLogin();
				break;

			case tLogout:
				trans_helper->processLogout();
				break;

			case tWithdrawal:
				trans_helper->processWithdrawal();
				break;

			case tPaybill:
				trans_helper->processPaybill();
				break;

			case tTransfer:
				trans_helper->processTransfer();
				break;

			case tDeposit:
				trans_helper->processDeposit();
				break;

			case tCreate:
				trans_helper->processCreate();
				break;

			case tChangeplan:
				trans_helper->processChangePlan();
				break;

			case tEnable:
				trans_helper->processEnable();
				break;

			case tDisable:
				trans_helper->processDisable();
				break;

			case tDelete:
				trans_helper->processDelete();
				break;

			default:
				std::cout << "Invalid Transaction Command!" << std::endl;
				break;
			}


			//std::cin.ignore();
		};

	//delete trans_helper;

	return 0;
}
