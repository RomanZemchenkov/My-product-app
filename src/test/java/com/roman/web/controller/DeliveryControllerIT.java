package com.roman.web.controller;

import com.roman.PostgresContainerInitializer;
import com.roman.dao.entity.Delivery;
import com.roman.dao.entity.Product;
import com.roman.service.dto.delivery.CreateDeliveryDto;
import jakarta.persistence.EntityManager;
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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeliveryControllerIT extends PostgresContainerInitializer {

    private final MockMvc mockMvc;
    private final EntityManager entityManager;
    private final static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public DeliveryControllerIT(MockMvc mockMvc, EntityManager entityManager) {
        this.mockMvc = mockMvc;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("Test for /api/delivery POST add new delivery")
    void addNewDeliveryTest() throws Exception {
        String productTitle = "Mobile 1";
        Product product = entityManager.createQuery("SELECT p FROM Product p WHERE p.title LIKE :product", Product.class)
                .setParameter("product", productTitle)
                .getSingleResult();

        assertThat(product.getCountInStock()).isEqualTo(0);

        CreateDeliveryDto newDelivery = new CreateDeliveryDto("Mobile 1 new delivery", productTitle, 11);
        addNewDelivery(newDelivery);

        Product updateProduct = entityManager.createQuery("SELECT p FROM Product p WHERE p.title LIKE :product", Product.class)
                .setParameter("product",  productTitle)
                .getSingleResult();

        assertThat(updateProduct.getCountInStock()).isEqualTo(11);
    }

    @Test
    @DisplayName("Test for /api/delivery GET find all by product title")
    void findAllByProductTitle() throws Exception {
        String productTitle = "Mobile 2";
        Long count = entityManager.createQuery("SELECT count(d) FROM Delivery d WHERE d.product.title LIKE :product", Long.class)
                .setParameter("product", productTitle)
                .getSingleResult();

        assertThat(count).isEqualTo(0);

        CreateDeliveryDto newDelivery1 = new CreateDeliveryDto("Mobile 2 new delivery1", productTitle, 11);
        CreateDeliveryDto newDelivery2 = new CreateDeliveryDto("Mobile 2 new delivery 2", productTitle, 10);
        addNewDelivery(newDelivery1);
        addNewDelivery(newDelivery2);

        ResultActions findQuery = mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery")
                .param("product", productTitle));

        findQuery.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Test for /api/delivery/{id} GET find delivery by id")
    void findDeliveryById() throws Exception {
        String productTitle = "Mobile 3";

        CreateDeliveryDto newDelivery1 = new CreateDeliveryDto("Mobile 3 new delivery 1", productTitle, 11);
        CreateDeliveryDto newDelivery2 = new CreateDeliveryDto("Mobile 3 new delivery 2", productTitle, 10);
        addNewDelivery(newDelivery1);
        addNewDelivery(newDelivery2);

        List<Delivery> countDelivery = entityManager.createQuery("SELECT d FROM Delivery d WHERE d.product.title = :productTitle", Delivery.class)
                .setParameter("productTitle",productTitle)
                .getResultList();
        assertThat(countDelivery).hasSize(2);

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get("/api/delivery/" + countDelivery.get(0).getId()));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title",Matchers.is("Mobile 3 new delivery 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productTitle",Matchers.is(productTitle)));
    }



    private void addNewDelivery(CreateDeliveryDto dto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)));
    }
}
