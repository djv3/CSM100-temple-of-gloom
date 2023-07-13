build:
	gradle build

test:
	gradle clean test

testAll:
	gradle clean testAll

run:
	gradle run

clean:
	gradle clean

lint:
	gradle spotlessCheck

format:
	gradle spotlessApply
