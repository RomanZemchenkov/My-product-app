package com.roman.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = {"id","title"})
public class ShowProductDto {

    private final long id;
    private final String title;
    private final String description;
    private final Integer cost;
    private final String inStock;
    @Setter
    private Integer countInStock;

    @JsonCreator
    public ShowProductDto(@JsonProperty(value = "id") long id,
                          @JsonProperty(value = "title") String title,
                          @JsonProperty(value = "description") String description,
                          @JsonProperty(value = "cost") Integer cost,
                          @JsonProperty(value = "inStock") String inStock) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.inStock = inStock;
    }
}
