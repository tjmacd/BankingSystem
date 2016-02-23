// FlippinFlapJacks.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include "FileStreamHelper.h"
#include "TransactionsHelper.h"

using namespace std;


int _tmain(int argc, _TCHAR* argv[]) {
	TransactionsHelper th;

	//fsh.readBankAccountFile();
	cout << "login - start a Front End session" << endl;
	cout << "withdrawal - withdraw money from a bank account" << endl;
	cout << "transfer - transfer money from one bank account to another" << endl;
	cout << "paybill - pay a bill from a bank account" << endl;
	cout << "deposit - deposit money into a bank account" << endl;
	cout << "create - create a new bank account" << endl;
	cout << "delete - delete a bank account" << endl;
	cout << "disable - disable all transactions in a bank account" << endl;
	cout << "changeplan - change the transaction plan of a bank account" << endl;
	cout << "logout - end a Front End session" << endl;
	cout << "\n" << endl;

	string command;

	cout << "Enter command: ";
	cin >> command;
	while(command != "exit")
	{
		if(command == "login")
		{
			th.processLogin();
		} 
		else if(command == "logout")
		{
			th.processLogout();
		}
		else if(command == "withdrawal")
		{
			th.processWithdrawal();
		}
		else if(command == "paybill")
		{
			th.processPaybill();
		}
		else {
		}

		cout << "Enter new command: ";
		cin >> command;
	}

	system("pause");
	return 0;
}

