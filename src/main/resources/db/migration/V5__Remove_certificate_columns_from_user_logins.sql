-- Migration: Remove certificate-related columns from user_logins table
-- Schema: att_user_db
-- These columns are now in user_details table

ALTER TABLE "att_user_db"."user_logins" 
DROP COLUMN IF EXISTS voen,
DROP COLUMN IF EXISTS structure_name,
DROP COLUMN IF EXISTS position,
DROP COLUMN IF EXISTS has_stamp,
DROP COLUMN IF EXISTS legal,
DROP COLUMN IF EXISTS thumbprint,
DROP COLUMN IF EXISTS serial_number;



