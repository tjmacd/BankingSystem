#ifndef BANKINGSYSTEM_ACCOUNTHELPER_H
#define BANKINGSYSTEM_ACCOUNTHELPER_H

#pragma once

#include <string>
#include "FileStreamHelper.h"

// Contains methods to process account information
class AccountHelper
{
private:

public:
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
	// Changes the enabled state of the account to new_state. Returns false if
	// account is already in that state
	bool changeStatus(int id, bool new_state);
	// Toggles between student and non-student account. Returns 'S' if changed
	// to student and 'N' if changed to non-student
	char changePlan(int id);
};
#endif // BANKINGSYSTEM_ACCOUNTHELPER_H