package com.chaosopen.ddd.infrastructure.sms.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 示例短信远程客户端实现。
 * 这里只做日志输出，后续可替换为真实HTTP/RPC调用。
 */
@Component
public class MockSmsRemoteClient implements SmsRemoteClient {

    private static final Logger log = LoggerFactory.getLogger(MockSmsRemoteClient.class);

    @Override
    public void sendByTemplate(String mobile, String templateCode, Map<String, String> templateParams) {
        log.info("[SMS-REMOTE] send to={}, templateCode={}, templateParams={}", mobile, templateCode, templateParams);
    }
}
