#! /bin/sh

docker build . -t dev.darrenhacks/bulk-dml-example_db:1.0

docker run -p 5432:5432 -e POSTGRES_PASSWORD=p0stgr@s -e POSTGRES_USER=postgres -e POSTGRES_DB=bulk_dml_db dev.darrenhacks/bulk-dml-example_db:1.0
