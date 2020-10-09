package com.example.demo.service;

import com.example.demo.domain.Payment;
import com.example.demo.domain.PaymentEvent;
import com.example.demo.domain.PaymentState;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;

    @Override
    public Payment newPayment(Payment payment) {
        payment.setState(PaymentState.NEW);
        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> preAuth(Long id) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(id);
        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long id) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(id);
        return null;
    }

    @Override
    public StateMachine<PaymentState, PaymentEvent> declineAuth(Long id) {
        StateMachine<PaymentState, PaymentEvent> stateMachine = build(id);
        return null;
    }

    private StateMachine<PaymentState, PaymentEvent> build(Long paymentId){
        Payment payment = paymentRepository.getOne(paymentId);
        StateMachine<PaymentState, PaymentEvent> stateMachine = stateMachineFactory.getStateMachine(Long.toString(payment.getId()));
        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(stateMachineA->{
                    stateMachineA.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(),null,null,null));
                });

        stateMachine.start();

        return stateMachine;
    }

    private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> stateMachine, PaymentEvent event){
        Message message = 
    }
}
