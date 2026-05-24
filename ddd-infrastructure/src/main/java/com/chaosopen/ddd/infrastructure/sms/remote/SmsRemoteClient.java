package com.chaosopen.ddd.infrastructure.sms.remote;

import java.util.Map;

/**
 * 短信远程客户端。
 */
public interface SmsRemoteClient {

    /**
     * 调用外部短信服务发送消息。
     */
    void sendByTemplate(String mobile, String templateCode, Map<String, String> templateParams);
}
