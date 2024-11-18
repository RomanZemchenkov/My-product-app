package com.roman.dao.repository.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilterAndSorter {

    private String title;
    private int cost;
    private int costMax;
    private int costMin;
    private String inStock;
    private String ascByName;
    private String descByName;
    private String ascByCost;
    private String descByCost;

    public static class Builder{
        private final ProductFilterAndSorter productFilterAndSorter;

        public Builder(){
            this.productFilterAndSorter = new ProductFilterAndSorter();
        }

        public Builder setTitle(String title){
            productFilterAndSorter.setTitle(title);
            return this;
        }
        public Builder setCost(int cost){
            productFilterAndSorter.setCost(cost);
            return this;
        }
        public Builder setCostMax(int costMax){
            productFilterAndSorter.setCostMax(costMax);
            return this;
        }
        public Builder setCostMin(int costMin){
            productFilterAndSorter.setCostMin(costMin);
            return this;
        }
        public Builder setInStock(String inStock){
            productFilterAndSorter.setInStock(inStock);
            return this;
        }

        public Builder setAscByName(String sort){
            productFilterAndSorter.setAscByName(sort);
            return this;
        }
        public Builder setDescByName(String sort){
            productFilterAndSorter.setDescByName(sort);
            return this;
        }
        public Builder setAscByCost(String sort){
            productFilterAndSorter.setAscByCost(sort);
            return this;
        }
        public Builder setDescByCost(String sort){
            productFilterAndSorter.setDescByCost(sort);
            return this;
        }

        public ProductFilterAndSorter build(){
            return productFilterAndSorter;
        }


    }
}
