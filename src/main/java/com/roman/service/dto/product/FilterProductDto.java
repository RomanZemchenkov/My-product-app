package com.roman.service.dto.product;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import static com.roman.service.exception.ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE;

@Getter
public class FilterProductDto {

    @Length(max = 255, message = PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE)
    private final String title;
    @Min(value = 0, message = PRODUCT_COST_EXCEPTION_MESSAGE)
    private final Integer cost;
    @Min(value = 0, message = PRODUCT_COST_EXCEPTION_MESSAGE)
    private final Integer costMax;
    @Min(value = 0, message = PRODUCT_COST_EXCEPTION_MESSAGE)
    private final Integer costMin;
    private final String inStock;

    public FilterProductDto(String title, Integer cost, Integer costMax, Integer costMin, String inStock) {
        this.title = title;
        this.cost = cost;
        this.costMax = costMax;
        this.costMin = costMin;
        this.inStock = inStock;
    }
}
