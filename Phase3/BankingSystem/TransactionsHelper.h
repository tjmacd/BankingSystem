#ifndef BANKINGSYSTEM_TRANSACTIONS_HELPER_H
#define BANKINGSYSTEM_TRANSACTIONS_HELPER_H
#pragma once

#include <string>
#include "FileStreamHelper.h"
#include "AccountHelper.h"
#include <regex>
#include <iomanip>

// Contains methods to process transactions
class TransactionsHelper
{
private:
    // Keeps track of whether someone is logged in
	bool is_logged_in;
	// Keeps track of whether an admin is logged in
	bool is_admin;
	// Name of the current account holder for the transaction
	std::string account_holder_name;
	// Account number for the transaction
	int account_holder_number;

	// Sets the account status to enabled or disabled
	void setStatus(bool enabled);
	// Prompts user for account holder name and reads it in
	void getName();
	// Prompts for, reads in, and validates account number against the account
	// holder
	bool getNumber();
	// Prints a message and returns false if no user is logged in
	bool checkLoggedIn();
	// Prints a message and returns false if user is not privileged
	bool checkPrivileged();
	// Outputs a message and returns false if amount is not valid
	bool verifyInputAmount(std::string input, float &amount_output);

public:
    // Constructs TransactionsHelper with accounts filename and transaction
    // file output name as parameters
	TransactionsHelper(std::string accounts, std::string output);
	~TransactionsHelper(void);
	// Processes login transaction
	void processLogin();
	// Processes logout transaction
	void processLogout();
	// Processes withdrawal transaction
	void processWithdrawal();
	// Processes paybill transaction
	void processPaybill();
	// Processes deposit transaction
	void processDeposit();
	// Processes create transaction
	void processCreate();
	// Processes delete transaction
	void processDelete();
	// Processes disable transaction
	void processDisable();
	// Processes changeplan transaction
	void processChangePlan();
	// Processes transfer transaction
	void processTransfer();
	// Processes enable transaction
	void processEnable();
};
#endif // BANKINGSYSTEM_TRANSACTIONS_HELPER_H
