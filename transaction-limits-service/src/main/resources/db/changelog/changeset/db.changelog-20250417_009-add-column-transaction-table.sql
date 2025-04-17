--liquibase formatted sql

--changeset Vlad:20250417_0324_009

ALTER TABLE app.transactions
ADD COLUMN limit_exceeded BOOLEAN NOT NULL;