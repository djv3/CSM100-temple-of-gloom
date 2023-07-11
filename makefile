build:
	gradle build

test:
	gradle test

run:
	gradle run

clean:
	gradle clean

lint:
	gradle spotlessCheck

format:
	gradle spotlessApply
