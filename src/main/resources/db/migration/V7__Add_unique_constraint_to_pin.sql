-- V7__Add_unique_constraint_to_pin.sql
-- Add unique constraint for PIN field to prevent duplicate logins

ALTER TABLE att_user_db.user_logins
ADD CONSTRAINT uk_pin UNIQUE (pin);
