--liquibase formatted sql

--changeset roman:1
CREATE TABLE product
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL CHECK ( length(description) <= 4096 ),
    cost INTEGER NOT NULL check ( cost >= 0 ),
    in_stock VARCHAR(16) NOT NULL
)