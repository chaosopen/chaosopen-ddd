package com.chaosopen.ddd.application.command.user;

import com.chaosopen.ddd.domain.user.model.User;
import com.chaosopen.ddd.domain.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户积分增加执行器（MQ 消费链路）。
 */
@Component
public class IncreaseUserPointsCmdExe {

    @Autowired
    private UserDomainService userDomainService;

    /**
     * 根据消息增加用户积分。
     *
     * @param userId 用户ID
     * @param points 增加积分
     * @param orderNo 订单号
     */
    @Transactional(rollbackFor = Exception.class)
    public void execute(Long userId, Integer points, String orderNo) {
        if (userId == null || points == null || points <= 0) {
            return;
        }
        User user = userDomainService.getByUserId(userId);
        userDomainService.addPointsAndSave(user, points);
    }
}
