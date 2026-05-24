package com.chaosopen.ddd.domain.event;

/**
 * 领域事件发布器（领域层定义接口）。
 */
public interface DomainEventPublisher {

    /**
     * 发布领域事件。
     *
     * @param event 事件对象
     */
    void publish(Object event);
}
