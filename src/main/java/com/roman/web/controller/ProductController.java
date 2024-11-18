package com.roman.web.controller;

import com.roman.service.ProductService;
import com.roman.service.dto.CreateProductDto;
import com.roman.service.dto.FilterProductDto;
import com.roman.service.dto.ShowProductDto;
import com.roman.service.dto.SortProductDto;
import com.roman.service.dto.UpdateProductDto;
import com.roman.service.exception.ExceptionMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.roman.service.exception.ExceptionMessage.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ShowProductDto> create(@RequestBody @Validated CreateProductDto dto) {
        ShowProductDto savedProduct = productService.addNewProduct(dto);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowProductDto> findById(@PathVariable("id") Long productId) {
        ShowProductDto existProduct = productService.findById(productId);
        return new ResponseEntity<>(existProduct, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ShowProductDto>> findAll() {
        List<ShowProductDto> allProducts = productService.findAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/byFilter")
    @Validated
    public ResponseEntity<List<ShowProductDto>> findAllBy(@RequestParam(name = "title", required = false) @Length(max = 255, message = PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE) String title,
                                                          @RequestParam(name = "cost", required = false) @Min(value = 0, message = PRODUCT_COST_EXCEPTION_MESSAGE) Integer cost,
                                                          @RequestParam(name = "costMin", required = false) @Min(value = 0, message = PRODUCT_COST_EXCEPTION_MESSAGE) Integer costMin,
                                                          @RequestParam(name = "costMax", required = false) @Min(value = 0, message = PRODUCT_COST_EXCEPTION_MESSAGE) Integer costMax,
                                                          @RequestParam(name = "inStock", required = false) String inStock,
                                                          @RequestParam(name = "sort", required = false) String sortBy,
                                                          @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction,
                                                          @RequestParam(name = "page", required = false, defaultValue = "0") @Min(value = 0, message = PAGE_NUMBER_EXCEPTION_MESSAGE) Integer page,
                                                          @RequestParam(name = "size", required = false, defaultValue = "12") @Min(value = 1, message = SIZE_PAGE_EXCEPTION_MESSAGE) Integer size){
        FilterProductDto filter = new FilterProductDto.Builder()
                .setCost(cost)
                .setCostMin(costMin)
                .setCostMax(costMax)
                .setTitle(title)
                .setInStock(inStock)
                .build();
        SortProductDto sort = new SortProductDto.Builder()
                .setSortBy(sortBy)
                .setOrderBy(direction)
                .build();

        List<ShowProductDto> products = productService.findProductByFilter(filter, sort, page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ShowProductDto> updateProduct(@PathVariable("id") Long productId,
                                                        @RequestBody @Validated UpdateProductDto dto) {
        ShowProductDto updatedProduct = productService.update(productId, dto);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteProduct(@PathVariable("id") Long productId){
        Long resultId = productService.delete(productId);
        return new ResponseEntity<>(resultId, HttpStatus.OK);
    }
}
