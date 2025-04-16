--liquibase formatted sql

--changeset Vlad:20250415_2330_008

ALTER TABLE app.transactions
ADD COLUMN date_time_zone character varying (6) NOT NULL;