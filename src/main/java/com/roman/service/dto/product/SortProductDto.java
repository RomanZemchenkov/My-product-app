package com.roman.service.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortProductDto {

    private String sortBy;
    private String orderBy;

    public static class Builder{
        private final SortProductDto sortProductDto;

        public Builder(){
            this.sortProductDto = new SortProductDto();
        }

        public Builder setSortBy(String sort){
            sortProductDto.setSortBy(sort);
            return this;
        }
        public Builder setOrderBy(String sort){
            sortProductDto.setOrderBy(sort);
            return this;
        }

        public SortProductDto build(){
            return sortProductDto;
        }
    }
}
