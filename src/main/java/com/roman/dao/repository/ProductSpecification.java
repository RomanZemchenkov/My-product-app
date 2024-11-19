package com.roman.dao.repository;

import com.roman.dao.entity.Product;
import com.roman.service.dto.FilterProductDto;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public final class ProductSpecification {

    public static Specification<Product> buildSpecification(FilterProductDto filter){
        return Specification
                .where(titleContains(filter.getTitle()))
                .and(cost(filter.getCost(),filter.getCostMin(), filter.getCostMax()))
                .and(inStock(filter.getInStock()));
    }

    private static Specification<Product> titleContains(String title){
        return (root, query, criteriaBuilder) -> title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    private static Specification<Product> cost(Integer target,Integer min, Integer max){
        return (root, query, criteriaBuilder) -> {
            Path<Integer> cost = root.get("cost");
            if(target != null){
                return criteriaBuilder.equal(cost, target);
            } else if (min != null && max != null){
                return criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(cost,min),
                        criteriaBuilder.lessThanOrEqualTo(cost,max));
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(cost, min);
            } else if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(cost, max);
            } else {
                return null;
            }
        };
    }

    private static Specification<Product> inStock(String inStock){
        return (root, query, criteriaBuilder) -> {
            if(inStock != null){
                return criteriaBuilder.equal(root.get("inStock"),inStock);
            }
            return null;
        };
    }
}
