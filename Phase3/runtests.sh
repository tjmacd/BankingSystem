## Remove old test files
rm tests/consoleOutput/* tests/transactionFiles/*

## Run tests
cd tests/input
for file in *
do
	test="${file%.txt}"
	echo “running test $test”
	../../BankingSystem/BankingSystem currentAccounts.txt ../transactionFiles/$test.atf < $file > ../consoleOutput/$test.out
done
cd ..

## Compare results with expected results
successes=0 # Number of tests passed
fails=0     # Number of tests failed
log=../testlog.txt  # File to store the difference between the expected and actual outputs
rm $log  # Wipe old log file
for file in $(ls consoleOutput/)
do
	test="${file%.out}"                        # name of test
	cOut=consoleOutput/$test.out               # console output
	eCOut=expectedConsoleOutput/$test.out      # expected console output
	tFile=transactionFiles/$test.atf           # transaction file
	eTFile=expectedTransactionFiles/$test.atf  # expected transaction file

	echo -n "Checking outputs of test $test: "
	consoleDiff=$(diff -N -q -Z $cOut $eCOut) # Empty string if files are the same
	transDiff=$(diff -N -q -Z $tFile $eTFile) # Empty string if files are the same

    if [ "$consoleDiff" = "" -a "$transDiff" = "" ]; then
		printf "\033[1;32mPassed!\n\033[0m" # Green coloured output
		successes=$((successes+1))
	else
		printf "\033[1;31mFailed\n\033[0m" # Red coloured output
		fails=$((fails+1))
		# Write differences to file
		if [ "$consoleDiff" != "" ]; then
			echo $cOut / $eCOut >> $log
			diff -N -Z $cOut $eCOut >> $log
			echo "\n" >> $log
		fi
		if [ "$transDiff" != "" ]; then
			echo $tFile / $eTFile >> $log
			diff -N -Z $tFile $eTFile >> $log
			echo "\n" >> $log
		fi
	fi
done
echo "$successes passed, $fails failed; check testlog.txt for details"
	
