package com.chaosopen.ddd.domain.user.service;

import com.chaosopen.ddd.domain.user.model.User;

/**
 * 用户领域服务：负责用户领域规则和状态变更。
 */
public interface UserDomainService {

    /**
     * 查询用户。
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    User getByUserId(Long userId);

    /**
     * 增加用户积分并持久化。
     *
     * @param user 用户实体
     * @param points 增加积分
     */
    void addPointsAndSave(User user, Integer points);
}
