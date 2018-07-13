compile:
	(cd backend; make)
	(cd frontend; make)

run:
	./week.sh

clean:
	(cd backend; make clean)
	(cd frontend; make clean)