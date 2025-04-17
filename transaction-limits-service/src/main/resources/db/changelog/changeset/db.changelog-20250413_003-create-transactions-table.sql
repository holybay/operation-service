--liquibase formatted sql

--changeset Vlad:20250413_2012_003

CREATE TABLE app.transactions
(
id BIGSERIAL,
account_from BIGINT NOT NULL,
account_to BIGINT NOT NULL,
date_time TIMESTAMPTZ(3) NOT NULL,
"sum" NUMERIC(18,2) NOT NULL,
currency character varying (3) NOT NULL,
category_id BIGINT NOT NULL,
created_at TIMESTAMP(3) NOT NULL,
updated_at TIMESTAMP(3) NOT NULL,
CONSTRAINT transactions_pk PRIMARY KEY (id),
CONSTRAINT transactions_expense_categories_fk FOREIGN KEY (category_id) REFERENCES app.expense_categories(id)
);

ALTER TABLE IF EXISTS app.transactions
    OWNER to postgres;
