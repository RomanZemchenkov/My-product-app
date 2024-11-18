package com.roman.service.mapper;

import com.roman.dao.entity.Product;
import com.roman.dao.entity.ProductState;
import com.roman.service.dto.CreateProductDto;
import com.roman.service.dto.ShowProductDto;
import com.roman.service.dto.UpdateProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring",imports = {ProductState.class})
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "countInStock", source = "countInStock")
    @Mapping(target = "inStock", expression = "java(dto.getCountInStock() == 0 ? ProductState.valueOf(\"NOT_EXIST\") : ProductState.valueOf(\"EXIST\"))")
    Product mapToProduct(CreateProductDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "inStock", expression = "java(dto.getCountInStock() == 0 ? ProductState.NOT_EXIST : ProductState.EXIST)")
    @Mapping(target = "countInStock", source = "countInStock")
    Product mapToProduct(UpdateProductDto dto, @MappingTarget Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cost", source = "cost")
    @Mapping(target = "inStock", source = "inStock")
    @Mapping(target = "countInStock", expression = "java(product.getInStock().equals(\"NOT_EXIST\") ? null : product.getCountInStock())")
    ShowProductDto mapToShow(Product product);

}
