package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePaymentSuccessful() {
        Payment payment = new Payment("p1", "VOUCHER", paymentData);
        assertEquals("p1", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
    }


    @Test
    void testCreatePaymentWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(null, "VOUCHER", paymentData);
        });
    }

    @Test
    void testCreatePaymentWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("", "VOUCHER", paymentData);
        });
    }

    @Test
    void testCreatePaymentWithNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("p1", null, paymentData);
        });
    }

    @Test
    void testCreatePaymentWithNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("p1", "VOUCHER", null);
        });
    }

    @Test
    void testCreatePaymentWithEmptyPaymentData() {
        Map<String, String> emptyData = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("p1", "VOUCHER", emptyData);
        });
    }


    @Test
    void testSetStatusValidSuccess() {
        Payment payment = new Payment("p1", "VOUCHER", paymentData);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetStatusValidRejected() {
        Payment payment = new Payment("p1", "VOUCHER", paymentData);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment("p1", "VOUCHER", paymentData);
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("MEOW");
        });
    }

    @Test
    void testPaymentStatusEnumValues() {
        assertEquals("PENDING", PaymentStatus.PENDING.getValue());
        assertEquals("SUCCESS", PaymentStatus.SUCCESS.getValue());
        assertEquals("REJECTED", PaymentStatus.REJECTED.getValue());
    }

    @Test
    void testPaymentStatusContainsTrue() {
        assertTrue(PaymentStatus.contains("PENDING"));
        assertTrue(PaymentStatus.contains("SUCCESS"));
        assertTrue(PaymentStatus.contains("REJECTED"));
    }

    @Test
    void testPaymentStatusContainsFalse() {
        assertFalse(PaymentStatus.contains("MEOW"));
        assertFalse(PaymentStatus.contains(""));
    }
}