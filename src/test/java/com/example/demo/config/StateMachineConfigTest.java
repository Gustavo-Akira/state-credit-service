package com.example.demo.config;

import com.example.demo.domain.PaymentEvent;
import com.example.demo.domain.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;


@SpringBootTest
class StateMachineConfigTest {
    StateMachineFactory<PaymentState, PaymentEvent> factory;

    @Test
    void testNewStateMachine(){
        StateMachine<PaymentState,PaymentEvent> stateMachine = factory.getStateMachine(UUID.randomUUID());

        stateMachine.start();

        System.out.println(stateMachine.getState().toString());

        stateMachine.sendEvent(PaymentEvent.PRE_AUTHORIZE);

        System.out.println(stateMachine.getState().toString());
    }
}