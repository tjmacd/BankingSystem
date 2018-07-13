make:
	rm -rf .objs/
	mkdir .objs/
	g++ -std=c++11  -c ./src/AccountHelper.cpp -o .objs/AccountHelper.o
	g++ -std=c++11  -c ./src/BankingSystem.cpp -o .objs/BankingSystem.o
	g++ -std=c++11  -c ./src/FileStreamHelper.cpp -o .objs/FileStreamHelper.o
	g++ -std=c++11  -c ./src/stdafx.cpp -o .objs/stdafx.o
	g++ -std=c++11  -c ./src/TransactionsHelper.cpp -o .objs/TransactionsHelper.o
	g++  -o frontend .objs/AccountHelper.o .objs/BankingSystem.o .objs/FileStreamHelper.o .objs/stdafx.o .objs/TransactionsHelper.o    

clean:
	rm -f .objs/* frontend
