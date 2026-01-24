CREATE TABLE asan_user_certificates
(
    id                UUID NOT NULL,
    user_login_id     UUID NOT NULL,
    certificat_number VARCHAR(255),
    has_stamp         BOOLEAN,
    legal             BOOLEAN,
    voen              VARCHAR(255),
    structure_name    VARCHAR(255),
    position          VARCHAR(255),
    CONSTRAINT pk_asan_user_certificates PRIMARY KEY (id)
);
