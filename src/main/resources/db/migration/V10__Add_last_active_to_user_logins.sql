-- Add last_active column to user_logins table
ALTER TABLE att_user_db.user_logins ADD COLUMN last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;
