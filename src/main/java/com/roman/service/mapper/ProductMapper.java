package com.roman.service.mapper;

import com.roman.dao.entity.Product;
import com.roman.service.dto.CreateProductDto;
import com.roman.service.dto.ShowProductDto;
import com.roman.service.dto.UpdateProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "inStock", source = "inStock")
    Product mapToProduct(CreateProductDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "inStock", expression = "java(ProductState.valueOf(dto.getInStock() == null || dto.getInStock().isEmpty() ? \"NOT_EXIST\" : dto.getInStock()))")
    Product mapToProduct(UpdateProductDto dto, @MappingTarget Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "inStock", source = "inStock")
    ShowProductDto mapToShow(Product product);

}
