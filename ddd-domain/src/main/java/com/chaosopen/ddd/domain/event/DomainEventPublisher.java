package com.chaosopen.ddd.domain.event;

import java.util.List;

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

    /**
     * 批量发布领域事件。
     *
     * @param events 事件列表
     */
    void publish(List<Object> events);
}
