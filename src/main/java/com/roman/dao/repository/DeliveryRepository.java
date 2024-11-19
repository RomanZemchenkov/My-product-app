package com.roman.dao.repository;

import com.roman.dao.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query(value = "SELECT d FROM Delivery d WHERE d.product.title = :productTitle")
    List<Delivery> findAllByProductTitle(@Param("productTitle") String productTitle);

    @Query(value = "SELECT d FROM Delivery d JOIN FETCH d.product WHERE d.id = :id")
    Optional<Delivery> findDeliveryWithProductById(@Param("id") Long id);
}
