// FlippinFlapJacks.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include "FileStreamHelper.h"
#include "TransactionsHelper.h"

/*
 * @method _tmain
 * @params int number of arguments, _TCHAR* argument list
 * @return int 0
 * @desc Main run method of the program
 */
int _tmain(int argc, _TCHAR* argv[]) {
	TransactionsHelper th;

	//fsh.readBankAccountFile();
	std::cout << "login - start a Front End session" << std::endl;
	std::cout << "withdrawal - withdraw money from a bank account" << std::endl;
	std::cout << "transfer - transfer money from one bank account to another" 
		<< std::endl;
	std::cout << "paybill - pay a bill from a bank account" << std::endl;
	std::cout << "deposit - deposit money into a bank account" << std::endl;
	std::cout << "create - create a new bank account" << std::endl;
	std::cout << "delete - delete a bank account" << std::endl;
	std::cout << "disable - disable all transactions in a bank account" 
		<< std::endl;
	std::cout << "changeplan - change the transaction plan of a bank account" 
		<< std::endl;
	std::cout << "logout - end a Front End session" << std::endl;
	std::cout << "\n" << std::endl;

	std::string command;

	std::cout << "Enter command: ";
	std::cin >> command;
	while(command != "exit")
	{
		if(command == "login")
		{
			th.processLogin();
		} else if(command == "logout") {
			th.processLogout();
		} else if(command == "withdrawal") {
			th.processWithdrawal();
		} else if(command == "paybill") {
			th.processPaybill();
		} else if(command == "transfer") {
			th.processTransfer();
		} else if(command == "deposit") {
			th.processDeposit();
		} else if(command == "create") { 
			th.processCreate();
		} else if(command == "changeplan") { 
			th.processChangePlan();
		} else if(command == "enable") { 
			th.processEnable();
		} else if(command == "disable") { 
			th.processDisable();
		} else if(command == "transfer") {
			th.processTransfer();
		} else if(command == "delete") {
			th.processDelete();
		} else {
		}

		std::cout << "Enter new command: ";
		std::cin >> command;
	}

	system("pause");
	return 0;
}

