# Banking System
A simulated banking consisting of a frontend in C++ and a backend in Java.

## Design
The frontend is text based and takes text commands and instructions. It reads a text file containing current account information and outputs a text file containing codes for each transaction.

This transaction file is then taken by the backend and the transactions are performed on a master set of accounts (if possible) and a new master bank accounts file is created.

## Usage

`frontend [accounts file] [transaction file]`

`backend [mergedTransactionFile] [AccountsFile]`

Running `./week.sh` simulates a week of transactions on the system, both frontend and backend.

## Testing
Quality Assurance testing was done throughout the development. Continuous blackbox requirements tests were performed on the frontend to ensure that all requirements continued to be met, comparing desired output with actual output. These were automated using the script `runtests.sh`.

**Junit** was used for whitebox testing on the backend.

## Transactions and Codes
Transactions marked \* require admin privileges

| Code | Transaction | Description |
| -----|-------------|-------------|
| 00   | logout      | ends session|
| 01   | withdrawal  | withdraws money from account |
| 02   | transfer    | transfer money between two accounts |
| 03   | paybill | pay bill with money from account; bills can be payed to EC, CQ, or TV |
| 04   | deposit | deposit money into account |
| 05   | create\* | create new bank account with an initial balance |
| 06   | delete\* | delete a bank account |
| 07   | disable\* | disable a bank account |
| 08   | changeplan\* | toggles account type between Student (S) and Non-student (N) |
| 09   | enable\*      | enables bank account |
| 10   | login | starts session|
