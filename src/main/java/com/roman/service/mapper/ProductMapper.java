package com.roman.service.mapper;

import com.roman.dao.entity.Product;
import com.roman.dao.entity.ProductState;
import com.roman.service.dto.CreateProductDto;
import com.roman.service.dto.ShowProductDto;
import com.roman.service.dto.UpdateProductDto;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class ProductMapper {

    private static final AtomicLong idGenerator = new AtomicLong(1);

    public Product mapToProduct(CreateProductDto dto){
        Product product = new Product();
        product.setId(idGenerator.getAndIncrement());

        return productFactory(product,dto.getTitle(), dto.getDescription(), dto.getCost(), dto.getState());
    }

    public ShowProductDto mapToShow(Product product){
        Long productId = product.getId();
        String title = product.getTitle();
        String description = product.getDescription();
        Integer cost = product.getCost();
        String state = product.getState().name();
        return new ShowProductDto(productId, title,description,cost, state);
    }

    public Product mapToProduct(UpdateProductDto dto, Product product){
        return productFactory(product,dto.getTitle(), dto.getDescription(), dto.getCost(), dto.getState());
    }

    private Product productFactory(Product product, String title, String description, Integer cost, String state){
        product.setTitle(title);
        product.setDescription(description);
        product.setCost(cost);
        product.setState((state != null && state.equals("EXIST")) ? ProductState.EXIST : ProductState.NOT_EXIST);

        return product;
    }
}
