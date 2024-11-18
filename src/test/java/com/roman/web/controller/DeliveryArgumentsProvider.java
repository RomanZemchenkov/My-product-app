package com.roman.web.controller;

import com.roman.service.dto.delivery.CreateDeliveryDto;
import com.roman.service.exception.ExceptionMessage;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

public class DeliveryArgumentsProvider {

    public static Stream<Arguments> argumentsForAddNewDeliveryWithWrongParameters(){
        return Stream.of(
                Arguments.of(new CreateDeliveryDto("", "Mobile 11", 120), ExceptionMessage.DELIVERY_TITLE_EMPTY_MESSAGE_EXCEPTION),
                Arguments.of(new CreateDeliveryDto(range(0,256).mapToObj(i -> "a").collect(joining()), "Mobile 11", 120), ExceptionMessage.DELIVERY_TITLE_LENGTH_MESSAGE_EXCEPTION),
                Arguments.of(new CreateDeliveryDto("Mobile 11 Delivery", "Mobile 11", null), ExceptionMessage.DELIVERY_COUNT_EMPTY_MESSAGE_EXCEPTION),
                Arguments.of(new CreateDeliveryDto("Mobile 11 Delivery", "Mobile 11", 0), ExceptionMessage.DELIVERY_COUNT_MIN_MESSAGE_EXCEPTION),
                Arguments.of(new CreateDeliveryDto("Mobile 11 Delivery", "Spoon", 120), ExceptionMessage.PRODUCT_WITH_TITLE_EXIST_EXCEPTION_MESSAGE.formatted("Spoon"))
        );
    }
}
