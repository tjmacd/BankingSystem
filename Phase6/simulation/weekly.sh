#!/bin/bash

for day in $(ls -d daily/* | grep day_);
do
  cp masterAccounts.txt day_1/masterAccounts.txt
  cp currentAccounts.txt day_1/currentAccounts.txt
  cd $day
  ../../daily.sh ./ masterAccounts.txt currentAccounts.txt
  cd ../../
done
