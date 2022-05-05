FROM postgres:14.2

# Create the example table.
COPY schema.sql /docker-entrypoint-initdb.d/01-schema.sql

CMD ["postgres", "-c", "log_min_messages=DEBUG4"]
