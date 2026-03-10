package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private List<Product> products;

    @BeforeEach
    void setUp() {
        this.products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        this.products.add(product1);

        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620121");
        product2.setProductName("Sabun Cap Pak Agus");
        product2.setProductQuantity(1);
        this.products.add(product2);
    }

    @Test
    void testCreateOrderWithDefaultStatus() {
        Order order = new Order("1365255a-1ef7-47d0-8133-14b3d9b3571d", this.products, 1708560000L, "Bambang Sugeni");
        assertEquals("1365255a-1ef7-47d0-8133-14b3d9b3571d", order.getId());
        assertEquals(this.products, order.getProducts());
        assertEquals(1708560000L, order.getOrderTime());
        assertEquals("Bambang Sugeni", order.getAuthor());
        assertEquals("WAITING_PAYMENT", order.getStatus()); // Status default
    }

    @Test
    void testCreateOrderWithSuccessStatus() {
        Order order = new Order("1365255a-1ef7-47d0-8133-14b3d9b3571d", this.products, 1708560000L, "Bambang Sugeni", OrderStatus.SUCCESS.getValue());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testCreateOrderWithInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order("1365255a-1ef7-47d0-8133-14b3d9b3571d", this.products, 1708560000L, "Bambang Sugeni", "MELEDAK");
        });
    }

    @Test
    void testSetStatusToCancelled() {
        Order order = new Order("1365255a-1ef7-47d0-8133-14b3d9b3571d", this.products, 1708560000L, "Bambang Sugeni");
        order.setStatus("CANCELLED");
        assertEquals("CANCELLED", order.getStatus());
    }

    @Test
    void testSetStatusToInvalid() {
        Order order = new Order("1365255a-1ef7-47d0-8133-14b3d9b3571d", this.products, 1708560000L, "Bambang Sugeni");
        assertThrows(IllegalArgumentException.class, () -> order.setStatus("INVALID_STATUS"));
    }
}