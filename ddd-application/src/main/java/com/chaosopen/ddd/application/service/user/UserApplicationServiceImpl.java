package com.chaosopen.ddd.application.service.user;

import com.chaosopen.ddd.application.command.user.query.UserInfoQryExe;
import com.chaosopen.ddd.client.api.UserServiceI;
import com.chaosopen.ddd.client.dto.UserInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.UserInfoCO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationServiceImpl implements UserServiceI {

    @Autowired
    private UserInfoQryExe userInfoQryExe;

    @Override
    public UserInfoCO getUserInfo(UserInfoQry qry) {
        return userInfoQryExe.execute(qry);
    }
}
