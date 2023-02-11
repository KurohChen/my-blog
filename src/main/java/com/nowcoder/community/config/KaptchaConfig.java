package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    // 声明一个bean 使这个bean被spring装配
    // 实例化核心接口，这个接口也有默认实现类
    @Bean
    public Producer kaptchaProducer() {
        Properties properties = new Properties();
        //单位像素
        properties.setProperty("kaptcha.img.width", "100");
        properties.setProperty("kaptcha.img.height", "40");
        properties.setProperty("kaptcha.textproducer.font.size", "32");
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYAZ");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        // 噪音
        properties.setProperty("kaptcha.textproducer.noise.impl", "com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        //config()类似map<key,value>，存的是参数
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }
}
