package com.chaosopen.ddd.adapter.mq.consumer;

import com.chaosopen.ddd.application.command.user.IncreaseUserPointsCmdExe;
import com.chaosopen.ddd.client.dto.mq.OrderCreatedMessage;
import com.chaosopen.ddd.common.constant.MqConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户积分增加消息消费者（Adapter 入站监听）。
 */
@Component
public class UserPointsIncreaseConsumer {

    @Autowired
    private IncreaseUserPointsCmdExe increaseUserPointsCmdExe;

    @RabbitListener(queues = MqConstants.USER_POINTS_QUEUE)
    public void onMessage(OrderCreatedMessage message) {
        if (message == null) {
            return;
        }
        increaseUserPointsCmdExe.execute(message.getUserId(), message.getTotalQuantity(), message.getOrderNo());
    }
}
