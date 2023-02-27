package com.nowcoder.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
//加@EnableScheduling启动定时任务
//加@EnableAsync 让@Async生效
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
