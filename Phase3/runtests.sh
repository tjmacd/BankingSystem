cd tests/input
for file in *
do
	test="${file%.txt}"
	echo “running test $test”
	../../BankingSystem.exe currentAccounts.txt ../transactionFiles/$test.atf < $file > ../consoleOutput/$test.out
done

#for file in tests/consoleOutput
#do
#	echo "checking outputs of test $file"
#done
	
