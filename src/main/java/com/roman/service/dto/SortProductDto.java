package com.roman.service.dto;

import lombok.Getter;

@Getter
public class SortProductDto {

    private final String sortBy;
    private final String orderBy;

    public SortProductDto(String sortBy, String orderBy) {
        this.sortBy = sortBy;
        this.orderBy = orderBy;
    }
}
