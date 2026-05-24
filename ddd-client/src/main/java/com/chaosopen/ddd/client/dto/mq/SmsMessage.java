package com.chaosopen.ddd.client.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 短信消息体（MQ 传输契约）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

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
