#!/bin/bash

count=1

echo "Running Daily Script"

if [ -z "$1" -o -z "$2" -o -z "$3" ]; then
  echo "Invalid argument. Format <day> <master file> <current file>"
  exit
fi;

day="$1"
master="$2"
current="$3"

#cp $master $day/masterAccounts.txt
#cp $current $day/currentAccounts.txt

cd $day

for input in $(ls | grep input);
do
  echo $input
  ../../frontend $current transaction_"$count".log < $input
  cat transaction_*.log >> merged_transactions.txt
  rm transaction_*.log
  count=$(($count+1))
done

#cp currentAccounts.txt ../$current
#cp masterAccounts.txt ../$master
sudo java -cp ../../.class Controller currentAccounts.txt merged_transactions.txt masterAccounts.txt
