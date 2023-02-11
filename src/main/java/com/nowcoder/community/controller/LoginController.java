package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    // 注入业务组件
    @Autowired
    private UserService userService;

    @Autowired
    private Producer kapchaProducer;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    // 返回注册网页
    // 在html中修改了相对路径的资源，让thymeleaf管理它们
    @RequestMapping(path="/register", method = RequestMethod.GET)
    public String getRegisterPage(){

        return "/site/register";
    }

    // 返回登录网页
    // 在html中修改了相对路径的资源，让thymeleaf管理它们
    @RequestMapping(path="/login", method = RequestMethod.GET)
    public String getLoginPage(){

        return "/site/login";
    }



    // 处理客户端post过来的用户数据
    @RequestMapping(path="/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String,Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            // 模板参数
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }
    // http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        //从路径中取变量
        int result = userService.activation(userId,code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号已经能够正常使用了");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已激活");
            model.addAttribute("target","/index");
        }else {
            model.addAttribute("msg","激活失败，您提供的激活码不正确");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    // 生成验证码返回路径
    @RequestMapping(path = "/kapcha", method = RequestMethod.GET)
    public void getKapcha(HttpServletResponse response, HttpSession session){
        // 生成验证码
        String text = kapchaProducer.createText();
        BufferedImage image = kapchaProducer.createImage(text);

        //将验证码存入session
        session.setAttribute("kapcha",text);

        //将图片输出给浏览器
        //声明数据所用的格式
        response.setContentType("image/png");
        // 获取输出流:字节流
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }

}
