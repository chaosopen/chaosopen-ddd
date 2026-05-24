package com.chaosopen.ddd.client.dto.clientobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoCO {

    private Long userId;
    private String userName;
    private String mobile;
    private Integer status;
    private Integer points;
}
