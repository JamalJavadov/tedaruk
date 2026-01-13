-- Migration: Add is_active column to user_logins table
-- Schema: att_user_db

ALTER TABLE "att_user_db"."user_logins" 
ADD COLUMN IF NOT EXISTS is_active BOOLEAN;

-- Set default value for existing records (optional, can be NULL)
-- UPDATE "att_user_db"."user_logins" SET is_active = false WHERE is_active IS NULL;

