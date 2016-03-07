#include "stdafx.h"
#include "AccountHelper.h"

FileStreamHelper *file_stream_helper;

AccountHelper::AccountHelper(std::string accounts_file)
{
	file_stream_helper = new FileStreamHelper(accounts_file, "");
}


AccountHelper::~AccountHelper(void)
{
}

bool AccountHelper::validateAccount(int account_num, std::string accountName) {
	file_stream_helper->readBankAccountFile();
	for(int i = 0; i < file_stream_helper->accounts.size(); i++)
	{
		if(file_stream_helper->accounts[i].number == account_num)
		{
			if(file_stream_helper->accounts[i].name == accountName)
			{
				return true;
			}
		}
	}
	return false;
}

bool AccountHelper::validateWithdrawAmount(int id, float amount, bool is_admin)
{
	if(is_admin) {
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

Accounts AccountHelper::getAccount(int id) {
	Accounts a;
	file_stream_helper->readBankAccountFile();
	for(int i = 0; i < file_stream_helper->accounts.size(); i++)
	{
		if(file_stream_helper->accounts[i].number == id)
		{
			a.name = file_stream_helper->accounts[i].name;
			a.balance = file_stream_helper->accounts[i].balance;
			a.is_active = file_stream_helper->accounts[i].is_active;
			a.number = file_stream_helper->accounts[i].number;
		}
	}

	return a;
}

bool AccountHelper::validateaccount_number(int id)
{
	file_stream_helper->readBankAccountFile();
	for(int i = 0; i < file_stream_helper->accounts.size(); i++)
	{
		if(file_stream_helper->accounts[i].number == id)
		{
			return true;
		} else {
			return false;
		}
	}

	return false;
}

bool AccountHelper::changeStatus(int id, bool newState){
    file_stream_helper->readBankAccountFile();
	for(int i = 0; i < file_stream_helper->accounts.size(); i++)
	{
		if(file_stream_helper->accounts[i].number == id)
		{
			if(file_stream_helper->accounts[i].is_active != newState) {
				file_stream_helper->accounts[i].is_active = newState;
				return true;
			}
			else {
				return false;
			}
		}
	}
}

char AccountHelper::changePlan(int id)
{
	file_stream_helper->readBankAccountFile();
	for(int i = 0; i < file_stream_helper->accounts.size(); i++)
	{
		if(file_stream_helper->accounts[i].number == id)
		{
			if(file_stream_helper->accounts[i].is_student)
				file_stream_helper->accounts[i].is_student = false;
			else
				file_stream_helper->accounts[i].is_student = true;

			return (file_stream_helper->accounts[i].is_student ? 'S' : 'N');
		}
	}
}
