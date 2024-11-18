package com.roman.service;

import com.roman.dao.entity.Delivery;
import com.roman.dao.entity.Product;
import com.roman.dao.repository.DeliveryRepository;
import com.roman.dao.repository.product.ProductRepository;
import com.roman.service.dto.delivery.CreateDeliveryDto;
import com.roman.service.exception.ProductDoesntExistException;
import com.roman.service.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final ProductRepository productRepository;

    public boolean addNewDelivery(CreateDeliveryDto dto){
        String productTitle = dto.getProductTitle();
        Optional<Product> mayBeProduct = productRepository.findProductByTitle(productTitle);
        if(mayBeProduct.isEmpty()){
            throw new ProductDoesntExistException(productTitle);
        }

        Delivery delivery = deliveryMapper.mapToDelivery(dto);
        Product currentProduct = mayBeProduct.get();
        delivery.setProduct(currentProduct);

        deliveryRepository.save(delivery);
        productRepository.updateCountInStock(currentProduct.getCountInStock() + dto.getCount(),currentProduct.getId());
        return true;
    }
}
