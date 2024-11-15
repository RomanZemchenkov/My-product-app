package com.roman.dao.repository;

import com.roman.dao.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepository implements BaseRepository<Long, Product> {

    private static final Map<Long, Product> productTable = new HashMap<>();

    @Override
    public List<Product> getAll() {
        return productTable
                .values()
                .stream()
                .toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productTable.get(id));
    }

    @Override
    public Product save(Product entity) {
        productTable.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Product update(Product entity) {
        return save(entity);
    }

    @Override
    public boolean delete(Long id) {
        Product remove = productTable.remove(id);
        return remove != null;
    }
}
