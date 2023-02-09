package com.nowcoder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
// 和main里同一类（具体忘了）
@ContextConfiguration(classes = CommunityApplication.class)
public class LoggerTests {
    // 为了便于所有方法调用，设立为静态的
    // final：不可改变
    // 传入的类名就是Logger的名字,以当前类
    private static final Logger logger = LoggerFactory.getLogger(LoggerTests.class);

    @Test
    public void testLogger(){
        System.out.println(logger.getName());
        // 跟踪级
        logger.debug("debug log");
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");
        //然后配日志级别：启动什么级别的日志在application.properties
        // 将日志存储到文件
    }

}
