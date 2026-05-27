package com.chaosopen.ddd.infrastructure.common.event;

import com.chaosopen.ddd.domain.event.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;


@Component
public class DomainEventPublisherImpl implements DomainEventPublisher {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publish(Object domainEvent) {
        if (domainEvent == null) {
            return;
        }
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            applicationEventPublisher.publishEvent(domainEvent);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                applicationEventPublisher.publishEvent(domainEvent);
            }
        });
    }

    @Override
    public void publish(List<Object> domainEvents) {
        if (domainEvents == null || domainEvents.isEmpty()) {
            return;
        }
        for (Object domainEvent : domainEvents) {
            publish(domainEvent);
        }
    }
}
