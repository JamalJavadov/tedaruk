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
    organization_id   UUID,
    CONSTRAINT pk_asan_user_certificates PRIMARY KEY (id)
);

ALTER TABLE asan_user_certificates
    ADD CONSTRAINT uk_user_voen UNIQUE (user_login_id, voen);

ALTER TABLE asan_user_certificates
    ADD CONSTRAINT fk_asan_user_certificates_on_user_login
        FOREIGN KEY (user_login_id) REFERENCES users (id);

ALTER TABLE asan_user_certificates
    ADD CONSTRAINT fk_asan_user_certificates_on_organization
        FOREIGN KEY (organization_id) REFERENCES organizations (id);
