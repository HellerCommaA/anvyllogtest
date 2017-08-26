## Anvyl Log Parsing



To build: `mvn package`

cmd line args: date [path to log location]

Date is required with a YYYY-MM-DD format date

path to log location (relative and absolute are both ok) is an optional location of log files if they are located in another directory

###### Note: default location, if not specified, assumes .jar will be run from same location as ./logs (IE: if you `mvn package` then attempt to run the jar, it will not work. `mv` the jar up a directory or specify a location) running from ./test: `$ java -jar AnvylTest-0.0.1-jar-with-dependencies.jar 2017-07-18 ../logs`