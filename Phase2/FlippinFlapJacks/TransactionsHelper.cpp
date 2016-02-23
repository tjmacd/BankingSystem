#include "stdafx.h"
#include <iostream>
#include <string>
#include "TransactionsHelper.h"
#include "FileStreamHelper.h"

using namespace std;

TransactionsHelper::TransactionsHelper() {
    isLoggedIn = false;
    ah = AccountHelper();
}

bool TransactionsHelper::initSession() {
    // TODO - implement TransactionsHelper::initSession
    throw "Not yet implemented";
}

void TransactionsHelper::processLogin() {
    string sessionType;

    if(!isLoggedIn)
    {
            cout << "Enter session type: (standard or admin) ";
            cin >> sessionType;

            if(sessionType == "admin")
            {
                    isAdmin = true;
                    cout << "Logged in as Admin" << endl;
            }
            else
            {
                    isAdmin = false;
                    cout << "Logged in as Standard" << endl;

                    cout << "Enter Account holder's name: ";
                    cin >> accountHolderName;
            }

            isLoggedIn = true;

            FileStreamHelper fsh;
            fsh.readBankAccountFile();
    }
    else
    {
            cout << "Already Logged in" << endl;
    }
}

void TransactionsHelper::processLogout() {
    if(isLoggedIn)
    {
            cout << "Logging out..." << endl;
            isLoggedIn = false;
    }
    else
    {
            cout << "Not logged in! Please login first!" << endl;
    }
}

void TransactionsHelper::processWithdrawal() {
    float toWithdraw;

    if(isLoggedIn) {
            if(isAdmin) {
                    cout << "Enter Account holder's name: ";
                    cin >> accountHolderName;
            }

            cout << "Enter Account holder's number: ";
            cin >> accountHolderNumber;

            if(ah.validateAccount(accountHolderNumber, accountHolderName))
            {
                    cout << "Account Found! Please enter amount to withdraw: ";
                    cin >> toWithdraw;

                    ah.validateWithdrawAmount(accountHolderNumber, toWithdraw, isAdmin);
            }
            else
            {
                    cout << "Account not found!" << endl;
            }
    } else {
            cout << "Not logged in! Please login!" << endl;
    }
}

void TransactionsHelper::processPaybill() {
    if(isLoggedIn) {
        string company;
        int number;
        float amount;
            if(isAdmin) {
                    cout << "Enter Account holder's name: ";
                    cin >> accountHolderName;
            }

            cout << "Enter the account number:" << endl;
    cin >> number;

            if(ah.validateAccount(accountHolderNumber, accountHolderName)) {
                    Accounts account = AccountHelper::getAccount(number);
                    cout << "Enter the payee company:" << endl;
                    cin >> company;

                    if(!(company == "EC" || company == "CQ" || company == "TV")) {
                    cout << "Company name is not recognized." << endl;
                    return;
                    }

                    cout << "Enter the amount to pay: " << endl;
            cin >> amount;

            FileStreamHelper::logTransaction("03", accountHolderName, account.num, amount, company);
            }
            else
            {
                    cout << "Account not found!" << endl;
            }
    } else {
            cout << "Not logged in! Please login!" << endl;
    }
}

void TransactionsHelper::processDeposit() {
    if(isLoggedIn) {
        int number;
        float amount;
        if(isAdmin){
            cout << "Enter the account holder's name:" << endl;
            cin >> accountHolderName;
        }
        cout << "Enter the account number:" << endl;
        cin >> number;
        Accounts account = AccountHelper::getAccount(number);
        cout << "Enter the amount to deposit:" << endl;
        cin >> amount;

        logTransaction("04", accountHolderName, account.num, amount, "");
    } else {
            cout << "Not logged in! Please login!" << endl;
    }
}

void TransactionsHelper::processCreate() {
    if(isLoggedIn) {
        if(isAdmin){
            float balance;
            cout << "Enter the account holder's name:" << endl;
            cin >> accountHolderName;

            cout << "Enter the initial balance:" << endl;
            cin >> balance;

            cout << "Account creation pending" << endl;

            logTransaction("05", accountHolderName, 0, balance, "");
        } else {
            cout << "Permission denied. Only admin can use this command" << endl;
        } 
    } else {
            cout << "Not logged in! Please login!" << endl;
    }
}

void TransactionsHelper::processDelete() {
    // TODO - implement TransactionsHelper::processDelete
    throw "Not yet implemented";
}

void TransactionsHelper::processDisable() {
    // TODO - implement TransactionsHelper::processDisable
    throw "Not yet implemented";
}

void TransactionsHelper::processChangePlan() {
    // TODO - implement TransactionsHelper::processChangePlan
    throw "Not yet implemented";
}