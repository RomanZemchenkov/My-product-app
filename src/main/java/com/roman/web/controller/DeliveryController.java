package com.roman.web.controller;

import com.roman.service.DeliveryService;
import com.roman.service.dto.delivery.CreateDeliveryDto;
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
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<NormalResponse> addNewDelivery(@RequestBody @Validated CreateDeliveryDto dto){
        deliveryService.addNewDelivery(dto);
        NormalResponse response = new NormalResponse(true, "Товар успешно доставлен");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
