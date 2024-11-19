package com.roman.service.dto.delivery;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ShowDeliveryDto {

    private final Long deliveryId;
    private final String title;
    private final String productTitle;
    private final Integer countOfProducts;

    public ShowDeliveryDto(Long deliveryId, String title, String productTitle, Integer countOfProducts) {
        this.deliveryId = deliveryId;
        this.title = title;
        this.productTitle = productTitle;
        this.countOfProducts = countOfProducts;
    }
}
