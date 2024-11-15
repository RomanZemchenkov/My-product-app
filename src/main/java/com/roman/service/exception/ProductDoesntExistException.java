package com.roman.service.exception;

import static com.roman.service.exception.ExceptionMessage.PRODUCT_DOESNT_EXIST_EXCEPTION_MESSAGE;

public class ProductDoesntExistException extends RuntimeException{

    public ProductDoesntExistException(Long id){
        super(PRODUCT_DOESNT_EXIST_EXCEPTION_MESSAGE.formatted(id));
    }
}
