package com.roman.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
public class Product implements BaseEntity<Long>{

    private Long id;

    private String title;

    private String description;

    private Integer cost;

    private ProductState state;

    public Product(){}

    public Product(Long id, String title, String description, Integer cost, ProductState state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.state = state;
    }
}
