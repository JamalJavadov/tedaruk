-- V8__Rename_user_details_to_user_certificates.sql
-- Rename user_details table to user_certificates for better clarity

ALTER TABLE att_user_db.user_details RENAME TO user_certificates;
ALTER TABLE att_user_db.user_certificates RENAME CONSTRAINT fk_user_details_user_login TO fk_user_certificates_user_login;
