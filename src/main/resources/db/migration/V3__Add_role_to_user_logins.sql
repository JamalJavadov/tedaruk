-- Migration: Add role column to user_logins table
-- Schema: att_user_db

ALTER TABLE "att_user_db"."user_logins" 
ADD COLUMN IF NOT EXISTS role VARCHAR(255) DEFAULT 'seller';

-- Set default value for existing records
UPDATE "att_user_db"."user_logins" SET role = 'seller' WHERE role IS NULL;

