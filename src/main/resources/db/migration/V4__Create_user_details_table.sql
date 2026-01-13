-- Migration: Create user_details table with foreign key to user_logins
-- Schema: att_user_db

CREATE TABLE IF NOT EXISTS "att_user_db"."user_details" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_login_id UUID NOT NULL,
    thumbprint VARCHAR(255),
    serial_number VARCHAR(255),
    has_stamp BOOLEAN,
    legal BOOLEAN,
    voen VARCHAR(255),
    structure_name VARCHAR(255),
    position VARCHAR(255),
    CONSTRAINT fk_user_details_user_login 
        FOREIGN KEY (user_login_id) 
        REFERENCES "att_user_db"."user_logins"(id) 
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_user_details_user_login_id ON "att_user_db"."user_details"(user_login_id);



