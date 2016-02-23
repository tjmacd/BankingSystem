#include <vector>
#include "AccountHelper.h"

class TransactionsHelper {

private:
	bool isLoggedIn;
	bool isAdmin;
	std::string accountHolderName;
	int accountHolderNumber;
	AccountHelper ah;

public:
	TransactionsHelper();

	bool initSession();

	void processLogin();

	void processLogout();

	void processWithdrawal();

	void processPaybill();

	void processDeposit();

	void processCreate();

	void processDelete();

	void processDisable();

	void processChangePlan();
        
        void processEnable();
};
