package com.store.shopping_cart.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.repo.ProductRepository;

@SpringBootTest
@Transactional
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ProductServiceTest {
    

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    private Product productA;
    private Product productB;


    @BeforeEach
    void setup() {

        // Clear existing data
        productRepository.deleteAll();

        productA = new Product();
        productA.setName("Product A");
        productA.setDescription("Description A");
        productA.setPrice(10.0);
        productRepository.save(productA);

        productB = new Product();
        productB.setName("Product B");
        productB.setDescription("Description B");
        productB.setPrice(10.0);
        productRepository.save(productB);
    }

    public Product createDummyProduct(String name, String description, double price) {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            return product;
    }
    
    @Test
    void testGetProducts() {
        List<Product> all = productService.getProducts();
        assertThat(all).hasSize(2)
                        .extracting(Product::getName)
                        .containsExactlyInAnyOrder("Product A", "Product B");
    }

    @Test
    void testGetProduct() {
        Product existing = productRepository.findAll().get(0);
        Product fetched = productService.getProduct(existing.getProductid());
        assertThat(fetched.getName()).isEqualTo(existing.getName());
    }
    

    @Test
    void testProductExists() {
        Integer id = productRepository.findAll().get(0).getProductid();
        boolean productExists = productRepository.existsById(id);
        assertTrue(productExists);
        assertFalse(productRepository.existsById(9001));
    }

    @Test
    void testUpdateProduct() {
        Product newProduct = createDummyProduct("product", "description", 1);
        int id = productService.createProduct(newProduct).getProductid();

        Product productUpdate = productService.getProduct(id);
        productUpdate.setName("New product name");
        productUpdate.setDescription("New product description");
        productUpdate.setPrice(99);
        productService.updateProduct(id, productUpdate);

        Product productNew = productService.getProduct(id);
        assertTrue(productNew.getName().equals("New product name"));
        assertTrue(productNew.getDescription().equals("New product description"));
        assertTrue(productNew.getPrice() == 99);

    }

    @Test
    void testCreateProduct() {
        Product productC = new Product();
        productC.setName("Product C");
        productC.setDescription("C description");
        productC.setPrice(9.99);

        productService.createProduct(productC);
        Product productCompare = productService.getProduct(productC.getProductid());
        assertTrue(productCompare.getName().equals("Product C"));
        assertTrue(productCompare.getDescription().equals("C description"));
        assertTrue(productCompare.getPrice() == 9.99);
    }

    @Test
    void testDeleteProduct() {

        productService.deleteProduct(1);
        assertTrue(!productService.productExists(1));

    }
   
}