package com.chaosopen.ddd.infrastructure.user.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class UserDO {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    private String userName;
    private String mobile;
    private Integer status;
    private Integer points;
}
