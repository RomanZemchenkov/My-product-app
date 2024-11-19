package com.roman.service.exception;

import static com.roman.service.exception.ExceptionMessage.SALE_COUNT_MORE_THAN_MESSAGE_EXCEPTION;

public class SaleException extends RuntimeException{

    public SaleException(int count){
        super(SALE_COUNT_MORE_THAN_MESSAGE_EXCEPTION.formatted(count));
    }
}
