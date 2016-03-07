#ifndef BANKINGSYSTEM_TRANSACTIONS_HELPER_H
#define BANKINGSYSTEM_TRANSACTIONS_HELPER_H
#pragma once

#include <string>
#include "FileStreamHelper.h"
#include "AccountHelper.h"

class TransactionsHelper
{
private:
	bool is_logged_in;
	bool is_admin;
	std::string account_holder_name;
	int account_holder_number;
	void setStatus(bool enabled);
	void getName();
	bool getNumber();

public:
	TransactionsHelper(std::string accounts, std::string output);
	~TransactionsHelper(void);
	void processLogin();
	void processLogout();
	void processWithdrawal();
	void processPaybill();
	void processDeposit();
	void processCreate();
	void processDelete();
	void processDisable();
	void processChangePlan();
	void processTransfer();
	void processEnable();
};
#endif // BANKINGSYSTEM_TRANSACTIONS_HELPER_H
