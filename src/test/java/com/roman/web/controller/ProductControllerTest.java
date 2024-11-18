package com.roman.web.controller;

import com.roman.service.ProductService;
import com.roman.service.dto.product.CreateProductDto;
import com.roman.service.dto.product.FilterProductDto;
import com.roman.service.dto.product.ShowProductDto;
import com.roman.service.dto.product.SortProductDto;
import com.roman.service.dto.product.UpdateProductDto;
import com.roman.service.exception.ExceptionMessage;
import com.roman.service.exception.ProductDoesntExistException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;
import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
public class ProductControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @ParameterizedTest
    @DisplayName("Test /api/product POST create query")
    @MethodSource("argumentsForCreateProductTest")
    void createProductQuery(CreateProductDto dto, String expectedState, long id) throws Exception {
        ShowProductDto expectedShowProductDto = new ShowProductDto(id, dto.getTitle(), dto.getDescription(), dto.getCost(), expectedState);
        when(productService.addNewProduct(dto)).thenReturn(expectedShowProductDto);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));


        actions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(dto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost", Matchers.is(dto.getCost())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inStock", Matchers.is(expectedState)));
    }

    static Stream<Arguments> argumentsForCreateProductTest(){
        return Stream.of(
                Arguments.of(new CreateProductDto("Mobile","desc",100,1),"EXIST",1L),
                Arguments.of(new CreateProductDto("Mobile1","desc",100,0),"NOT_EXIST",2L),
                Arguments.of(new CreateProductDto("Mobile2","desc",100,0),"NOT_EXIST",3L)
        );
    }

    @ParameterizedTest
    @DisplayName("Test /api/product POST create query with wrong parameters")
    @MethodSource("argumentForCreateProductWithWrongParameters")
    void createProductWithWrongParameters(CreateProductDto dto, String expectedExceptionMessage) throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(expectedExceptionMessage)));
    }

    static Stream<Arguments> argumentForCreateProductWithWrongParameters(){
        return Stream.of(
                Arguments.of(new CreateProductDto("","desc",100,1), ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto(range(0, 256).mapToObj(i -> "a").collect(joining()), "desc",100,1), ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", "",100,1), ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", range(0, 4097).parallel().mapToObj(i -> "a").collect(joining()),100,1), ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", "desc",-1,1), ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", "desc",100,-1), ExceptionMessage.PRODUCT_COUNT_IN_STOCK_EXCEPTION_MESSAGE)
        );
    }

    @Test
    @DisplayName("Test for /api/product/{id} GET find query")
    void findByIdMethodTest() throws Exception {
        ShowProductDto dto = new ShowProductDto(1L, "Mobile Phone", "Desc", 100,"EXIST" );
        Mockito.when(productService.findById(1L)).thenReturn(dto);
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + 1));

        actions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()",Matchers.is(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",Matchers.is("Mobile Phone")));

    }

    @Test
    @DisplayName("Test for /api/product/{id} GET find query without exist product")
    void findByIdMethodWithoutExistProduct() throws Exception {
        Mockito.when(productService.findById(100L)).thenThrow(new ProductDoesntExistException(100L));
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + 100));

        actions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result",Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is(ExceptionMessage.PRODUCT_WITH_ID_EXIST_EXCEPTION_MESSAGE.formatted(100))));
    }

    @Test
    @DisplayName("Test for /api/product GET find all query")
    void findAllProducts() throws Exception {
        ShowProductDto dto1 = new ShowProductDto(1L, "Mobile Phone 1", "Desc", 100,"EXIST");
        ShowProductDto dto2 = new ShowProductDto(2L, "Mobile Phone 2", "Desc", 100,"EXIST");

        Mockito.when(productService.findAllProducts()).thenReturn(List.of(dto1,dto2));
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
    }

    @ParameterizedTest
    @DisplayName("Test for /api/product/byFilter GET find products by filter with wrong parameters")
    @MethodSource("argumentsForFindProductByFilterWithWrongParameters")
    void findProductByFilterWithWrongParameters(String param, String value, String expectedExceptionMessage) throws Exception {
        Mockito.when(productService
                .findProductByFilter(Mockito.any(FilterProductDto.class),Mockito.any(SortProductDto.class),Mockito.anyInt(),Mockito.anyInt()))
                .thenReturn(List.of());

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byFilter")
                .param(param, value));

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result",Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is(expectedExceptionMessage)));
    }

    static Stream<Arguments> argumentsForFindProductByFilterWithWrongParameters(){
        return Stream.of(
                Arguments.of("title", IntStream.range(0,256).mapToObj(i -> "a").collect(joining()),ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of("cost", "-1",ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of("costMin", "-1",ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of("costMax", "-1",ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of("page", "-1",ExceptionMessage.PAGE_NUMBER_EXCEPTION_MESSAGE),
                Arguments.of("size", "0",ExceptionMessage.SIZE_PAGE_EXCEPTION_MESSAGE)
        );
    }

    @Test
    @DisplayName("Test /api/product/{id} PATCH update query")
    void updateProduct() throws Exception {
        UpdateProductDto dto = new UpdateProductDto("Mobile Phone 1", "Description", 111, 1);
        ShowProductDto updatedDto = new ShowProductDto(1L, "Mobile Phone 1", "Description", 111,"EXIST");
        updatedDto.setCountInStock(1);
        Mockito.when(productService.update(1L,dto)).thenReturn(updatedDto);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));


        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Mobile Phone 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost", Matchers.is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inStock", Matchers.is("EXIST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.countInStock", Matchers.is(1)));
    }

    @ParameterizedTest
    @DisplayName("Test /api/product/{id} PATCH update query with wrong parameters")
    @MethodSource("argumentForUpdateProductWithWrongParameters")
    void updateProductWithWrongParameters(Long id, int status, UpdateProductDto dto, String expectedExceptionMessage) throws Exception {
        Mockito.when(productService.update(id,dto)).thenThrow(new ProductDoesntExistException(id));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));


        actions.andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(expectedExceptionMessage)));
    }

    static Stream<Arguments> argumentForUpdateProductWithWrongParameters(){
        return Stream.of(
                Arguments.of(1L,400,new UpdateProductDto("","desc",100,1), ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto(range(0, 256).mapToObj(i -> "a").collect(joining()), "desc",100,1), ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", "",100,1), ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", range(0, 4097).parallel().mapToObj(i -> "a").collect(joining()),100,1), ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", "desc",-1,1), ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of(100L,404,new UpdateProductDto("Mobile phone", "desc",100,1), ExceptionMessage.PRODUCT_WITH_ID_EXIST_EXCEPTION_MESSAGE.formatted(100)),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", "desc",100,-1), ExceptionMessage.PRODUCT_COUNT_IN_STOCK_EXCEPTION_MESSAGE)
        );
    }

    @Test
    @DisplayName("Test for /api/product/{id} DELETE product")
    void deleteProductById() throws Exception {
        Mockito.when(productService.delete(1L)).thenReturn(1L);
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(1)));
    }

    @Test
    @DisplayName("Test for /api/product/{id} DELETE product without exist product")
    void deleteProductByIdWithoutExistProduct() throws Exception {
        Mockito.when(productService.delete(100L)).thenThrow(new ProductDoesntExistException(100L));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/100"));

        actions.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result",Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is(ExceptionMessage.PRODUCT_WITH_ID_EXIST_EXCEPTION_MESSAGE.formatted(100))));
    }

}
