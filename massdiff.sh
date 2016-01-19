#!/bin/sh
echo Filename: $1
for i in `ls -a *.*`
do
	DIFF=$(diff $i $1)
	if [ "$DIFF" != "" ]; then
		`echo `diff $i $1 >> mass_diff_log.txt``	
	else
		echo "$i and $1 are the same"
	fi
done
