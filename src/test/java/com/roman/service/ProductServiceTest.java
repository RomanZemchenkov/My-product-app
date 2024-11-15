package com.roman.service;

import com.roman.dao.entity.Product;
import com.roman.dao.entity.ProductState;
import com.roman.dao.repository.ProductRepository;
import com.roman.service.dto.CreateProductDto;
import com.roman.service.dto.ShowProductDto;
import com.roman.service.dto.UpdateProductDto;
import com.roman.service.mapper.ProductMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private final ProductMapper productMapper = new ProductMapper();
    private static final Product PRODUCT = new Product(1L,"Phone","Description",100,ProductState.EXIST);

    @BeforeEach
    void setUp(){
        productService = new ProductService(productRepository, productMapper);
    }

    @Test
    @DisplayName("Test for create product")
    void createProductTest(){
        String title = "Phone";
        String description = "Description";
        int cost = 100;
        CreateProductDto dto = new CreateProductDto(title, description, cost, "EXIST");

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(PRODUCT);

        productService.addNewProduct(dto);

        Mockito.verify(productRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test for find product by id")
    void findProductById(){

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(PRODUCT));

        productService.findById(1L);

        Mockito.verify(productRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test for find all products")
    void findAllProducts(){
        Mockito.when(productRepository.getAll()).thenReturn(List.of(PRODUCT));

        Assertions.assertDoesNotThrow(() -> productService.findAllProducts());

        Mockito.verify(productRepository, Mockito.times(1)).getAll();
    }

    @Test
    @DisplayName("Test for update product")
    void updateProduct(){
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(PRODUCT));

        UpdateProductDto updateDto = new UpdateProductDto("NewTitle", "NewDescription", 120, "EXIST");
        Product updateProduct = new Product(1L, "NewTitle", "NewDescription", 120, ProductState.EXIST);
        Mockito.when(productRepository.update(updateProduct)).thenReturn(updateProduct);

        Assertions.assertDoesNotThrow(() -> productService.update(1L,updateDto));

        Mockito.verify(productRepository, Mockito.times(1)).update(updateProduct);
    }

    @Test
    @DisplayName("Test for delete product")
    void deleteProduct(){
        Mockito.when(productRepository.delete(1L)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> productService.delete(1L));

        Mockito.verify(productRepository, Mockito.times(1)).delete(1L);
    }

}
