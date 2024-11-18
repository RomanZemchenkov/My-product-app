package com.roman.service.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductDto {

    private String title;
    @Min(value = 0L,message = "Message")
    private Integer cost;
    private Integer costMax;
    private Integer costMin;
    private String inStock;

    public static class Builder{
        private final FilterProductDto filterProductDto;

        public Builder(){
            this.filterProductDto = new FilterProductDto();
        }

        public Builder setTitle(String title){
            filterProductDto.setTitle(title);
            return this;
        }
        public Builder setCost(Integer cost){
            filterProductDto.setCost(cost);
            return this;
        }
        public Builder setCostMax(Integer costMax){
            filterProductDto.setCostMax(costMax);
            return this;
        }
        public Builder setCostMin(Integer costMin){
            filterProductDto.setCostMin(costMin);
            return this;
        }
        public Builder setInStock(String inStock){
            filterProductDto.setInStock(inStock);
            return this;
        }

        public FilterProductDto build(){
            return filterProductDto;
        }
    }
}
