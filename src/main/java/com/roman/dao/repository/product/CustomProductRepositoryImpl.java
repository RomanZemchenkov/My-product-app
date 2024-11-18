package com.roman.dao.repository.product;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository{

    private final EntityManager entityManager;

    public void updateCountInStock(int newCountInStock, long productId){
        entityManager.createQuery("UPDATE Product p SET countInStock = :count WHERE p.id = :id")
                .setParameter("count", newCountInStock)
                .setParameter("id", productId)
                .executeUpdate();
    }

}
