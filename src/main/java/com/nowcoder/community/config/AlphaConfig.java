package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

// 表示这个类是一个配置类
@Configuration
public class AlphaConfig {
    // 配置一个第三方的bean要加个bean注解 方法名就是bean的名字
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        // 该方法返回的对象将被装到容器里
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
