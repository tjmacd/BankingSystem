#include "stdafx.h"
#include <iostream>
#include <string>
#include <regex>
#include "FileStreamHelper.h"
#include "AccountHelper.h"

using namespace std;

AccountHelper::AccountHelper()
{

}

bool AccountHelper::validateAccount(int accountNum, string accountName) {
	FileStreamHelper fsh;
	fsh.readBankAccountFile();
	for(int i = 0; i < fsh.accounts.size(); i++)
	{
		if(fsh.accounts[i].num == accountNum)
		{
			if(fsh.accounts[i].name == accountName)
			{
				return true;
			}
		}
	}
	return false;
}

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

bool AccountHelper::validateWithdrawAmount(float amount)
{

}