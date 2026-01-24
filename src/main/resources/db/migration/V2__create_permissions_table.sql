CREATE TABLE permissions
(
    name             VARCHAR(255) NOT NULL,
    label            VARCHAR(255) NOT NULL,
    last_modified_by VARCHAR(255),
    last_modified_on TIMESTAMP WITHOUT TIME ZONE,
    created_on       TIMESTAMP WITHOUT TIME ZONE,
    created_by       VARCHAR(255),
    CONSTRAINT pk_permissions PRIMARY KEY (name)
);
