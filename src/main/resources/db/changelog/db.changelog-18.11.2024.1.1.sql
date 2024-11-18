--liquibase formatted sql

--changeset roman:1
ALTER TABLE product
ADD COLUMN count_in_stock INTEGER check ( count_in_stock >= 0 ) NOT NULL DEFAULT 0;