CREATE TABLE role_permission
(
    permission_id VARCHAR(255) NOT NULL,
    role_id       BIGINT       NOT NULL
);

ALTER TABLE role_permission
    ADD CONSTRAINT uk_role_permission UNIQUE (role_id, permission_id);

ALTER TABLE role_permission
    ADD CONSTRAINT fk_rolper_on_permission
        FOREIGN KEY (permission_id) REFERENCES permissions (name);

ALTER TABLE role_permission
    ADD CONSTRAINT fk_rolper_on_role
        FOREIGN KEY (role_id) REFERENCES roles (id);
