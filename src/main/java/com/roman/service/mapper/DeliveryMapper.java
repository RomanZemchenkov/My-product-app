package com.roman.service.mapper;

import com.roman.dao.entity.Delivery;
import com.roman.service.dto.delivery.CreateDeliveryDto;
import com.roman.service.dto.delivery.ShowDeliveryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "count", source = "count")
    Delivery mapToDelivery(CreateDeliveryDto dto);

    @Mapping(target = "deliveryId", source = "delivery.id")
    @Mapping(target = "title", source = "delivery.title")
    @Mapping(target = "productTitle", source = "productTitle")
    @Mapping(target = "countOfProducts", source = "delivery.count")
    ShowDeliveryDto mapToShow(Delivery delivery, String productTitle);

    @Mapping(target = "deliveryId", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "productTitle", source = "delivery.product.title")
    @Mapping(target = "countOfProducts", source = "count")
    ShowDeliveryDto mapToShow(Delivery delivery);
}
