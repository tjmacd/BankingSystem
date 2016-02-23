#include <iostream>
#include <fstream>
#include <vector>

class AccountHelper {

private:

public:
	AccountHelper();
	bool validateAccount(int num, std::string name);
	bool validateWithdrawAmount(int id, float balance, bool isAdmin);
	struct Accounts getAccount(int id);
};
