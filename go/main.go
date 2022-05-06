package main

import (
	"database/sql"
	"fmt"
	"github.com/google/uuid"
	_ "github.com/jackc/pgx/v4/stdlib"
	"golang.org/x/text/language"
	"golang.org/x/text/message"
	"os"
	"time"
)

// Demonstrates bulk loading of data into a relational database.
func main() {

	// Connect to the DB, open a transaction.
	conn, err := sql.Open("pgx", "postgres://postgres:p0stgr@s@localhost:5432/bulk_dml_db")
	if err != nil {
		fmt.Println(err)
		os.Exit(-1)
	}
	defer conn.Close()

	trx, err := conn.Begin()
	if err != nil {
		fmt.Println(err)
		os.Exit(-1)
	}

	// Prepare the statement to do the inserts.
	stmt, err := trx.Prepare("insert into bulk_dml_schema.accounts (account_id, account_name) values ($1, $2)")
	if err != nil {
		fmt.Println(err)
		os.Exit(-1)
	}
	defer stmt.Close()

	start := time.Now()

	// Generate a bunch of accounts and persist them. I'm not sure
	// if there's a way to do this in bulk, so starting with just
	// running Exec.
	rowsInserted := 0
	for i := 1; i <= 1346; i++ {

		accountId := uuid.New()
		accountName := fmt.Sprintf("ACCOUNT %d", i)
		_, err = stmt.Exec(accountId.String(), accountName)
		if err != nil {
			trx.Rollback()
			fmt.Println(err)
			os.Exit(-1)
		}
		rowsInserted++
	}

	err = trx.Commit()
	if err != nil {
		fmt.Println(err)
		os.Exit(-1)
	}

	// Print out stats on how long it took to do the inserts.
	end := time.Now()
	length := end.Sub(start)
	printer := message.NewPrinter(language.English)
	printer.Printf("%d rows inserted in %d milliseconds\n", rowsInserted, length.Milliseconds())
}
