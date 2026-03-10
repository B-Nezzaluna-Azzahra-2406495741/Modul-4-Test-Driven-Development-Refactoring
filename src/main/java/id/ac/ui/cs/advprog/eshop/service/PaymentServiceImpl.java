package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String status = validatePayment(method, paymentData);
        Payment payment = new Payment(UUID.randomUUID().toString(), method, paymentData);
        return setStatus(payment, status, order);
    }

    private String validatePayment(String method, Map<String, String> paymentData) {
        if ("VOUCHER".equals(method)) {
            return isValidVoucher(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
        } 
        
        if ("COD".equals(method)) {
            return isValidCod(paymentData) ? "SUCCESS" : "REJECTED";
        }

        return "REJECTED";
    }

    private boolean isValidVoucher(String code) {
        if (code == null || code.length() != 16 || !code.startsWith("ESHOP")) {
            return false;
        }
        long digits = code.chars().filter(Character::isDigit).count();
        return digits == 8;
    }

    private boolean isValidCod(Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        return address != null && !address.isBlank() && 
               deliveryFee != null && !deliveryFee.isBlank();
    }

    @Override
    public Payment setStatus(Payment payment, String status, Order order) {
        payment.setStatus(status);
        if ("SUCCESS".equals(status)) {
            order.setStatus("SUCCESS");
        } else if ("REJECTED".equals(status)) {
            order.setStatus("FAILED");
        }
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) { 
        return paymentRepository.findById(paymentId); 
    }

    @Override
    public List<Payment> getAllPayments() { 
        return paymentRepository.findAll(); 
    }
}