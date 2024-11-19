package com.roman.service.mapper;

import com.roman.dao.entity.Sale;
import com.roman.service.dto.sale.CreateSaleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "count", source = "count")
    Sale mapToSale(CreateSaleDto dto);
}
