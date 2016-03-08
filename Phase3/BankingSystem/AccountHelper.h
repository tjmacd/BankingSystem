#ifndef BANKINGSYSTEM_ACCOUNTHELPER_H
#define BANKINGSYSTEM_ACCOUNTHELPER_H

#pragma once

#include <string>
#include "FileStreamHelper.h"

// Contains methods to process account information
class AccountHelper
{
private:
	// List of all the accounts
	std::vector<Account> accounts;

public:
    const float MAX_AMOUNT = 99999.99;
		const float MAX_TRANSFER = 1000.00;

    // Takes the current accounts file name as a parameter
	AccountHelper(std::string accounts_file);
	~AccountHelper(void);
    // Returns true if account_num is an account belonging to accountName
	bool validateAccount(int account_num, std::string account_name);
	// Returns true if amount can be safely withdrawn from account
	bool validateWithdrawAmount(int id, float amount, bool is_admin);
	// Returns an the account with the given id
	struct Account getAccount(int id);
	// Returns true if an account with the given id exists
	bool validateAccountNumber(int id);
	bool validateAccountHolderName(std::string name);
	bool isAccountActive(int id);
	// Changes the enabled state of the account to new_state. Returns false if
	// account is already in that state
	bool changeStatus(int id, bool new_state);
	// Toggles between student and non-student account. Returns 'S' if changed
	// to student and 'N' if changed to non-student
	char changePlan(int id);

	float getFee(Account account);
	bool deposit(int id, float amount);
	bool deleteAccount(int id);
	bool transferAmount(int fromAccount, int toAccount, float amount);
};
#endif // BANKINGSYSTEM_ACCOUNTHELPER_H
