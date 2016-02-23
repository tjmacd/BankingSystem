#include <iostream>
#include <fstream>
#include <vector>

class AccountHelper {

private:

public:
	AccountHelper();
	bool validateAccount(int num, std::string name);
	bool validateWithdrawAmount(float balance);
	Accounts getAccount(int id);
};
