--liquibase formatted sql

--changeset Vlad:20250413_2021_004

CREATE TABLE app.exchange_rates
(
id BIGSERIAL,
date TIMESTAMP(3) NOT NULL,
currency_from character varying (3) NOT NULL,
currency_to character varying (3) NOT NULL,
close_rate NUMERIC(18,2),
previous_close_rate NUMERIC(18,2),
created_at TIMESTAMP(3) NOT NULL,
updated_at TIMESTAMP(3) NOT NULL,
CONSTRAINT exchange_rates_pk PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS app.exchange_rates
    OWNER to postgres;
