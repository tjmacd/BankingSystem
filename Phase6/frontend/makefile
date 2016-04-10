make:
	g++ -std=c++11  -c src/AccountHelper.cpp -o src/.objs/AccountHelper.o
	g++ -std=c++11  -c src/BankingSystem.cpp -o src/.objs/BankingSystem.o
	g++ -std=c++11  -c src/FileStreamHelper.cpp -o src/.objs/FileStreamHelper.o
	g++ -std=c++11  -c src/stdafx.cpp -o src/.objs/stdafx.o
	g++ -std=c++11  -c src/TransactionsHelper.cpp -o src/.objs/TransactionsHelper.o
	g++  -o src/BankingSystem src/.objs/AccountHelper.o src/.objs/BankingSystem.o src/.objs/FileStreamHelper.o src/.objs/stdafx.o src/.objs/TransactionsHelper.o
	@mv src/BankingSystem ../simulation/frontend
	@rm src/.objs/*
	@clear
	@echo "Finished building frontend"
