--liquibase formatted sql

--changeset roman:1
CREATE TABLE delivery
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    product_id BIGINT REFERENCES product(id) NOT NULL,
    count INTEGER NOT NULL check ( count >= 1 )
);

--changeset roman:2
CREATE TABLE sale
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    product_id BIGINT REFERENCES product(id) NOT NULL,
    count INTEGER NOT NULL check ( count >= 1 )
);