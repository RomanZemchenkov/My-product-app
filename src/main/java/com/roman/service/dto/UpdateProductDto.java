package com.roman.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import static com.roman.service.exception.ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE;

@Getter
public class UpdateProductDto {

    @NotBlank(message = PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE)
    @Length(max = 255, message = PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE)
    private final String title;
    @NotBlank(message = PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE)
    @Length(max = 4096,message = PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE)
    private final String description;
    @Min(value = 0 , message = PRODUCT_COST_EXCEPTION_MESSAGE)
    private final Integer cost;
    private final String inStock;

    public UpdateProductDto(String title, String description, Integer cost, String inStock) {
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.inStock = inStock;
    }
}
