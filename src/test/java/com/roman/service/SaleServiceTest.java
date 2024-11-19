package com.roman.service;

import com.roman.dao.entity.Product;
import com.roman.dao.entity.ProductState;
import com.roman.dao.entity.Sale;
import com.roman.dao.repository.SaleRepository;
import com.roman.dao.repository.product.ProductRepository;
import com.roman.service.dto.sale.CreateSaleDto;
import com.roman.service.exception.ProductDoesntExistException;
import com.roman.service.exception.SaleException;
import com.roman.service.mapper.SaleMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SaleMapper saleMapper;

    @Test
    @DisplayName("Test for add new sale")
    void addNewSale(){
        String productTitle = "Mobile";
        Product product = new Product(1L, productTitle, "Desc", 1000, ProductState.EXIST, 10);
        CreateSaleDto mobileSale = new CreateSaleDto("Mobile sale", productTitle, 5);
        Sale sale = new Sale("Mobile sale", 5);
        Mockito.when(productRepository.findProductByTitle(productTitle)).thenReturn(Optional.of(product));
        Mockito.when(saleMapper.mapToSale(mobileSale)).thenReturn(sale);
        Mockito.when(saleRepository.save(sale)).thenReturn(sale);

        Boolean result = assertDoesNotThrow(() -> saleService.addNewSave(mobileSale));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test for add new sale with not exist product")
    void addNewSaleWithNotExistProduct(){
        String productTitle = "Mobile";
        CreateSaleDto mobileSale = new CreateSaleDto("Mobile sale", productTitle, 5);
        Mockito.when(productRepository.findProductByTitle(productTitle)).thenReturn(Optional.empty());

        assertThrows(ProductDoesntExistException.class,() -> saleService.addNewSave(mobileSale));

    }

    @Test
    @DisplayName("Test for add new sale with product count less than sale count")
    void addNewSaleWithWrongSaleCount(){
        String productTitle = "Mobile";
        Product product = new Product(1L, productTitle, "Desc", 1000, ProductState.EXIST, 10);
        CreateSaleDto mobileSale = new CreateSaleDto("Mobile sale", productTitle, 15);
        Mockito.when(productRepository.findProductByTitle(productTitle)).thenReturn(Optional.of(product));

        assertThrows(SaleException.class,() -> saleService.addNewSave(mobileSale));
    }
}
