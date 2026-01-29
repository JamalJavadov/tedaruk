CREATE TABLE organizations
(
    id   UUID NOT NULL,
    tin VARCHAR(255),
    CONSTRAINT pk_organizations PRIMARY KEY (id)
);
