package com.roman.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",length = 255)
    private String title;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "in_stock")
    @Enumerated(value = EnumType.STRING)
    private ProductState inStock;

    @Column(name = "count_in_stock")
    private Integer countInStock;

    public Product(){}

    public Product(Long id, String title, String description, Integer cost, ProductState inStock, Integer countInStock) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.inStock = inStock;
        this.countInStock = countInStock;
    }
}
