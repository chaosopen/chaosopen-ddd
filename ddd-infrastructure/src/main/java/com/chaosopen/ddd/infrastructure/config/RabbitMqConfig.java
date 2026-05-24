package com.chaosopen.ddd.infrastructure.config;

import com.chaosopen.ddd.common.constant.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置。
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public FanoutExchange orderCreatedFanoutExchange() {
        return new FanoutExchange(MqConstants.ORDER_CREATED_FANOUT_EXCHANGE);
    }

    @Bean
    public Queue orderSmsQueue() {
        return new Queue(MqConstants.ORDER_SMS_QUEUE, true);
    }

    @Bean
    public Binding orderSmsBinding(Queue orderSmsQueue, FanoutExchange orderCreatedFanoutExchange) {
        return BindingBuilder.bind(orderSmsQueue).to(orderCreatedFanoutExchange);
    }

    @Bean
    public Queue userPointsQueue() {
        return new Queue(MqConstants.USER_POINTS_QUEUE, true);
    }

    @Bean
    public Binding userPointsBinding(Queue userPointsQueue, FanoutExchange orderCreatedFanoutExchange) {
        return BindingBuilder.bind(userPointsQueue).to(orderCreatedFanoutExchange);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new SimpleMessageConverter();
    }
}
