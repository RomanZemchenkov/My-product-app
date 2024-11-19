package com.roman.service;

import com.roman.dao.entity.Product;
import com.roman.dao.entity.Sale;
import com.roman.dao.repository.SaleRepository;
import com.roman.dao.repository.product.ProductRepository;
import com.roman.service.dto.sale.CreateSaleDto;
import com.roman.service.exception.ProductDoesntExistException;
import com.roman.service.exception.SaleException;
import com.roman.service.mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaleService {
    
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final SaleMapper saleMapper;
    
    @Transactional
    public boolean addNewSave(CreateSaleDto dto){
        String productTitle = dto.getProductTitle();
        Optional<Product> mayBeProduct = productRepository.findProductByTitle(productTitle);
        if(mayBeProduct.isEmpty()){
            throw new ProductDoesntExistException(productTitle);
        }
        Product currentProduct = mayBeProduct.get();
        int remains = checkAvailableCount(currentProduct, dto);
        Sale sale = saleMapper.mapToSale(dto);
        sale.setProduct(currentProduct);
        
        saleRepository.save(sale);
        productRepository.updateCountInStock(remains, currentProduct.getId());
        return true;
    }
    
    private int checkAvailableCount(Product product, CreateSaleDto dto){
        Integer currentCount = product.getCountInStock();
        Integer futureCountSale = dto.getCount();
        int resultCount = currentCount - futureCountSale;
        if (resultCount < 0){
            throw new SaleException(currentCount);
        }
        return currentCount - futureCountSale;
    }
}
