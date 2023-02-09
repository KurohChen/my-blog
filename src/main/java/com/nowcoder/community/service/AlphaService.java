package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;

// 表示是一个业务组件
@Service
// 每次生成不同的bean
//@Scope("prototype")
public class AlphaService {
    // controller调用service，service调用Dao，所以在这里运用依赖注入
    @Autowired
    private AlphaDao alphaDao;

    // 对比
    public AlphaService(){
        // 在初始化前
        System.out.println("实例化AlphaService");
    }

    // 在构造之后用
    @PostConstruct
    public void init(){
        System.out.println("初始化AlphaService");
    }
    // 在销毁对象之前调用它
    @PreDestroy
    public  void destroy(){
        System.out.println("销毁AlphaService");
    }

    public String find(){
        return alphaDao.select();
    }


}
