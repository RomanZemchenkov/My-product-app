package com.roman.web.controller;

import com.roman.PostgresContainerInitializer;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;


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
    private static final int countOfProductVariables = 4;

    @Autowired
    public ProductControllerIT(MockMvc mockMvc, EntityManager entityManager) {
        this.mockMvc = mockMvc;
        this.entityManager = entityManager;
    }

    @ParameterizedTest
    @DisplayName("Test for /api/product/byFilter GET with variables filters")
    @MethodSource("com.roman.web.controller.ProductArgumentProvider#argumentForFindWithFilter")
    void findWithFilter(Map<String, String> parameters, int expectedCountResult) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/byFilter");
        parameters.forEach(requestBuilder::param);
        ResultActions actions = mockMvc.perform(requestBuilder);
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(expectedCountResult)));
    }

    @ParameterizedTest
    @DisplayName("Test for /api/product/byFilter GET with variables filters and sort")
    @MethodSource("com.roman.web.controller.ProductArgumentProvider#argumentForFindWithFilterAndSort")
    void findWithFilterAndSort(Map<String, String> parameters, String firstElement) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/byFilter");
        parameters.forEach(requestBuilder::param);
        ResultActions actions = mockMvc.perform(requestBuilder);
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title",Matchers.is(firstElement)));
    }

    @ParameterizedTest
    @DisplayName("Test for /api/product/byFilter GET with variables filters, sort and pageable")
    @MethodSource("com.roman.web.controller.ProductArgumentProvider#argumentForFindWithFilterSortAndPageable")
    void findWithFilterSortAndPageable(Map<String, String> parameters,int expectedCountResult, String firstElement) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/byFilter");
        parameters.forEach(requestBuilder::param);
        ResultActions actions = mockMvc.perform(requestBuilder);
        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(expectedCountResult)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title",Matchers.is(firstElement)));
    }


    @Test
    @DisplayName("Several product add and try to find by filter without sort")
    void addAndFindProductWithoutSort() throws Exception {
        int countOfProducts = 10;
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
    }
}
