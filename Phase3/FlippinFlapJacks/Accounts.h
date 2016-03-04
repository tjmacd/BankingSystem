#ifndef FLIPPINFLAPJACKS_ACCOUNTS_H
#define FLIPPINFLAPJACKS_ACCOUNTS_H
#include <iostream>
#include <string>

/*
 * @class Accounts
 * @desc Data structure of accounts
 */
struct Accounts {

public:
	int num;
	std::string name;
	bool active;
	float balance;
	bool student;
};

#endif // FLIPPINFLAPJACKS_ACCOUNTS_H
