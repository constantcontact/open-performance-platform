#!/usr/bin/env bash
# need to move this to docker to initialize the database if needed or have it in some type of an init script
echo "You are about to delete everything (if it exists) and re-initialize the schema"
mysql --host={mysqldbhost} --user=oppuser --port=3306 --default-character-set=utf8 --database=opp < "opp-20170809-schema.sql"
