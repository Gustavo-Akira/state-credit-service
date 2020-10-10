package com.example.demo.service;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentEvent;
import com.example.demo.domain.PaymentState;
import com.example.demo.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    Payment payment;

    @BeforeEach
    void setUp(){
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void preAuth(){
        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        Payment preAuthPayment = paymentRepository.getOne(payment.getId());
        System.out.println(sm.getState().getId());
        System.out.println(preAuthPayment);
    }

    @RepeatedTest(10)
    @Transactional
    void auth(){
        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());
        if(sm.getState().getId() == PaymentState.PRE_AUTH){
            StateMachine<PaymentState, PaymentEvent> stateMachine = paymentService.authorizePayment(savedPayment.getId());
            System.out.println(stateMachine.getState().getId());
        }

    }
}