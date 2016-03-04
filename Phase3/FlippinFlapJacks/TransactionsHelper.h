#ifndef FLIPPINFLAPJACKS_TRANSACTIONS_HELPER_H
#define FLIPPINFLAPJACKS_TRANSACTIONS_HELPER_H
#include <vector>
#include "AccountHelper.h"

/*
 * @class TransactionsHelper
 * @desc Contains method to process transaction
 */
class TransactionsHelper {

private:
	bool is_logged_in;
	bool is_admin;
	std::string account_holder_name;
	int account_holder_number;
	AccountHelper ah;

public:
	TransactionsHelper();
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
#endif // FLIPPINFLAPJACKS_TRANSACTIONS_HELPER_H