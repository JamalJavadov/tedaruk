-- V9__Remove_login_logout_time_from_user_logins.sql
-- Remove login_time and logout_time columns from user_logins table
-- These fields are now tracked separately in asan_login_time table

ALTER TABLE att_user_db.user_logins DROP COLUMN IF EXISTS login_time;
ALTER TABLE att_user_db.user_logins DROP COLUMN IF EXISTS logout_time;
