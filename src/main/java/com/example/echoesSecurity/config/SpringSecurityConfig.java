package com.example.echoesSecurity.config;

import  com.example.echoesSecurity.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;

/** @EnableMethodSecurity 开启方法级别的权限验证 **/
@EnableMethodSecurity
@Configuration
public class SpringSecurityConfig {
    @Autowired
    private DbUserDetailsManager dbUserDetailsManager;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /** authenticationManager 注入自定义的数据库版本的授权管理器**/
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(dbUserDetailsManager);
        authenticationProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //authorizeRequests()：开启授权保护
        //anyRequest()：对所有请求开启授权保护
        //authenticated()：已认证请求会自动被授权
        http.authorizeRequests(authorize -> {
            // 放行不需要认证的路径
            authorize.antMatchers("/webjars/**", "/v3/**", "/doc.html", "/favicon.ico", "/swagger-ui/**").permitAll();
            //其它请求需要认证
            authorize.anyRequest().authenticated();
        });

        /**配置登录的认证及认证处理**/
        //自定义登录
        http.formLogin(formLogin -> {
            formLogin.loginPage("/login").permitAll() //登录页面无需授权即可访问
                    .usernameParameter("username") //自定义表单用户名参数，默认是username
                    .passwordParameter("password");//自定义表单密码参数，默认是password
            //登录认证成功
            formLogin.successHandler(new MyAuthenticationSuccessHandler(stringRedisTemplate));
            //登录认证失败
            formLogin.failureHandler(new MyAuthenticationFailureHandler());
        });

        // 基本授权方式
        http.httpBasic(Customizer.withDefaults());

        //自定义注销
        http.logout(logout -> {
            //注销的访问路径
            logout.logoutUrl("/logout");
            //删除授权信息
            logout.clearAuthentication(true);
            //删除cookie
            logout.deleteCookies("JSESSIONID");
            //设置session失效
            logout.invalidateHttpSession(true);
            //退出的处理
            logout.addLogoutHandler(new MyLogoutHandler());
            //退出成功的响应
            logout.logoutSuccessHandler(new MyLogoutSuccessHandler());
        });

        //错误处理
        http.exceptionHandling(exception -> {
            //请求未认证的接口
            exception.authenticationEntryPoint(new MyAuthenticationEntryPoint());
            //未授权的接口异常处理：可能会不生效，被全局异常处理器拦截
            exception.accessDeniedHandler(new MyAccessDeniedHandler());
        });

        // 跨域处理
        http.cors(Customizer.withDefaults());


        //关闭csrf攻击防御
        http.csrf(csrf -> {
            csrf.disable();
        });
        return http.build();
    }

}