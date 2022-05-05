# Bulk DML Example

The purpose of this project is to do some comparisons
on bulk inserts between Go and Java. It consists of
a PostgreSQL database running in a container and
a Java application that loads some data into it. A
Go application is forthcoming.

If you have Docker installed, you can run the
command `./run_db.sh` to build and run
the database container. It will expose the database
on port 5432. The database is configured with a farily
high level of debug logging. You can increase the
logging by editing the [Dockerfile](./Dockerfile).

As this is built for experimentation, how you run the
Java application is up to you. I designed around
running the applicaton in a debugger and watching
the logs in the database container as I stepped through.
That said, if you have Java installed, you can run the
application on the command line with the command
`./gradlew deployableJar && java -jar ./build/libs/bulk-dml-java-deploy.jar`

