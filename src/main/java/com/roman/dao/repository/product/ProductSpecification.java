package com.roman.dao.repository.product;

import com.roman.dao.entity.Product;
import com.roman.service.dto.product.FilterProductDto;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public Specification<Product> buildSpecification(FilterProductDto filter){
        return Specification
                .where(titleContains(filter.getTitle()))
                .and(cost(filter.getCost(),filter.getCostMin(), filter.getCostMax()))
                .and(inStock(filter.getInStock()));
    }

    private Specification<Product> titleContains(String title){
        return (root, query, criteriaBuilder) -> title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    private Specification<Product> cost(Integer target,Integer min, Integer max){
        return (root, query, criteriaBuilder) -> {
            Path<Integer> cost = root.get("cost");
            if(target != null){
                return criteriaBuilder.equal(cost, target);
            } else if (min != null && max != null){
                return criteriaBuilder.between(cost, min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThan(cost, min);
            } else if (max != null) {
                return criteriaBuilder.lessThan(cost, min);
            } else {
                return null;
            }
        };
    }

    private Specification<Product> inStock(String inStock){
        return (root, query, criteriaBuilder) -> {
            if(inStock != null){
                return criteriaBuilder.equal(root.get("inStock"),inStock);
            }
            return null;
        };
    }
}
