package com.roman.service.mapper;

import com.roman.dao.entity.Delivery;
import com.roman.service.dto.delivery.CreateDeliveryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "count", source = "count")
    Delivery mapToDelivery(CreateDeliveryDto dto);
}
