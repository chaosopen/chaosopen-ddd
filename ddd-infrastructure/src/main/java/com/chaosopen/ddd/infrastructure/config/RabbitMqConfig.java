package com.chaosopen.ddd.infrastructure.config;

import com.chaosopen.ddd.common.constant.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单短信RabbitMQ配置。
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange orderSmsExchange() {
        return new DirectExchange(MqConstants.ORDER_SMS_EXCHANGE);
    }

    @Bean
    public Queue orderSmsQueue() {
        return new Queue(MqConstants.ORDER_SMS_QUEUE, true);
    }

    @Bean
    public Binding orderSmsBinding(Queue orderSmsQueue, DirectExchange orderSmsExchange) {
        return BindingBuilder.bind(orderSmsQueue).to(orderSmsExchange).with(MqConstants.ORDER_SMS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new SimpleMessageConverter();
    }
}
