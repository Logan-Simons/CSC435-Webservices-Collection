package com.store.shopping_cart.integration;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.store.shopping_cart.integration.base.AbstractIntegrationTest;
import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.repo.ProductRepository;

import jakarta.annotation.PostConstruct;

class ProductIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE = "/store/products";

    @Autowired
    private ProductRepository productRepository;

    private Product productDTO;

    @PostConstruct
    void initDto() {
        productDTO = new Product();
        productDTO.setName("Test Product");
        productDTO.setDescription("Test Description");
        productDTO.setPrice(123.45);
    }

    @BeforeEach
    void cleanDb() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("POST  /store/products/ → 200 + created product")
    void createProduct_HappyPath() throws Exception {
        Product created = performPostRequestExpectedSuccess(
            BASE + "/", productDTO, Product.class
        );

        assertThat(created.getProductid()).isNotNull();
        assertThat(created.getName()).isEqualTo("Test Product");
        assertThat(created.getDescription()).isEqualTo("Test Description");
        assertThat(created.getPrice()).isEqualTo(123.45);
    }

    @Test
    @DisplayName("GET  /store/products → 200 + list of products")
    void getAllProducts_WhenExist() throws Exception {

        Product productA = new Product();
        Product productB = new Product();
        productA.setName("A");
        productB.setName("B");
        productRepository.save(productA);
        productRepository.save(productB);

        mockMvc.perform(get(BASE))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[?(@.name=='A')]").exists())
               .andExpect(jsonPath("$[?(@.name=='B')]").exists());
    }

    @Test
    @DisplayName("GET  /store/products/{id} → 404 if missing")
    void getById_NotFound() throws Exception {
        mockMvc.perform(get(BASE + "/{id}", 9999))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Product does not exist"));
    }

    @Test
    @DisplayName("GET  /store/products/{id} → 200 if exists")
    void getById_Found() throws Exception {
        Product saved = productRepository.save(productDTO);

        mockMvc.perform(get(BASE + "/{id}", saved.getProductid()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.productid").value(saved.getProductid()))
               .andExpect(jsonPath("$.name").value("Test Product"))
               .andExpect(jsonPath("$.price").value(123.45));
    }

    @Test
    @DisplayName("PUT  /store/products/{id}/ → 200 + updated product")
    void updateProduct_Found() throws Exception {
        Product saved = productRepository.save(productDTO);

        Product update = new Product();
        update.setName("Updated Name");
        update.setDescription("Updated Desc");
        update.setPrice(999.0);

        mockMvc.perform(put(BASE + "/{id}/", saved.getProductid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(update)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.productid").value(saved.getProductid()))
               .andExpect(jsonPath("$.name").value("Updated Name"))
               .andExpect(jsonPath("$.price").value(999.0));
    }

    @Test
    @DisplayName("PUT  /store/products/{id}/ → 204 if missing")
    void updateProduct_NotFound() throws Exception {
        mockMvc.perform(put(BASE + "/{id}/", 4242)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productDTO)))
               .andExpect(status().isNoContent())
               .andExpect(content().string("Product does not exist"));
    }

    @Test
    @DisplayName("DELETE  /store/products/{id} → 200 + confirmation")
    void deleteProduct_Found() throws Exception {
        Product saved = productRepository.save(productDTO);
        Integer id = saved.getProductid();

        mockMvc.perform(delete(BASE + "/{id}", id))
               .andExpect(status().isOk())
               .andExpect(content().string("Product " + id + " has been deleted."));

        // verify it’s gone
        mockMvc.perform(get(BASE + "/{id}", id))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE  /store/products/{id} → 404 if missing")
    void deleteProduct_NotFound() throws Exception {
        mockMvc.perform(delete(BASE + "/{id}", 5555))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Product does not exist"));
    }
}
