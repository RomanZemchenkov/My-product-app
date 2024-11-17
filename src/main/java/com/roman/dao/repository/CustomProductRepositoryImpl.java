package com.roman.dao.repository;

import com.roman.dao.entity.Product;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl {

    private final EntityManager entityManager;

}
