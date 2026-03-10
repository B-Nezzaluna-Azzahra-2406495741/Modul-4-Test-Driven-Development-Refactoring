package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @InjectMocks
    PaymentServiceImpl paymentService; 
    
    @Mock
    PaymentRepository paymentRepository;

    Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Sample Product");
        product.setProductQuantity(1);
        products.add(product);

        order = new Order("o1", products, 123L, "Nezza");
    }


    @Test
    void testAddPaymentVoucherSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP12345678ABC"); 
        
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Payment payment = paymentService.addPayment(order, "VOUCHER", data);
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testAddPaymentVoucherRejectedInvalidCode() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "INVALID"); 
        
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Payment payment = paymentService.addPayment(order, "VOUCHER", data);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testAddPaymentVoucherRejectedNoDigits() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOPAAAAAAAAAAAA"); 
        
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Payment payment = paymentService.addPayment(order, "VOUCHER", data);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testAddPaymentVoucherRejectedNullCode() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", null);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = paymentService.addPayment(order, "VOUCHER", data);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testAddPaymentVoucherRejectedDigitCountNotEight() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234567ABCD");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = paymentService.addPayment(order, "VOUCHER", data);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testAddPaymentVoucherRejectedWrongPrefix() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ABCDE12345678XYZ");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = paymentService.addPayment(order, "VOUCHER", data);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }


    @Test
    void testAddPaymentCODSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Depok");
        data.put("deliveryFee", "15000");
        
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Payment payment = paymentService.addPayment(order, "COD", data);
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testAddPaymentCODRejectedEmptyAddress() {
        Map<String, String> data = new HashMap<>();
        data.put("address", ""); 
        data.put("deliveryFee", "15000");
        
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Payment payment = paymentService.addPayment(order, "COD", data);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testAddPaymentCODRejectedNullFee() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Depok");
        
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Payment payment = paymentService.addPayment(order, "COD", data);
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testAddPaymentCODRejectedNullAddress() {
        Map<String, String> data = new HashMap<>();
        data.put("deliveryFee", "15000");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = paymentService.addPayment(order, "COD", data);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testAddPaymentCODRejectedBlankFee() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Depok");
        data.put("deliveryFee", "   ");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = paymentService.addPayment(order, "COD", data);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testAddPaymentUnknownMethodRejected() {
        Map<String, String> data = new HashMap<>();
        data.put("note", "test");

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment payment = paymentService.addPayment(order, "BANK_TRANSFER", data);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }


    @Test
    void testSetStatusToRejectedUpdatesOrderToFailed() {
        Payment payment = new Payment("p1", "VOUCHER", new HashMap<String, String>() {{ put("k", "v"); }});
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.setStatus(payment, "REJECTED", order);
        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testSetStatusPendingDoesNotChangeOrderStatus() {
        Payment payment = new Payment("p2", "COD", new HashMap<String, String>() {{ put("k", "v"); }});
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        paymentService.setStatus(payment, "PENDING", order);
        assertEquals("PENDING", payment.getStatus());
        assertEquals("WAITING_PAYMENT", order.getStatus());
    }


    @Test
    void testGetPaymentById() {
        Payment payment = new Payment("p1", "VOUCHER", new HashMap<String, String>() {{ put("k", "v"); }});
        when(paymentRepository.findById("p1")).thenReturn(payment);

        Payment result = paymentService.getPayment("p1");
        assertNotNull(result);
        verify(paymentRepository, times(1)).findById("p1");
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment("p1", "VOUCHER", new HashMap<String, String>() {{ put("k", "v"); }}));
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();
        assertEquals(1, result.size());
        verify(paymentRepository, times(1)).findAll();
    }
}
