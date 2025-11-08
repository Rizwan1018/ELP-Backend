package com.elearning.backend.controller;

import com.elearning.backend.model.Payment;
import com.elearning.backend.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Payment APIs", description = "Simulated Payment Processing and History")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // 🎯 Process Payment
    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        return ResponseEntity.ok(paymentService.processPayment(studentId, courseId));
    }

    // 📜 View Payment History
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Payment>> getPayments(@PathVariable Long studentId) {
        return ResponseEntity.ok(paymentService.getPaymentsByStudent(studentId));
    }
}
