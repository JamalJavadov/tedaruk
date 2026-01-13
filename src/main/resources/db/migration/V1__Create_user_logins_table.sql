-- Migration: Create user_logins table with all required columns
-- Schema: att_user_db

CREATE SCHEMA IF NOT EXISTS "att_user_db";

CREATE TABLE IF NOT EXISTS "att_user_db"."user_logins" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    voen VARCHAR(255),
    structure_name VARCHAR(255),
    position VARCHAR(255),
    phone_number VARCHAR(255),
    user_id VARCHAR(255),
    login_time TIMESTAMP NOT NULL,
    logout_time TIMESTAMP,
    pin VARCHAR(255),
    name VARCHAR(255),
    surname VARCHAR(255),
    patronymic VARCHAR(255),
    citizenship VARCHAR(255),
    has_stamp BOOLEAN,
    legal BOOLEAN,
    thumbprint VARCHAR(255),
    serial_number VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_user_logins_login_time ON "att_user_db"."user_logins"(login_time);
