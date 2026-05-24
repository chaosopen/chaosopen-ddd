package com.chaosopen.ddd.client.api;

import com.chaosopen.ddd.client.dto.UserInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.UserInfoCO;

public interface UserServiceI {

    UserInfoCO getUserInfo(UserInfoQry qry);
}
