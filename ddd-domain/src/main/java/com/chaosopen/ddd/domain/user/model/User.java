package com.chaosopen.ddd.domain.user.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体。
 */
@Data
public class User {

    /**
     * 用户ID。
     */
    private Long userId;
    /**
     * 用户名。
     */
    private String userName;
    /**
     * 手机号。
     */
    private String mobile;
    /**
     * 用户状态。
     */
    private Integer status;
    /**
     * 用户积分。
     */
    private Integer points;
    /**
     * 创建时间。
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间。
     */
    private LocalDateTime updatedAt;

    /**
     * 增加积分。
     *
     * @param delta 增加数量
     */
    public void addPoints(Integer delta) {
        if (delta == null || delta <= 0) {
            return;
        }
        int current = points == null ? 0 : points;
        points = current + delta;
    }

    /**
     * 扣减积分。
     *
     * @param delta 扣减数量
     * @return 是否扣减成功
     */
    public boolean deductPoints(Integer delta) {
        if (delta == null || delta <= 0) {
            return true;
        }
        int current = points == null ? 0 : points;
        if (current < delta) {
            return false;
        }
        points = current - delta;
        return true;
    }
}
