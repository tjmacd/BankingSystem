#ifndef BANKINGSYSTEM_ACCOUNTHELPER_H
#define BANKINGSYSTEM_ACCOUNTHELPER_H

#pragma once

#include <string>
#include "FileStreamHelper.h"

class AccountHelper
{
private:

public:
	AccountHelper(std::string accounts);
	~AccountHelper(void);

	bool validateAccount(int account_num, std::string accountName);
	bool validateWithdrawAmount(int id, float balance, bool is_admin);
	struct Accounts getAccount(int id);
	bool validateAccountNumber(int id);
	bool changeStatus(int id, bool newState);
	char changePlan(int id);
};
#endif // BANKINGSYSTEM_ACCOUNTHELPER_H
