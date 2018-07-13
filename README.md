# FlippinFlapJacks

___

Usage:
frontend [accounts file] [transaction file]
backend [mergedTransactionFile] [AccountsFile]

### Transactions
Transactions marked * require admin privileges
| Code | Transaction | Description |
| -----|-------------|-------------|
| 00   | logout      | ends session|
| 01   | withdrawal  | withdraws money from account |
| 02   | transfer    | transfer money between two accounts |
| 03   | paybill | pay bill with money from account; bills can be payed to EC, CQ, or TV |
| 04   | deposit | deposit money into account |
| 05   | create* | create new bank account with an initial balance |
| 06   | delete* | delete a bank account |
| 07   | disable* | disable a bank account |
| 08   | changeplan* | toggles account type between Student (S) and Non-student (N) |
| 09   | enable*      | enables bank account |
| 10   | login | starts session|