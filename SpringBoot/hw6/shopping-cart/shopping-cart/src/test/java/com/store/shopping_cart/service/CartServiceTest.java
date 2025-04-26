package com.store.shopping_cart.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.store.shopping_cart.model.Cart;
import com.store.shopping_cart.model.CartItem;
import com.store.shopping_cart.model.Product;
import com.store.shopping_cart.repo.CartRepository;
import com.store.shopping_cart.repo.ProductRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class CartServiceTest {

    @Autowired
    CartService cartService = new CartService();
    
    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    Product productA;
    Product productB;
    
    // create Cart
    @BeforeEach
    void setup() {
        cartRepository.deleteAll();
        productRepository.deleteAll();

        productA = new Product();
        productA.setName("Product A");
        productA.setDescription("Description A");
        productA.setPrice(10.0);
        productService.createProduct(productA);

        productB = new Product();
        productB.setName("Product B");
        productB.setDescription("Description B");
        productB.setPrice(10.0);
        productService.createProduct(productB);

        // create & save a new cart
        Cart cart = new Cart();
        Cart savedCart = cartRepository.save(cart);
        assertNotNull(savedCart.getCartId(), "Cart should have been saved and given an ID");

    }

    @Test
    void testCartCreate() {

        Cart newCart = new Cart();
        Cart savedCart = cartRepository.save(newCart);
        assertNotNull(savedCart.getCartId(), "Cart should have been saved and given an ID");


    }
    
    @Test
    void testGetCart() {
        Cart cart = cartService.createCart();
        Cart checkCart = cartService.getCart(cart.getCartId());
        assertTrue( checkCart.getCartId() == cart.getCartId());
    }

    // add items to Cart
    @Test
    @Transactional
    void testCartAddItem() {
    // grab the real IDs
    List<Product> products = productService.getProducts();
    int idA = products.get(0).getProductid();
    int idB = products.get(1).getProductid();

    // create a fresh cart
    int cartId = cartService.createCart().getCartId();

    // --- add product A once ---
    cartService.addProduct(cartId, idA, 1);

    Cart afterA1 = cartService.getCart(cartId);
    List<CartItem> itemsA1 = new ArrayList<>(afterA1.getItems());
    assertEquals(1, itemsA1.size(), "should have one CartItem after first add");

    CartItem itemA = itemsA1.get(0);
    assertEquals("Product A", itemA.getProduct().getName());
    assertEquals("Description A", itemA.getProduct().getDescription());
    assertEquals(10.0, itemA.getProduct().getPrice());
    assertEquals(1, itemA.getQuantity());

    // --- add product A two more (total = 3) ---
    cartService.addProduct(cartId, idA, 2);

    Cart afterA3 = cartService.getCart(cartId);
    List<CartItem> itemsA3 = new ArrayList<>(afterA3.getItems());
    assertEquals(1, itemsA3.size(), "still only one item entry for A");
    assertEquals(3, itemsA3.get(0).getQuantity());

    // --- add product B twice ---
    cartService.addProduct(cartId, idB, 2);

    Cart afterB2 = cartService.getCart(cartId);
    List<CartItem> itemsB2 = new ArrayList<>(afterB2.getItems());
    assertEquals(2, itemsB2.size(), "now two different CartItem entries");

    CartItem itemB = itemsB2.get(1);
    assertEquals("Product B", itemB.getProduct().getName());
    assertEquals("Description B", itemB.getProduct().getDescription());
    assertEquals(10.0, itemB.getProduct().getPrice());
    assertEquals(2, itemB.getQuantity());
    }

    // remove item from Cart
    @Test
    @Transactional
    void testCartRemoveItem() {

        List<Product> products = productService.getProducts();
        int idA = products.get(0).getProductid();
        int idB = products.get(1).getProductid();

        // initialize cart with some products
        int cartId = cartService.createCart().getCartId();
        cartService.addProduct(cartId, idA, 3);
        cartService.addProduct(cartId, idB, 3);
        Cart afterABCart = cartService.getCart(cartId);

        // cart should contain 2 unique CartItems
        List<CartItem> itemsAB = new ArrayList<>(afterABCart.getItems());
        assertEquals(itemsAB.size(), 2, "Cart should contain 2 items");

        // subtract 2 productA from cart, 1 productA remaining
        cartService.subtractProduct(cartId, idA, 2);
        Cart afterSubA = cartService.getCart(cartId);
        assertEquals(afterSubA.getItems().get(0).getQuantity(), 1, "CartItem for product A should have quantity 1");
        
        // subtract 2 productB from cart, 1 productB remaining
        cartService.subtractProduct(cartId, idB, 2);
        Cart afterSubB = cartService.getCart(cartId);
        assertEquals(afterSubB.getItems().get(1).getQuantity(), 1, "CartItem for product B should have quantity 1");

        // subtracting all remaining products from cart, should delete relations and return an empty cart
        cartService.subtractProduct(cartId, idA, 1);
        cartService.subtractProduct(cartId, idB, 1);
        Cart afterSubALL = cartService.getCart(cartId);
        assertEquals(afterSubALL.getItems().size(), 0);

         // subtracting all remaining products from cart AGAIN, no changes should occur
         cartService.subtractProduct(cartId, idA, 1);
         cartService.subtractProduct(cartId, idB, 1);
         afterSubALL = cartService.getCart(cartId);
         assertEquals(afterSubALL.getItems().size(), 0);


    
    }
    // clear cart
    @Test
    @Transactional
    void testClearCart() {

        List<Product> products = productService.getProducts();
        int idA = products.get(0).getProductid();
        int idB = products.get(1).getProductid();

        // create a new cart, add some products
        Cart cart = cartService.createCart();
        int cartId = cart.getCartId();
        cartService.addProduct(cartId, idA, 3);
        cartService.addProduct(cartId, idB, 3);
        cart = cartService.getCart(cartId);
        // cart should have two unique items in it
        assertEquals(cart.getItems().size(), 2);
        
        // clear cart, should have 0 items in it
        cartService.clearCart(cartId);
        cart = cartService.getCart(cartId);
        assertEquals(cart.getItems().size(), 0);


    }

    // cart exists
    void testCartExists() {

        Cart cart = cartService.createCart();
        assertTrue(cartService.cartExists(cart.getCartId()));

    }

    // delete cart
    @Test
    @Transactional
    void testDeleteCart() {

        // create a new cart
        Cart cart = cartService.createCart();
        // make sure cart exists
        assertTrue(cartService.cartExists(cart.getCartId()));

        // delete the cart
        cartService.deleteCart(cart.getCartId());
        // ensure the cart does not exist
        assertFalse(cartService.cartExists(cart.getCartId()));
        
    }

    @Test
    @Transactional
    void testGetCartCost() {

        // initialize products
        List<Product> products = productService.getProducts();
        int idA = products.get(0).getProductid();
        int idB = products.get(1).getProductid();

        // create a new car
        Cart cart = cartService.createCart();
        int cartId = cart.getCartId();

        // add products to cart
        cartService.addProduct(cartId, idA, 2);
        cartService.addProduct(cartId, idB, 2);

        // calculate cart cost manually, based off of our products
        double expectedCost = (products.get(0).getPrice() * 2) + (products.get(1).getPrice() * 2);
        // calculate cart cost from our service
        double actualCost = cartService.getCartCost(cartId);
        // check if costs are indeed equal
        assertEquals(expectedCost, actualCost);


    }

   
}
