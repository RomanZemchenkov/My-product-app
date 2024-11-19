package com.roman.web.controller;

import com.roman.service.SaleService;
import com.roman.service.dto.sale.CreateSaleDto;
import com.roman.web.response.NormalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sale")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    public ResponseEntity<NormalResponse> addNewSale(@RequestBody @Validated CreateSaleDto dto){
        boolean result = saleService.addNewSave(dto);
        NormalResponse response = new NormalResponse(result, "Продажа успешна.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
