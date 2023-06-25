build:
	gradle build

test:
	gradle test --info

run:
	gradle run

clean:
	gradle clean

format:
	gradle spotlessApply
