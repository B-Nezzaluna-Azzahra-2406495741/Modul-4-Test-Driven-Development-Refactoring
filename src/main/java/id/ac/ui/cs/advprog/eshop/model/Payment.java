package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter; 
import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

@Getter
public class Payment {
    private final String id;
    private final String method;
    private String status;
    private final Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        if (id == null || id.isBlank() || method == null || paymentData == null || paymentData.isEmpty()) {
            throw new IllegalArgumentException("Invalid payment arguments");
        }
        
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
        this.status = "PENDING"; 
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
}