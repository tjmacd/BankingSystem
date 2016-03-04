#ifndef BANKINGSYSTEM_ACCOUNTS_H
#define BANKINGSYSTEM_ACCOUNTS_H
#include <iostream>
#include <string>

struct Accounts {

public:
	int number;
	std::string name;
	bool is_active;
	float balance;
	bool is_student;
};

#endif // BANKINGSYSTEM_ACCOUNTS_H
