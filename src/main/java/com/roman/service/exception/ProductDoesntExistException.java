package com.roman.service.exception;

import static com.roman.service.exception.ExceptionMessage.PRODUCT_WITH_ID_EXIST_EXCEPTION_MESSAGE;
import static com.roman.service.exception.ExceptionMessage.PRODUCT_WITH_TITLE_EXIST_EXCEPTION_MESSAGE;

public class ProductDoesntExistException extends RuntimeException{

    public ProductDoesntExistException(Long id){
        super(PRODUCT_WITH_ID_EXIST_EXCEPTION_MESSAGE.formatted(id));
    }

    public ProductDoesntExistException(String id){
        super(PRODUCT_WITH_TITLE_EXIST_EXCEPTION_MESSAGE.formatted(id));
    }
}
