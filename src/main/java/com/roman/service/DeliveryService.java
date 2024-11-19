package com.roman.service;

import com.roman.dao.entity.Delivery;
import com.roman.dao.entity.Product;
import com.roman.dao.repository.DeliveryRepository;
import com.roman.dao.repository.product.ProductRepository;
import com.roman.service.dto.delivery.CreateDeliveryDto;
import com.roman.service.dto.delivery.ShowDeliveryDto;
import com.roman.service.exception.DeliveryDoesntExistException;
import com.roman.service.exception.ProductDoesntExistException;
import com.roman.service.mapper.DeliveryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final ProductRepository productRepository;

    @Transactional
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

    public List<ShowDeliveryDto> findAllDeliveriesForOneProduct(String productTitle){
        Optional<Product> mayBeProduct = productRepository.findProductByTitle(productTitle);
        if(mayBeProduct.isEmpty()){
            throw new ProductDoesntExistException(productTitle);
        }
        List<Delivery> deliveries = deliveryRepository.findAllByProductTitle(productTitle);
        return deliveries.stream().map(del -> deliveryMapper.mapToShow(del, productTitle)).toList();
    }

    public ShowDeliveryDto findDeliveryById(Long deliveryId){
        Optional<Delivery> mayBeDelivery = deliveryRepository.findDeliveryWithProductById(deliveryId);
        if(mayBeDelivery.isEmpty()){
            throw new DeliveryDoesntExistException(deliveryId);
        }
        return mayBeDelivery.map(deliveryMapper::mapToShow).get();
    }
}
