package com.roman.service.exception;

import static com.roman.service.exception.ExceptionMessage.DELIVERY_WITH_ID_EXIST_EXCEPTION_MESSAGE;

public class DeliveryDoesntExistException extends RuntimeException{

    public DeliveryDoesntExistException(Long id){
        super(DELIVERY_WITH_ID_EXIST_EXCEPTION_MESSAGE.formatted(id));
    }
}
