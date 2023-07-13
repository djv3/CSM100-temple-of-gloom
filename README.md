# CSM100 The Temple of Gloom

This project is for Group M coursework two of UofL CSM100, named temple of gloom.

## How to work with this repo

Prerequisites: Java 20, Gradle 8.1.1
Note: Gradle 8.1.1 at the time of writing is the latest and has only limited support for Java 20.
It is recommended to use Java 17 to run Gradle itself, however the project will target Java 20.

### Building

To build the project, run `gradle build` or `make build` in the root directory of the project.

### Running

To run the project, run `gradle run` or `make run` in the root directory of the project.

To run Temple-Of-Gloom use options -s and -n
`gradle run --args='-s 1050 -n 10`

### Testing

To run the tests, run `gradle clean test` or `make test` in the root directory of the project.
Note: the default test command excludes the regression test and the test that compares against
league table results. To run these as well, use `make testAll` or `gradle clean testAll`. Clean
is required to ensure that the cache is cleared and the tests are run from scratch.

### Formatting

To format the code, run `gradle spotlessApply` or `make format` in the root directory of the project.

### Linting

To lint the code, run `gradle spotlessCheck` or `make lint` in the root directory of the project.

### Documentation

The coursework brief has been copied from the codio guides section as I find it easier to read in markdown format and across one page. The file can be found at [BRIEF.md](BRIEF.md).
