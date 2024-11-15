package com.roman.service.dto;

import lombok.Getter;

@Getter
public class ShowProductDto {

    private final long id;
    private final String title;
    private final String description;
    private final Integer cost;
    private final String state;

    public ShowProductDto(long id, String title, String description, Integer cost, String state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.state = state;
    }
}
