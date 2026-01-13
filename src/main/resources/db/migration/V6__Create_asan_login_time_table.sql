-- V6__Create_asan_login_time_table.sql
-- New table for tracking login/logout times separately from user credentials

CREATE TABLE IF NOT EXISTS att_user_db.asan_login_time (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    uid UUID NOT NULL REFERENCES att_user_db.user_logins(id) ON DELETE CASCADE,
    login_time TIMESTAMP NOT NULL,
    logout_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_asan_login_time_uid ON att_user_db.asan_login_time(uid);
CREATE INDEX idx_asan_login_time_login_time ON att_user_db.asan_login_time(login_time);
