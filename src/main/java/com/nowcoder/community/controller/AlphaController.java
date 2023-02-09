package com.nowcoder.community.controller;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    // controller调用service,所以在这里运用依赖注入
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){
        // request 请求 response 响应
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        // 得到所有请求行的key，是一个迭代器
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        // 获取传参
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write("<h1>博客</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Get请求:传参
    // /student?current=1&limit=20
    // 指定路径，指定只能用get请求
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name="current",required = false,defaultValue = "1") int current,
            @RequestParam(name="limit",required = false,defaultValue = "10") int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some student";
    }

    // Get请求：把参数拼到路径当中
    // /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    // Post请求：
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,String age){
        // @RequestParam需要加，也可以不加
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应HTML数据
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView mav = new ModelAndView();
        // 需要什么参数返回啥参数
        mav.addObject("name","gokotta");
        mav.addObject("age","18");
        // 放在templates/demo/view.html
        mav.setViewName("/demo/view");
        return mav;
    }

    // 响应HTML数据
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model){
        // model不是自己创建的，创建方式时自动实例化这个对象传给你
        // 把model和view分开，把model装到model参数里，把view返回
        // 结果和getTeacher()一样
        // 这个更好一点
        model.addAttribute("name","bupt");
        model.addAttribute("age","70");
        return "/demo/view";
    }

    // 响应json数据（异步请求）
    // Java对象-> JSON字符串 ->JS对象
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","Gokotta");
        emp.put("age","18");
        emp.put("salary","25k");
        return emp;
    }

    // 响应json数据（异步请求）
    // Java对象-> JSON字符串 ->JS对象
    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        // 返回多组数据
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> emp = new HashMap<>();

        emp.put("name","Gokotta");
        emp.put("age","18");
        emp.put("salary","25k");
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","Kuroh");
        emp.put("age","18");
        emp.put("salary","32k");
        list.add(emp);

        return list;
    }


}
