package com.example.demo.service;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentEvent;
import com.example.demo.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    Payment newPayment(Payment payment);
    StateMachine<PaymentState, PaymentEvent> preAuth(Long id);
    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long id);
    StateMachine<PaymentState, PaymentEvent> declineAuth(Long id);
}
