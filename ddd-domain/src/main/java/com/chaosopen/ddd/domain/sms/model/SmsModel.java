package com.chaosopen.ddd.domain.sms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 短信领域模型。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsModel {

    /**
     * 接收手机号。
     */
    private String mobile;

    /**
     * 模板编码。
     */
    private String templateCode;

    /**
     * 模板参数。
     */
    private Map<String, String> templateParams;
}
