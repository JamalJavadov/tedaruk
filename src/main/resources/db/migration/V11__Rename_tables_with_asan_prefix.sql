-- Rename tables with asan_ prefix
ALTER TABLE att_user_db.user_logins RENAME TO asan_user_logins;
ALTER TABLE att_user_db.user_certificates RENAME TO asan_user_certificates;

-- Drop asan_login_time table if exists
DROP TABLE IF EXISTS att_user_db.asan_login_time;

-- Rename last_active column to last_active_time in asan_user_logins
ALTER TABLE att_user_db.asan_user_logins RENAME COLUMN last_active TO last_active_time;
