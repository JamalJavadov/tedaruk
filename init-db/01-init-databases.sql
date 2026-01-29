-- Create database and schema
CREATE DATABASE att_tedaruk_db;

\c att_tedaruk_db;

-- Create schema
CREATE SCHEMA IF NOT EXISTS att_user_db;

-- Set default schema search path
SET search_path TO att_user_db, public;

-- Grants
GRANT ALL PRIVILEGES ON DATABASE att_tedaruk_db TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA att_user_db TO postgres;
