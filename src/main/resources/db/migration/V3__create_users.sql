CREATE TABLE users
(
    id               UUID NOT NULL,
    phone_number     VARCHAR(255),
    pin              VARCHAR(255),
    first_name       VARCHAR(255),
    last_name        VARCHAR(255),
    patronymic       VARCHAR(255),
    citizenship      VARCHAR(255),
    last_active_time TIMESTAMP WITHOUT TIME ZONE,
    gmail            VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uk_pin UNIQUE (pin);
