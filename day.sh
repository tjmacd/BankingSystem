
# variables
day="$1"
current="$2"
dayfolder="inputs/$day/"
number=1

mkdir -p transactionLogs/
mkdir -p transactionLogs/$day

echo " ======= Start $day ======="
rm transactionLogs/$day/*
for file in $(ls $dayfolder);
do
	echo " ======= $file ======="
	# run frontend on each input file, creating a new transaction log for each
	cat $dayfolder$file | frontend/frontend $current transactionLogs/$day/"transactions_log_"$number".txt"
	number=$((number+1))
	echo ""
done

# Merge transaction logs
> "transaction_log_$day.txt"
for file in $(ls transactionLogs/$day);
do
	cat transactionLogs/$day/$file >> "transaction_log_$day.txt"
done

# now, run the server
java -cp backend/bin BankingSystemBackend "transaction_log_$day.txt" "master_accounts_work.txt"
mv transaction_log_$day.txt transactionLogs/
echo ""
