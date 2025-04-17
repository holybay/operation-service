--liquibase formatted sql

--changeset Vlad:20250417_0507_010

ALTER TABLE exchange_rates
  ALTER COLUMN currency_date TYPE DATE;

ALTER TABLE exchange_rates
  ALTER COLUMN currency_date SET NOT NULL;