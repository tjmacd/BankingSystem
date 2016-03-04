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

	bool AccountHelper::validateAccount(int account_num, std::string accountName);
	bool validateWithdrawAmount(int id, float balance, bool is_admin);
	struct Accounts getAccount(int id);
	bool validateaccount_number(int id);
	bool AccountHelper::disableAccount(int id);
	bool AccountHelper::enableAccount(int id);
	char AccountHelper::changePlan(int id);
};
#endif // BANKINGSYSTEM_ACCOUNTHELPER_H