package com.roman.service.dto.sale;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import static com.roman.service.exception.ExceptionMessage.SALE_COUNT_EMPTY_MESSAGE_EXCEPTION;
import static com.roman.service.exception.ExceptionMessage.SALE_COUNT_MIN_MESSAGE_EXCEPTION;
import static com.roman.service.exception.ExceptionMessage.SALE_TITLE_EMPTY_MESSAGE_EXCEPTION;
import static com.roman.service.exception.ExceptionMessage.SALE_TITLE_LENGTH_MESSAGE_EXCEPTION;

@Getter
@EqualsAndHashCode
public class CreateSaleDto {

    @NotBlank(message = SALE_TITLE_EMPTY_MESSAGE_EXCEPTION)
    @Length(max = 255,message = SALE_TITLE_LENGTH_MESSAGE_EXCEPTION)
    private final String title;
    private final String productTitle;
    @NotNull(message = SALE_COUNT_EMPTY_MESSAGE_EXCEPTION)
    @Min(value = 1, message = SALE_COUNT_MIN_MESSAGE_EXCEPTION)
    private final Integer count;

    public CreateSaleDto(String title, String productTitle, Integer count) {
        this.title = title;
        this.productTitle = productTitle;
        this.count = count;
    }
}