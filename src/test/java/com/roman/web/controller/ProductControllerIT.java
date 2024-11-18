package com.roman.web.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roman.PostgresContainerInitializer;
import com.roman.service.dto.product.CreateProductDto;
import com.roman.service.dto.product.ShowProductDto;
import com.roman.service.dto.product.UpdateProductDto;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest(
        properties = {
                "spring.jpa.properties.hibernate.show_sql = false",
                "spring.jpa.properties.hibernate.format_sql = false"
        }
)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ProductControllerIT extends PostgresContainerInitializer {

    private final MockMvc mockMvc;
    private final EntityManager entityManager;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int countOfProductVariables = 4;

    @Autowired
    public ProductControllerIT(MockMvc mockMvc, EntityManager entityManager) {
        this.mockMvc = mockMvc;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("Several product add and try to find by filter without sort")
    void addAndFindProductWithoutSort() throws Exception {
        int countOfProducts = 10;

        List<CreateProductDto> products = createProductGenerator(countOfProducts);
        for (CreateProductDto product : products) {
            String productAsString = objectMapper.writeValueAsString(product);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productAsString));
        }

        Long result = entityManager.createQuery("SELECT count(p) FROM Product p", Long.class).getSingleResult();
        Assertions.assertThat(result).isEqualTo(countOfProducts * countOfProductVariables);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byFilter")
                .param("title", "Mobile"));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(10)));

        ResultActions titleAndTwoPageQuery = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byFilter")
                .param("title", "Mobile")
                .param("size", "4")
                .param("page", "1"));

        titleAndTwoPageQuery
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)));

        ResultActions titleAndThreePageQuery = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byFilter")
                .param("title", "Mobile")
                .param("size", "4")
                .param("page", "2"));

        titleAndThreePageQuery
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

        ResultActions findTableWithMaxAndMinCost = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byFilter")
                .param("title", "Table")
                .param("costMin", "3000")
                .param("costMax", "5000"));

        findTableWithMaxAndMinCost.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(5)));

        deleteAll(mockMvc, countOfProducts);

        ResultActions findAllAfterDelete = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"));

        findAllAfterDelete.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.empty()));

    }

    @Test
    @DisplayName("Forty product add and try to find Mobile by filter and with sort")
    void findMobileByFilterAndWithSort() throws Exception {
        int countOfProducts = 10;

        List<CreateProductDto> products = createProductGenerator(countOfProducts);
        for (CreateProductDto product : products) {
            String productAsString = objectMapper.writeValueAsString(product);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productAsString));
        }

        Long result = entityManager.createQuery("SELECT count(p) FROM Product p", Long.class).getSingleResult();
        Assertions.assertThat(result).isEqualTo(countOfProducts * countOfProductVariables);

        ResultActions phones = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byFilter")
                .param("title", "Mobile")
                .param("costMin", "3000")
                .param("costMax", "6000")
                .param("inStock", "EXIST")
                .param("sort", "cost")
                .param("direction", "DESC"));

        phones.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].cost", Matchers.is(5000)));

        deleteAll(mockMvc, countOfProducts);

        ResultActions findAllAfterDelete = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"));

        findAllAfterDelete.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.empty()));
    }

    @Test
    @DisplayName("Four product add and try to update and find updated product")
    void addUpdateAndFindProduct() throws Exception {
        int countOfProducts = 1;

        List<CreateProductDto> products = createProductGenerator(countOfProducts);
        for (CreateProductDto product : products) {
            String productAsString = objectMapper.writeValueAsString(product);
            mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productAsString));
        }

        Long result = entityManager.createQuery("SELECT count(p) FROM Product p", Long.class).getSingleResult();
        Assertions.assertThat(result).isEqualTo(countOfProducts * countOfProductVariables);

        ResultActions findAllProducts = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"));
        byte[] allProductsByArray = findAllProducts.andReturn().getResponse().getContentAsByteArray();
        JavaType showProductType = objectMapper.getTypeFactory().constructCollectionType(List.class, ShowProductDto.class);
        List<ShowProductDto> list = objectMapper.readValue(allProductsByArray, showProductType);
        int firstProductId = (int) list.get(0).getId();

        UpdateProductDto updateMobile = new UpdateProductDto("Mobile 1", "New desc", 17999, 73);

        ResultActions updateQuery = mockMvc.perform(MockMvcRequestBuilders.patch("/api/products/" + firstProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateMobile)));

        updateQuery.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(firstProductId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost",Matchers.is(17999)));

        Long secondResult = entityManager.createQuery("SELECT count(p) FROM Product p", Long.class).getSingleResult();
        Assertions.assertThat(result).isEqualTo(secondResult);

        deleteAll(mockMvc, countOfProducts);

        ResultActions findAllAfterDelete = mockMvc.perform(MockMvcRequestBuilders.get("/api/products"));

        findAllAfterDelete.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.empty()));
    }

    private static List<CreateProductDto> createProductGenerator(int count) {
        List<CreateProductDto> products = new ArrayList<>();
        IntStream.range(1, count + 1).forEach(i -> {
            products.add(new CreateProductDto("Mobile " + i, "Description", i * 1000, i % 3 == 0 ? 0 : i));
            products.add(new CreateProductDto("Table " + i, "Description", i * 500, i % 3 == 0 ? 0 : i));
            products.add(new CreateProductDto("Spoon " + i, "Description", i * 40, i % 3 == 0 ? 0 : i));
            products.add(new CreateProductDto("Mouse " + i, "Description", i * 200, i % 3 == 0 ? 0 : i));
        });
        return products;
    }

    private static void deleteAll(MockMvc mockMvc, int countOfProducts) throws Exception {
        ResultActions findFirstProduct = mockMvc.perform(MockMvcRequestBuilders.get("/api/products/byFilter")
                .param("direction", "ASC")
                .param("size", "1")
                .param("page", "0"));

        String firstProduct = findFirstProduct.andReturn().getResponse().getContentAsString();
        JavaType showProductType = objectMapper.getTypeFactory().constructCollectionType(List.class, ShowProductDto.class);
        List<ShowProductDto> list = objectMapper.readValue(firstProduct, showProductType);
        int id = (int) list.get(0).getId();

        for (int i = id; i < countOfProducts * countOfProductVariables + 1 + id; i++) {
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/" + i));
        }
    }
}
