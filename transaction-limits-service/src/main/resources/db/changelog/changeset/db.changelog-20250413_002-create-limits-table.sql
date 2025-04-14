--liquibase formatted sql

--changeset Vlad:20250413_1940_002

CREATE TABLE app.limits
(
id BIGSERIAL,
account_from BIGINT NOT NULL,
date_from TIMESTAMPTZ(3) NOT NULL,
date_to TIMESTAMPTZ(3),
limit_sum NUMERIC(18,2) NOT NULL,
currency character varying (3) NOT NULL,
category_id BIGINT NOT NULL,
created_at TIMESTAMP(3) NOT NULL,
updated_at TIMESTAMP(3) NOT NULL,
CONSTRAINT limits_pk PRIMARY KEY (id),
CONSTRAINT limits_expense_categories_fk FOREIGN KEY (category_id) REFERENCES app.expense_categories(id),
CONSTRAINT limits_account_dateFrom_dateTo_category_unq UNIQUE(account_from, date_from, date_to, category_id)
);

ALTER TABLE IF EXISTS app.limits
    OWNER to postgres;
