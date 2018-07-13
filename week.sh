#!/bin/bash

# create a copy of the current and master accounts files for the week to run on
cp "current_accounts_work.txt" "current_accounts.txt"
cp "master_accounts_work.txt" "master_accounts.txt"

mkdir -p transactionLogs
mkdir -p masterAccountFiles

inputs="inputs/"

for folder in $(ls $inputs)
do
	./day.sh $folder "current_accounts.txt"
	
	#save the master bank account folder for each days
	cp masterBankAccountsFile.txt masterAccountFiles/"master_accounts_$folder.txt"
done

#get rid of extra files
rm master_accounts.txt current_accounts.txt
