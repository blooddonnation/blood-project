#!/bin/bash
set -e

echo "Starting database initialization..."

# Create databases with error handling
echo "Creating database bloody_donation..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c "CREATE DATABASE bloody_donation;" || {
    echo "Failed to create database bloody_donation"
    exit 1
}

echo "Creating database MicroserverBanque..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c "CREATE DATABASE \"MicroserverBanque\";" || {
    echo "Failed to create database MicroserverBanque"
    exit 1
}

echo "Creating database authdb..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c "CREATE DATABASE authdb;" || {
    echo "Failed to create database authdb"
    exit 1
}

echo "Database initialization completed." 