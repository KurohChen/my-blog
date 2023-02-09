package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@RunWith(SpringRunner.class)
@SpringBootTest
// 和main里同一类（具体忘了）
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;

    // 模板引擎
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("465344912@qq.com", "TEST", "HelloWord.");

    }

    @Test
    public void testHtmlMail(){
        // templeaf的
        Context context = new Context();
        // 传参
        context.setVariable("username","Gokotta");

        String content = templateEngine.process("/mail/demo",context);
        mailClient.sendMail("465344912@qq.com", "Html", content);


    }
}
