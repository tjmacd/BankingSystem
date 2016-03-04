#include <iostream>
#include <fstream>
#include <vector>
#ifndef FLIPPINFLAPJACKS_ACCOUNTHELPER_H
#define FLIPPINFLAPJACKS_ACCOUNTHELPER_H
/*
 * @class AccountHelper
 * @desc Contains methods to process account information
 */
class AccountHelper {

private:

public:
	AccountHelper();
	bool validateAccount(int num, std::string name);
	bool validateWithdrawAmount(int id, float balance, bool is_admin);
	struct Accounts getAccount(int id);
	char changePlan(int id);
	bool enableAccount(int id);
	bool disableAccount(int id);
	bool validateaccount_number(int id);
};
#endif // FLIPPINFLAPJACKS_ACCOUNTHELPER_H