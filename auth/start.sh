#!/bin/sh

# set env variables from secrets

AUTH_DB_PSWD=$(cat /run/secrets/postgres_password)
export AUTH_DB_PSWD

# run app
java -jar auth.jar