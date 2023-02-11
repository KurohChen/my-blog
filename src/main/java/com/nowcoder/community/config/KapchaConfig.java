package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ObjectInputFilter;
import java.util.Properties;

@Configuration
public class KapchaConfig {
    // 声明一个bean 使这个bean被spring装配
    // 实例化核心接口，这个接口也有默认实现类
    @Bean
    public Producer kapchaProducer(){
        Properties properties = new Properties();
        //单位像素
        properties.setProperty("kapcha.img.width","100");
        properties.setProperty("kapcha.img.height","40");
        properties.setProperty("kapcha.textproducer.font.size","32");
        properties.setProperty("kapcha.textproducer.font.color","0,0,0");
        properties.setProperty("kapcha.textproducer.char.strin","0123456789QWERTYUIOPASDFGHJKLZXCVBNM");
        properties.setProperty("kapcha.textproducer.char.length","4");
        // 噪音
        properties.setProperty("kapcha.textproducer.noise.impl","com.google.code.kaptcha.impl.NoNoise");

        DefaultKaptcha kaptcha = new DefaultKaptcha();
        //config()类似map<key,value>，存的是参数
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
