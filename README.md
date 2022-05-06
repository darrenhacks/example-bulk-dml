# Bulk DML Example

The purpose of this project is to do some comparisons
on bulk inserts between Go and Java. It consists of
a PostgreSQL database running in a container and
Java and Go applications that loads some data into it.

If you have Docker installed, you can run the
command `./run_db.sh` to build and run
the database container. It will expose the database
on port 5432. The database is configured with a farily
high level of debug logging. You can increase the
logging by editing the [Dockerfile](./Dockerfile).

As this projce was meant for experimentation, how you run the
Java and Go applications is up to you. I designed around
running the applicatons in a debugger and watching
the logs in the database container as I stepped through.

That said...

If you have Java installed, you can run the
Java application on the command line by changing to the java directory
and running  the command
`./gradlew deployableJar && java -jar ./build/libs/bulk-dml-java-deploy.jar`.

If you have a Go compier installed, you can run the Go
application by changing to the go directory and runnig the command
`make && ./example-bulk-dml`.

