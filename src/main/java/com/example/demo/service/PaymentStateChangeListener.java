package com.example.demo.service;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentEvent;
import com.example.demo.domain.PaymentState;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;


@RequiredArgsConstructor
@Component
public class PaymentStateChangeListener extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {
        private final PaymentRepository repository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message, Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine) {
        Optional.ofNullable(message).ifPresent(msg->{
            Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_TO_HEADER, -1L)))
            .ifPresent(paymentId->{
                Payment payment = repository.getOne(paymentId);
                payment.setState(state.getId());
                repository.save(payment);
            });
        });
    }
}
