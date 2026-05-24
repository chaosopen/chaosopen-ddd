package com.chaosopen.ddd.adapter.controller.user;

import com.chaosopen.ddd.client.api.UserServiceI;
import com.chaosopen.ddd.client.dto.UserInfoQry;
import com.chaosopen.ddd.client.dto.clientobject.UserInfoCO;
import com.chaosopen.ddd.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceI userService;

    @GetMapping("/{userId}")
    public Result<UserInfoCO> getUserInfo(@PathVariable("userId") Long userId) {
        UserInfoQry qry = new UserInfoQry();
        qry.setUserId(userId);
        return Result.success(userService.getUserInfo(qry));
    }
}
