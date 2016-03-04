#include "stdafx.h"
#include <iostream>
#include <string>
#include <regex>
#include "FileStreamHelper.h"
#include "AccountHelper.h"

/*
 * @method AccountHelper
 * @params none
 * @return none
 * @desc Construct AccountHelper class
 */
AccountHelper::AccountHelper()
{

}

/*
 * @method validateAccount
 * @params int account number, string account name
 * @return false if account is not valid, true otherwise
 * @desc Validate Account using account name and its number
 */
bool AccountHelper::validateAccount(int account_num, std::string accountName) {
	FileStreamHelper fsh;
	fsh.readBankAccountFile();
	for(int i = 0; i < fsh.accounts.size(); i++)
	{
		if(fsh.accounts[i].num == account_num)
		{
			if(fsh.accounts[i].name == accountName)
			{
				return true;
			}
		}
	}
	return false;
}

/*
 * @method getAccount
 * @params int account id
 * @return Accounts structure of the account found during search
 * @desc Get the account information by account id
 */
Accounts AccountHelper::getAccount(int id) {
	Accounts a;
	FileStreamHelper fsh;
	fsh.readBankAccountFile();
	for(int i = 0; i < fsh.accounts.size(); i++)
	{
		if(fsh.accounts[i].num == id)
		{
			a.name = fsh.accounts[i].name;
			a.balance = fsh.accounts[i].balance;
			a.active = fsh.accounts[i].active;
			a.num = fsh.accounts[i].num;
		}
	}

	return a;
}

/*
 * @method validateWithdrawAmount
 * @params int account id, float amount, bool session type
 * @return boolean if the withdraw amount is valid
 * @desc Check for balance to process withdrawal and check its constraints
 */
bool AccountHelper::validateWithdrawAmount(int id, float amount, bool is_admin)
{
	if(is_admin)	{
		if(getAccount(id).balance < amount)
			std::cout << "Not enough balance!" << std::endl;
	} else {
		if(amount > 500)
		{
			std::cout << "You can only withdraw amount less than $500.00 on " <<
				"standard account" << std::endl;
		} else {
			if(getAccount(id).balance < amount)
				std::cout << "Not enough balance!" << std::endl;
		}
	}
	return false;
}

/*
 * @method changePlan
 * @params int account id
 * @return char account type
 * @desc Change the plan of student to non-student or vice versa
 */
char AccountHelper::changePlan(int id)
{
	FileStreamHelper fsh;
	fsh.readBankAccountFile();
	for(int i = 0; i < fsh.accounts.size(); i++)
	{
		if(fsh.accounts[i].num == id)
		{
			if(fsh.accounts[i].student)
				fsh.accounts[i].student = false;
			else
				fsh.accounts[i].student = true;

			return (fsh.accounts[i].student ? 'S' : 'N');
		} 
	}
}

/*
 * @method enableAccount
 * @params int account id
 * @return boolean true if account is enabled
 * @desc Enable an account by its id
 */
bool AccountHelper::enableAccount(int id)
{
	FileStreamHelper fsh;
	fsh.readBankAccountFile();
	for(int i = 0; i < fsh.accounts.size(); i++)
	{
		if(fsh.accounts[i].num == id)
		{
			if(fsh.accounts[i].active) {
				return false;
			}
			else {
				fsh.accounts[i].active = true;
				return true;
			}
		} 
	}
}

/*
 * @method disableAccount
 * @params int account id
 * @return boolean true if account is disabled
 * @desc Disable an account by its id
 */
bool AccountHelper::disableAccount(int id)
{
	FileStreamHelper fsh;
	fsh.readBankAccountFile();
	for(int i = 0; i < fsh.accounts.size(); i++)
	{
		if(fsh.accounts[i].num == id)
		{
			if(fsh.accounts[i].active) {
				fsh.accounts[i].active = false;
				return true;
			}
			else {
				return false;
			}
		} 
	}
}

/*
 * @method validateaccount_number
 * @params int account id
 * @return boolean true if the account number is valid
 * @desc Validate account number from the list of accounts in the file
 */
bool AccountHelper::validateaccount_number(int id)
{
	FileStreamHelper fsh;
	fsh.readBankAccountFile();
	for(int i = 0; i < fsh.accounts.size(); i++)
	{
		if(fsh.accounts[i].num == id)
		{
			return true;
		} else {
			return false;
		}
	}

	return false;
}