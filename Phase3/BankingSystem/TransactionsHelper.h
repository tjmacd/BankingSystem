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

public:
	TransactionsHelper(std::string accounts, std::string output);
	~TransactionsHelper(void);
	void TransactionsHelper::processLogin();
	void TransactionsHelper::processLogout();
	void TransactionsHelper::processWithdrawal();
	void TransactionsHelper::processPaybill();
	void TransactionsHelper::processDeposit();
	void TransactionsHelper::processCreate();
	void TransactionsHelper::processDelete();
	void TransactionsHelper::processDisable();
	void TransactionsHelper::processChangePlan();
	void TransactionsHelper::processTransfer();
	void TransactionsHelper::processEnable();
};
#endif // BANKINGSYSTEM_TRANSACTIONS_HELPER_H