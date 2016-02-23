#include "stdafx.h"
#include <iostream>
#include <string>
#include "TransactionsHelper.h"
#include "FileStreamHelper.h"

using namespace std;

TransactionsHelper::TransactionsHelper() {
	isLoggedIn = false;
	ah = AccountHelper();
}

bool TransactionsHelper::initSession() {
	// TODO - implement TransactionsHelper::initSession
	throw "Not yet implemented";
}

void TransactionsHelper::processLogin() {
	string sessionType;

	if(!isLoggedIn)
	{
		cout << "Enter session type: (standard or admin) ";
		cin >> sessionType;

		if(sessionType == "admin")
		{
			isAdmin = true;
			cout << "Logged in as Admin" << endl;
		}
		else
		{
			isAdmin = false;
			cout << "Logged in as Standard" << endl;

			cout << "Enter Account holder's name: ";
			cin >> accountHolderName;
		}

		isLoggedIn = true;
		
		FileStreamHelper fsh;
		fsh.readBankAccountFile();
	}
	else
	{
		cout << "Already Logged in" << endl;
	}
}

void TransactionsHelper::processLogout() {
	if(isLoggedIn)
	{
		cout << "Logging out..." << endl;
		isLoggedIn = false;
	}
	else
	{
		cout << "Not logged in! Please login first!" << endl;
	}
}

void TransactionsHelper::processWithdrawal() {
	float toWithdraw;

	if(isLoggedIn) {
		if(isAdmin) {
			cout << "Enter Account holder's name: ";
			cin >> accountHolderName;
		}

		cout << "Enter Account holder's number: ";
		cin >> accountHolderNumber;

		if(ah.validateAccount(accountHolderNumber, accountHolderName))
		{
			cout << "Account Found! Please enter amount to withdraw: ";
			cin >> toWithdraw;

			ah.validateWithdrawAmount(accountHolderNumber, toWithdraw, isAdmin);
		}
		else
		{
			cout << "Account not found!" << endl;
		}
	} else {
		cout << "Not logged in! Please login!" << endl;
	}
}

void TransactionsHelper::processPaybill() {
	if(isLoggedIn) {
		if(isAdmin) {
			cout << "Enter Account holder's name: ";
			cin >> accountHolderName;
		}

		cout << "Enter Account holder's number: ";
		cin >> accountHolderNumber;

		if(ah.validateAccount(accountHolderNumber, accountHolderName))
		{
			
		}
		else
		{
			cout << "Account not found!" << endl;
		}
	} else {
		cout << "Not logged in! Please login!" << endl;
	}
}

void TransactionsHelper::processDeposit() {
	// TODO - implement TransactionsHelper::processDeposit
	throw "Not yet implemented";
}

void TransactionsHelper::processCreate() {
	// TODO - implement TransactionsHelper::processCreate
	throw "Not yet implemented";
}

void TransactionsHelper::processDelete() {
	// TODO - implement TransactionsHelper::processDelete
	throw "Not yet implemented";
}

void TransactionsHelper::processDisable() {
	// TODO - implement TransactionsHelper::processDisable
	throw "Not yet implemented";
}

void TransactionsHelper::processChangePlan() {
	// TODO - implement TransactionsHelper::processChangePlan
	throw "Not yet implemented";
}