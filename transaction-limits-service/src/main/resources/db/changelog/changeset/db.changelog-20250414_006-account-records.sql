--liquibase formatted sql

--changeset Vlad:20250414_2330_006

CREATE TABLE app.account_records
(
id BIGSERIAL,
account_id BIGINT,
is_activated BOOLEAN NOT NULL DEFAULT FALSE,
CONSTRAINT account_records_pk PRIMARY KEY (id),
CONSTRAINT account_records_unq UNIQUE(account_id, is_activated)
);

ALTER TABLE IF EXISTS app.account_records
    OWNER to postgres;
