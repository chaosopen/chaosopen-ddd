package com.chaosopen.ddd.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.chaosopen.ddd.infrastructure")
public class InfrastructureConfig {
}
