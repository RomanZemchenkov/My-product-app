package com.roman.service;

import com.roman.dao.entity.Product;
import com.roman.dao.repository.product.ProductRepository;
import com.roman.dao.repository.product.ProductSort;
import com.roman.dao.repository.product.ProductSpecification;
import com.roman.service.dto.product.CreateProductDto;
import com.roman.service.dto.product.FilterProductDto;
import com.roman.service.dto.product.ShowProductDto;
import com.roman.service.dto.product.SortProductDto;
import com.roman.service.dto.product.UpdateProductDto;
import com.roman.service.exception.ProductDoesntExistException;
import com.roman.service.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductSpecification specification = new ProductSpecification();
    private final ProductSort productSort = new ProductSort();

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ShowProductDto addNewProduct(CreateProductDto dto) {
        Product product = productMapper.mapToProduct(dto);
        Product savedProduct = productRepository.save(product);
        return productMapper.mapToShow(savedProduct);
    }

    public List<ShowProductDto> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::mapToShow)
                .toList();
    }

    public List<ShowProductDto> findProductByFilter(FilterProductDto filter, SortProductDto sort, int page, int size){
        Specification<Product> productSpecification = specification.buildSpecification(filter);
        PageRequest sortParameters = productSort.createSortParameters(sort, page, size);
        Page<Product> products = productRepository.findAll(productSpecification, sortParameters);
        return products.stream().map(productMapper::mapToShow).toList();
    }

    public ShowProductDto findById(Long id) {
        return productMapper.mapToShow(find(id));
    }

    @Transactional
    public ShowProductDto update(Long id, UpdateProductDto dto) {
        Product product = find(id);
        Product productBeforeUpdate = productMapper.mapToProduct(dto, product);
        Product updatedProduct = productRepository.save(productBeforeUpdate);
        return productMapper.mapToShow(updatedProduct);
    }

    @Transactional
    public Long delete(Long id) {
        find(id);
        productRepository.deleteById(id);
        return id;
    }

    private Product find(Long id) {
        Optional<Product> mayBeProduct = productRepository.findById(id);
        if (mayBeProduct.isEmpty()) {
            throw new ProductDoesntExistException(id);
        }
        return mayBeProduct.get();
    }
}
