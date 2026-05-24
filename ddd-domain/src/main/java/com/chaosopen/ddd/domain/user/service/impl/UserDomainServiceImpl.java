package com.chaosopen.ddd.domain.user.service.impl;

import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.domain.user.gateway.UserGateway;
import com.chaosopen.ddd.domain.user.model.User;
import com.chaosopen.ddd.domain.user.service.UserDomainService;

/**
 * 用户领域服务实现。
 */
public class UserDomainServiceImpl implements UserDomainService {

    private UserGateway userGateway;

    public void setUserGateway(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User getByUserId(Long userId) {
        return userGateway.findByUserId(userId)
                .orElseThrow(() -> new BizException(ErrorCode.USER_NOT_FOUND, "用户不存在"));
    }

    @Override
    public void addPointsAndSave(User user, Integer points) {
        user.addPoints(points);
        userGateway.save(user);
    }
}
