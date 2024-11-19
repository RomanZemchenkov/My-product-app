package com.roman.service;

import com.roman.dao.entity.Delivery;
import com.roman.dao.entity.Product;
import com.roman.dao.entity.ProductState;
import com.roman.dao.repository.DeliveryRepository;
import com.roman.dao.repository.product.ProductRepository;
import com.roman.service.dto.delivery.CreateDeliveryDto;
import com.roman.service.dto.delivery.ShowDeliveryDto;
import com.roman.service.exception.ProductDoesntExistException;
import com.roman.service.mapper.DeliveryMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DeliveryMapper deliveryMapper;

    @Mock
    private DeliveryRepository deliveryRepository;


    @Test
    @DisplayName("Test for add new delivery method")
    void addNewDeliveryTest(){
        String productTitle = "Mobile 11";
        String deliveryTitle = "Mobile 11 delivery";
        Integer count = 11;
        CreateDeliveryDto delivery = new CreateDeliveryDto(productTitle, deliveryTitle, count);
        Delivery newDelivery = new Delivery(deliveryTitle,count);
        newDelivery.setId(11L);
        Product existProduct = new Product(11L, productTitle, "Mobile", 9999, ProductState.EXIST, 5);
        Mockito.when(productRepository.findProductByTitle(deliveryTitle)).thenReturn(Optional.of(existProduct));
        Mockito.when(deliveryMapper.mapToDelivery(delivery)).thenReturn(newDelivery);
        Mockito.when(deliveryRepository.save(newDelivery)).thenReturn(newDelivery);

        boolean result = assertDoesNotThrow(() -> deliveryService.addNewDelivery(delivery));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test for add new delivery method with not exist product")
    void addNewDeliveryTestWithNotExistProduct(){
        String productTitle = "Mobile 11";
        String deliveryTitle = "Mobile 11 delivery";
        Integer count = 11;
        CreateDeliveryDto delivery = new CreateDeliveryDto(productTitle, deliveryTitle, count);
        Mockito.when(productRepository.findProductByTitle(deliveryTitle)).thenReturn(Optional.empty());

        assertThrows(ProductDoesntExistException.class,() -> deliveryService.addNewDelivery(delivery));
    }

    @Test
    @DisplayName("Test for find all deliveries for one product")
    void findAllDeliveries(){
        String productTitle = "Mobile";
        Product existProduct = new Product(11L, productTitle, "Mobile", 9999, ProductState.EXIST, 5);
        Mockito.when(productRepository.findProductByTitle(productTitle)).thenReturn(Optional.of(existProduct));
        Mockito.when(deliveryRepository.findAllByProductTitle(productTitle)).thenReturn(List.of());

        List<ShowDeliveryDto> showDeliveryDtos = assertDoesNotThrow(() -> deliveryService.findAllDeliveriesForOneProduct(productTitle));
        assertThat(showDeliveryDtos).hasSize(0);
    }

    @Test
    @DisplayName("Test for find all deliveries for one not exist product")
    void findAllDeliveriesForNotExistProduct(){
        String productTitle = "Mobile";
        Mockito.when(productRepository.findProductByTitle(productTitle)).thenReturn(Optional.empty());

        assertThrows(ProductDoesntExistException.class,() -> deliveryService.findAllDeliveriesForOneProduct(productTitle));
    }

    @Test
    @DisplayName("Test for find delivery by id")
    void findDeliveryById(){
        Long deliveryId = 1L;
        Delivery delivery = new Delivery("Mobile delivery", 10);
        delivery.setId(deliveryId);
        Mockito.when(deliveryRepository.findDeliveryWithProductById(deliveryId)).thenReturn(Optional.of(delivery));
        Mockito.when(deliveryMapper.mapToShow(delivery)).thenReturn(new ShowDeliveryDto(1L,"Mobile delivery","Mobile",10));

        assertDoesNotThrow(() -> deliveryService.findDeliveryById(deliveryId));

        Mockito.verify(deliveryRepository, Mockito.times(1)).findDeliveryWithProductById(deliveryId);
    }
}
