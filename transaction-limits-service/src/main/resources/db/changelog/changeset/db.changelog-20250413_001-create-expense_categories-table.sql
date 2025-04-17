--liquibase formatted sql

--changeset Vlad:20250413_1924_001

CREATE TABLE app.expense_categories
(
id BIGSERIAL,
name character varying (30) NOT NULL,
created_at TIMESTAMP(3) NOT NULL,
updated_at TIMESTAMP(3) NOT NULL,
CONSTRAINT expense_categories_pk PRIMARY KEY (id),
CONSTRAINT expense_categories_name_unq UNIQUE(name)
);

ALTER TABLE IF EXISTS app.users
    OWNER to postgres;
