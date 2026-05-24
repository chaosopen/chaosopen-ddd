package com.chaosopen.ddd.application.command.user.query;

import com.chaosopen.ddd.application.converter.user.UserApplicationConverter;
import com.chaosopen.ddd.client.dto.UserInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.UserInfoCO;
import com.chaosopen.ddd.common.enums.ErrorCode;
import com.chaosopen.ddd.common.exception.BizException;
import com.chaosopen.ddd.infrastructure.user.persistence.dataobject.UserDO;
import com.chaosopen.ddd.infrastructure.user.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 读模型、简单查询、无业务规则，应用层直查 mapper
 */
@Component
public class UserInfoQryExe {

    @Resource
    private UserMapper userMapper;

    public UserInfoCO execute(UserInfoQry qry) {
        UserDO user = userMapper.selectById(qry.getUserId());
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND, "用户不存在");
        }
        return UserApplicationConverter.toUserInfoCO(user);
    }
}
