package com.nowcoder.community.config;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 忽略静态资源的访问
        web.ignoring().antMatchers("/resources/**");
    }
    //处理认证
    //AuthenticationManager：认证的核心接口
    //AuthenticationManagerBuilder：用于构建AuthenticationManager对象的工具.
    //ProviderManager：AuthenticationManager接口的默认实现类
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //内置的认证规则
////        auth.userDetailsService(userService).passwordEncoder(new Pbkdf2PasswordEncoder("12345"));
//        //自定义认证规则
//        //AuthenticationProvider:ProviderManager持有一组AuthenticationProvider，每个AuthenticationProvider负责一种认证
//        //委托模式：ProviderManager将认证委托给AuthenticationProvider
//        auth.authenticationProvider(new AuthenticationProvider() {
//            //Authentication：用于封装认证信息的接口，不同的实现类代表不同类型的认证信息
//            @Override
//            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                String username = authentication.getName();
//                String password = (String) authentication.getCredentials();
//                User user = userService.findUserByName(username);
//                if(user == null){
//                    throw new UsernameNotFoundException("账号不存在");
//                }
//                password = CommunityUtil.md5(password+user.getSalt());
//                if(!user.getPassword().equals(password)){
//                    throw new BadCredentialsException("密码不正确");
//                }
//                //principal:主要信息
//                //credentials：证书（密码）
//                //authorities：权限
//                return new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
//            }
//            //supports：AuthenticationProvide支持的是哪种认证类型
//            @Override
//            public boolean supports(Class<?> aClass) {
//                //UsernamePasswordAuthenticationToken:Authentication常用的实现类，密码认证
//                return UsernamePasswordAuthenticationToken.class.equals(aClass);
//            }
//        });
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //会默认所有请求都被处理
        //super.configure(http);
        //登录相关配置
//        http.formLogin()
//                .loginPage("/loginpage")
//                .loginProcessingUrl("/login")
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        //重定向
//                        response.sendRedirect(request.getContextPath()+"/index");
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
//                        //转发
//                        request.setAttribute("error",e.getMessage());
//                        request.getRequestDispatcher("/loginpage").forward(request,response);
//                    }
//                });

        //授权配置
        http.authorizeRequests()
                .antMatchers("/user/setting",
                        "/user/upload",
                        "discuss/add",
                        "/comment/add/**",
                        "/letter/**",
                        "/notice/**",
                        "/like",
                        "/follow",
                        "/unfollow"
                ).hasAnyAuthority(AUTHORITY_USER,AUTHORITY_ADMIN,AUTHORITY_MODERATOR)
                .antMatchers("/discuss/top",
                        "/discuss/wonderful")
                .hasAnyAuthority(AUTHORITY_MODERATOR)
                .antMatchers("/discuss/delete")
                .hasAnyAuthority(AUTHORITY_ADMIN)
                .anyRequest().permitAll()
                .and().csrf().disable();
        //权限不够时的处理
        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    //没有登陆
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        String xRequestWith = request.getHeader("x-requested-with");
                        if("XMLRequest".equals(xRequestWith)){
                            //异步请求
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"您还没有登录"));
                        }else {
                            //同步请求
                            response.sendRedirect(request.getContextPath()+"/login");
                        }
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    //权限不足
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        String xRequestWith = request.getHeader("x-requested-with");
                        if("XMLHttpRequest".equals(xRequestWith)){
                            //异步请求
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(CommunityUtil.getJSONString(403,"您没有访问此功能的权限"));
                        }else {
                            //同步请求
                            response.sendRedirect(request.getContextPath()+"/denied");
                        }
                    }
                });
        //Security底层会拦截/logout请求进行处理，故覆盖默认逻辑，执行自己的退出代码
        http.logout().logoutUrl("/securitylogout");

        // 增加Filter,处理验证码
//        http.addFilterBefore(new Filter() {
//            @Override
//            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//                HttpServletRequest request = (HttpServletRequest) servletRequest;
//                HttpServletResponse response = (HttpServletResponse) servletResponse;
//                if (request.getServletPath().equals("/login")) {
//                    String verifyCode = request.getParameter("verifyCode");
//                    if (verifyCode == null || !verifyCode.equalsIgnoreCase("1234")) {
//                        request.setAttribute("error", "验证码错误!");
//                        request.getRequestDispatcher("/loginpage").forward(request, response);
//                        return;
//                    }
//                }
//                // 让请求继续向下执行.
//                filterChain.doFilter(request, response);
//            }
//        }, UsernamePasswordAuthenticationFilter.class);
//
//        // 记住我
//        http.rememberMe()
//                .tokenRepository(new InMemoryTokenRepositoryImpl())
//                .tokenValiditySeconds(3600 * 24)
//                .userDetailsService(userService);
    }
}
