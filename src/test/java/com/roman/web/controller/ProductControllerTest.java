package com.roman.web.controller;

import com.roman.dao.entity.Product;
import com.roman.dao.entity.ProductState;
import com.roman.dao.repository.ProductRepository;
import com.roman.service.dto.CreateProductDto;
import com.roman.service.dto.UpdateProductDto;
import com.roman.service.exception.ExceptionMessage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @ParameterizedTest
    @DisplayName("Test /api/product POST create query")
    @MethodSource("argumentsForCreateProductTest")
    void createProductQuery(CreateProductDto dto, String expectedState, int expectedId) throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title" : "%s",
                          "description" : "%s",
                          "cost" : "%d",
                          "inStock" : "%s"
                        }
                        """.formatted(dto.getTitle(),dto.getDescription(),dto.getCost(),dto.getInStock())));


        actions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is(dto.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(dto.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost", Matchers.is(dto.getCost())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inStock", Matchers.is(expectedState)));
    }

    static Stream<Arguments> argumentsForCreateProductTest(){
        return Stream.of(
                Arguments.of(new CreateProductDto("Mobile","desc",100,"EXIST"),"EXIST",1),
                Arguments.of(new CreateProductDto("Mobile1","desc",100,null),"NOT_EXIST",2),
                Arguments.of(new CreateProductDto("Mobile2","desc",100,"NOT_EXIST"),"NOT_EXIST",3)
        );
    }

    @ParameterizedTest
    @DisplayName("Test /api/product POST create query with wrong parameters")
    @MethodSource("argumentForCreateProductWithWrongParameters")
    void createProductWithWrongParameters(CreateProductDto dto, String expectedExceptionMessage) throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title" : "%s",
                          "description" : "%s",
                          "cost" : "%d",
                          "inStock" : "%s"
                        }
                        """.formatted(dto.getTitle(),dto.getDescription(),dto.getCost(),dto.getInStock())));

        actions.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(expectedExceptionMessage)));
    }

    static Stream<Arguments> argumentForCreateProductWithWrongParameters(){
        return Stream.of(
                Arguments.of(new CreateProductDto("","desc",100,"EXIST"), ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto(range(0, 256).mapToObj(i -> "a").collect(joining()), "desc",100,"EXIST"), ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", "",100,"EXIST"), ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", range(0, 4097).parallel().mapToObj(i -> "a").collect(joining()),100,"EXIST"), ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(new CreateProductDto("Mobile phone", "desc",-1,"EXIST"), ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE)
        );
    }

    @Test
    @DisplayName("Test for /api/product/{id} GET find query")
    void findByIdMethodTest() throws Exception {
        productRepository.save(new Product(1L, "Mobile Phone", "desc", 100, ProductState.EXIST));
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + 1));

        actions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()",Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",Matchers.is("Mobile Phone")));

    }

    @Test
    @DisplayName("Test for /api/product/{id} GET find query without exist product")
    void findByIdMethodWithoutExistProduct() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/" + 100));

        actions
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result",Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is(ExceptionMessage.PRODUCT_DOESNT_EXIST_EXCEPTION_MESSAGE.formatted(100))));
    }

    @Test
    @DisplayName("Test for /api/product GET find all query")
    void findAllProducts() throws Exception {
        productRepository.save(new Product(1L, "Mobile Phone", "desc", 100, ProductState.EXIST));
        productRepository.save(new Product(2L, "Mobile Phone 2", "desc", 100, ProductState.EXIST));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
    }

    @Test
    @DisplayName("Test /api/product/{id} PATCH update query")
    void updateProduct() throws Exception {
        productRepository.save(new Product(1L, "Mobile Phone", "desc", 100, ProductState.EXIST));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title" : "Mobile phone 1",
                          "description" : "Description",
                          "cost" : 111,
                          "inStock" : "EXIST"
                        }
                        """));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Mobile phone 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Description")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost", Matchers.is(111)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.inStock", Matchers.is("EXIST")));
    }

    @ParameterizedTest
    @DisplayName("Test /api/product/{id} PATCH update query with wrong parameters")
    @MethodSource("argumentForUpdateProductWithWrongParameters")
    void updateProductWithWrongParameters(Long id, int status, UpdateProductDto dto, String expectedExceptionMessage) throws Exception {
        productRepository.save(new Product(1L, "Mobile Phone", "desc", 100, ProductState.EXIST));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "title" : "%s",
                          "description" : "%s",
                          "cost" : "%d",
                          "inStock" : "%s"
                        }
                        """.formatted(dto.getTitle(),dto.getDescription(),dto.getCost(),dto.getInStock())));

        actions.andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result", Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(expectedExceptionMessage)));
    }

    static Stream<Arguments> argumentForUpdateProductWithWrongParameters(){
        return Stream.of(
                Arguments.of(1L,400,new UpdateProductDto("","desc",100,"EXIST"), ExceptionMessage.PRODUCT_TITLE_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto(range(0, 256).mapToObj(i -> "a").collect(joining()), "desc",100,"EXIST"), ExceptionMessage.PRODUCT_TITLE_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", "",100,"EXIST"), ExceptionMessage.PRODUCT_DESCRIPTION_EMPTY_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", range(0, 4097).parallel().mapToObj(i -> "a").collect(joining()),100,"EXIST"), ExceptionMessage.PRODUCT_DESCRIPTION_LENGTH_EXCEPTION_MESSAGE),
                Arguments.of(1L,400,new UpdateProductDto("Mobile phone", "desc",-1,"EXIST"), ExceptionMessage.PRODUCT_COST_EXCEPTION_MESSAGE),
                Arguments.of(100L,404,new UpdateProductDto("Mobile phone", "desc",100,"EXIST"), ExceptionMessage.PRODUCT_DOESNT_EXIST_EXCEPTION_MESSAGE.formatted(100))
        );
    }

    @Test
    @DisplayName("Test for /api/product/{id} DELETE product")
    void deleteProductById() throws Exception {
        productRepository.save(new Product(1L, "Mobile Phone", "desc", 100, ProductState.EXIST));

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(1)));
    }

    @Test
    @DisplayName("Test for /api/product/{id} DELETE product without exist product")
    void deleteProductByIdWithoutExistProduct() throws Exception {
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/100"));

        actions.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result",Matchers.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",Matchers.is(ExceptionMessage.PRODUCT_DOESNT_EXIST_EXCEPTION_MESSAGE.formatted(100))));
    }

}
