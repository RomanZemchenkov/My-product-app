package com.roman.web.controller;

import com.roman.service.DeliveryService;
import com.roman.service.dto.delivery.CreateDeliveryDto;
import com.roman.service.exception.ProductDoesntExistException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {DeliveryController.class})
public class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test for /api/delivery POST add new delivery")
    void addNewDelivery() throws Exception {
        CreateDeliveryDto delivery = new CreateDeliveryDto("Mobile delivery 122", "Mobile 11", 120);
        Mockito.when(deliveryService.addNewDelivery(delivery)).thenReturn(true);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(delivery)));

        actions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is("Товар успешно доставлен")));
    }

    @ParameterizedTest
    @DisplayName("Test for /api/delivery POST add new delivery with wrong parameters")
    @MethodSource("com.roman.web.controller.DeliveryArgumentsProvider#argumentsForAddNewDeliveryWithWrongParameters")
    void addNewDeliveryWithWrongParameters(CreateDeliveryDto dto, String expectedExceptionMessage) throws Exception {
        Mockito.when(deliveryService.addNewDelivery(dto)).thenThrow(new ProductDoesntExistException(dto.getProductTitle()));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        actions.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(expectedExceptionMessage)));
    }
}
