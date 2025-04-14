--liquibase formatted sql

--changeset Vlad:20250413_2044_005

INSERT INTO app.expense_categories (name, created_at, updated_at)
VALUES ('product', now(), now()),
('service', now(), now());
