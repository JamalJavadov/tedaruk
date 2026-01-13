-- V12__Add_unique_constraint_to_user_voen.sql
-- Add unique constraint for (user_login_id, voen) in asan_user_certificates table

ALTER TABLE att_user_db.asan_user_certificates
ADD CONSTRAINT uk_user_voen UNIQUE (user_login_id, voen);
