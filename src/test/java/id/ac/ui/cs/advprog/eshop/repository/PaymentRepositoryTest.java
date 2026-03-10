package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment("p1", "VOUCHER", paymentData);
    }

    @Test
    void testSave() {
        Payment result = paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById(payment.getId());
        
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
    }

    @Test
    void testFindByIdIfIdFound() {
        paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById(payment.getId());
        assertNotNull(findResult);
        assertEquals(payment.getId(), findResult.getId());
    }

    @Test
    void testFindByIdIfIdNotFound() {
        paymentRepository.save(payment);
        Payment findResult = paymentRepository.findById("invalid-id");
        assertNull(findResult);
    }

    @Test
    void testFindAll() {
        paymentRepository.save(payment);
        Map<String, String> data2 = new HashMap<>();
        data2.put("address", "Depok");
        data2.put("deliveryFee", "10000");
        Payment payment2 = new Payment("p2", "COD", data2);
        paymentRepository.save(payment2);

        assertEquals(2, paymentRepository.findAll().size());
    }
}