package com.roman.dao.repository.product;

public interface CustomProductRepository {

    void updateCountInStock(int newCountInStock, long productId);
}
