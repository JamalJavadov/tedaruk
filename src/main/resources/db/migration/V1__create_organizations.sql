CREATE TABLE organizations
(
    id                     UUID NOT NULL,
    name                   VARCHAR(255) NOT NULL,
    tin                    VARCHAR(10),
    address                VARCHAR(255),
    phone_number           VARCHAR(255),
    email                  VARCHAR(255),
    region_id              INTEGER,
    organization_type      INTEGER DEFAULT 0 NOT NULL,
    is_registered          BOOLEAN DEFAULT FALSE NOT NULL,
    created_by             VARCHAR(255),
    created_on             TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_by       VARCHAR(255),
    last_modified_on       TIMESTAMP WITHOUT TIME ZONE,
    parent_organization_id UUID,
    organisation_status    INTEGER DEFAULT 0 NOT NULL,
    logo_name              VARCHAR(255),
    about                  VARCHAR(255),
    ownership_type_id      INTEGER DEFAULT 100 NOT NULL,
    visible_on_public      BOOLEAN DEFAULT TRUE NOT NULL,
    is_first_entry         BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT pk_organizations PRIMARY KEY (id)
);
