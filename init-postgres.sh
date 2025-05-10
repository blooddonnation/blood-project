#!/bin/bash
set -e
echo "Starting database initialization..."

# Test psql connection first
echo "Testing psql connection..."
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" -c "SELECT 1;" || {
    echo "Failed to connect to database $POSTGRES_DB as user $POSTGRES_USER"
    exit 1
}

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

echo "Database initialization completed."