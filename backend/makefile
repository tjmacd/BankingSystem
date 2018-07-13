compile:
	rm -rf bin/
	mkdir bin/
	javac -cp bin/:junit.jar -d bin/ src/helpers/*.java
	javac -cp bin/.:junit.jar -d bin/ src/*.java

run: compile
	java Server

clean:	
	rm -rf bin/