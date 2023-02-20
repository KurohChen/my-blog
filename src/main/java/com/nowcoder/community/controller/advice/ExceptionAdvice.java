package com.nowcoder.community.controller.advice;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//这个注解只会扫描待有Controller的组件
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response){
        //记录异常
        logger.error("服务器发生异常"+e.getMessage());
        for (StackTraceElement element : e.getStackTrace()){
            logger.error(element.toString());
        }

        //判断是主动请求还是异步请求
        //因为两者需要的返回值不用，主动请求返回网页，异步请求返回json
        String xRequestedWith = request.getHeader("x-requested-with");
        try{
            if(xRequestedWith.equals("XMLHttpRequest")){
                //异步请求
                //返回一个普通的字符串
                response.setContentType("application/plain;charset=utf-8");
                PrintWriter writer = null;
                writer = response.getWriter();

                writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
            }else {
                //返回页面
                response.sendRedirect(request.getContextPath()+"/error");
            }
        }catch(IOException ex){
            logger.error("发生异常："+ ex.getMessage());
        }


    }
}
