-- Database və schema yaradılması
CREATE DATABASE att_tedaruk_db;

\c att_tedaruk_db;

-- Schema yaradılması
CREATE SCHEMA IF NOT EXISTS att_user_db;

-- Default schema təyin edilməsi
SET search_path TO att_user_db, public;

-- İcazələr
GRANT ALL PRIVILEGES ON DATABASE att_tedaruk_db TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA att_user_db TO postgres;
