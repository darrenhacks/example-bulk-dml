package dev.darrenhacks.bulkdml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

/**
 * Demonstrates bulk loading of data into a relational database.
 */
public class App {

    private static final int START = 1;
    private static final int BATCH_SIZE = 20;

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bulk_dml_db", "postgres", "p0stgr@s")) {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement("insert into bulk_dml_schema.accounts (account_id, account_name) values (?, ?)")) {

                int rowsAdded = 0;
                Instant start = Instant.now();

                // Generates data and sends it to the database to be persisted.
                boolean unProcessedRows = false;
                for (int i = START; i <= START + 1_364; i++) {

                    unProcessedRows = true;

                    // Tell the driver to hold on to the data until we've built a full batch.
                    String accountName = String.format("ACCOUNT %d", i);
                    preparedStatement.setString(1, UUID.randomUUID().toString());
                    preparedStatement.setString(2, accountName);
                    preparedStatement.addBatch();

                    // This will tell the driver to send the batch to the database.
                    if (i % BATCH_SIZE == 0) {
                        unProcessedRows = false;
                        int rowsInserted = Arrays.stream(preparedStatement.executeBatch()).sum();
                        rowsAdded += rowsInserted;
                        System.out.printf("%d rows inserted.%n", rowsInserted);
                    }
                }

                // If there was anything we haven't sent over to the database, do
                // so before calling commit.
                if (unProcessedRows) {
                    int rowsInserted = Arrays.stream(preparedStatement.executeBatch()).sum();
                    rowsAdded += rowsInserted;
                    System.out.printf("%d rows inserted.%n", rowsInserted);
                }
                connection.commit();

                Instant end = Instant.now();

                long runTime = Duration.between(start, end).toMillis();
                System.out.printf("%,d rows inserted in %,d milliseconds.", rowsAdded, runTime);

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
