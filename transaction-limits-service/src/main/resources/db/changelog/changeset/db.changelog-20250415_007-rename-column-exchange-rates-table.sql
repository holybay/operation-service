--liquibase formatted sql

--changeset Vlad:20250415_1827_007

ALTER TABLE app.exchange_rates
RENAME COLUMN "date" TO currency_date;