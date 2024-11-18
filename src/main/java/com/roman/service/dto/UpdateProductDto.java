package com.roman.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import static com.roman.service.exception.ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_COUNT_IN_STOCK_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE;

@Getter
@EqualsAndHashCode(of = "title")
public class UpdateProductDto {

    @NotBlank(message = PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE)
    @Length(max = 255, message = PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE)
    private final String title;
    @NotBlank(message = PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE)
    @Length(max = 4096,message = PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE)
    private final String description;
    @Min(value = 0 , message = PRODUCT_COST_EXCEPTION_MESSAGE)
    private final Integer cost;
    @Min(value = 0,message = PRODUCT_COUNT_IN_STOCK_EXCEPTION_MESSAGE)
    @NotNull(message = PRODUCT_COUNT_IN_STOCK_EXCEPTION_MESSAGE)
    private final Integer countInStock;
    @Setter
    private String inStock;

    @JsonCreator
    public UpdateProductDto(@JsonProperty(value = "title") String title,
                            @JsonProperty(value = "description") String description,
                            @JsonProperty(value = "cost") Integer cost,
                            @JsonProperty(value = "countInStock") Integer countInStock) {
        this.title = title;
        this.description = description;
        this.cost = cost;
        this.countInStock = countInStock;
    }
}
