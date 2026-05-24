package com.chaosopen.ddd.infrastructure.user.gatewayImpl;

import com.chaosopen.ddd.domain.user.gateway.UserGateway;
import com.chaosopen.ddd.domain.user.model.User;
import com.chaosopen.ddd.infrastructure.user.persistence.mapper.UserMapper;
import com.chaosopen.ddd.infrastructure.user.persistence.dataobject.UserDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class UserGatewayImpl implements UserGateway {

    @Resource
    private UserMapper userMapper;

    @Override
    public Optional<User> findByUserId(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        if (userDO == null) {
            return Optional.empty();
        }
        User user = new User();
        user.setUserId(userDO.getUserId());
        user.setUserName(userDO.getUserName());
        user.setMobile(userDO.getMobile());
        user.setStatus(userDO.getStatus());
        user.setPoints(userDO.getPoints());
        return Optional.of(user);
    }

    @Override
    public void save(User user) {
        UserDO userDO = new UserDO();
        userDO.setUserId(user.getUserId());
        userDO.setUserName(user.getUserName());
        userDO.setMobile(user.getMobile());
        userDO.setStatus(user.getStatus());
        userDO.setPoints(user.getPoints());

        if (userDO.getUserId() == null) {
            userMapper.insert(userDO);
            user.setUserId(userDO.getUserId());
        } else {
            userMapper.updateById(userDO);
        }
    }
}
