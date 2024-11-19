DELETE FROM delivery WHERE id != 0;
DELETE FROM product WHERE id != 0;

ALTER SEQUENCE product_id_seq RESTART WITH 1;
ALTER SEQUENCE delivery_id_seq RESTART WITH 1;
