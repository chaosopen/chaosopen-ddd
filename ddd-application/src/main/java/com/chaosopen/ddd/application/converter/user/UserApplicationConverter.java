package com.chaosopen.ddd.application.converter.user;

import com.chaosopen.ddd.client.dto.clientobject.UserInfoCO;
import com.chaosopen.ddd.domain.user.model.User;
import com.chaosopen.ddd.infrastructure.user.persistence.dataobject.UserDO;
import org.springframework.beans.BeanUtils;

public final class UserApplicationConverter {

    private UserApplicationConverter() {
    }

    public static UserInfoCO toUserInfoCO(User user) {
        return new UserInfoCO(
                user.getUserId(),
                user.getUserName(),
                user.getMobile(),
                user.getStatus(),
                user.getPoints()
        );
    }

    public static UserInfoCO toUserInfoCO(UserDO userDO) {
        UserInfoCO userInfoCO = new UserInfoCO();
        BeanUtils.copyProperties(userDO, userInfoCO);
        return userInfoCO;
    }
}
