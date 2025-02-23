package com.example.config;



import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthenticationFilter;
import com.example.service.AccountService;
import com.example.util.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {
    @Resource
    private JwtUtils utils;

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    private AccountService accountService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> {
                    conf.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    //一般分离之后，为了统一规范接口，使用 /api/模块/功能 的形式命名接口
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler(this::handleProcess);
                    conf.failureHandler(this::handleProcess);
                    conf.permitAll();
                })
                .cors(conf -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    //添加前端站点地址，这样就可以告诉浏览器信任了
                    cors.addAllowedOrigin("http://localhost:8080");
                    //虽然也可以像这样允许所有 cors.addAllowedOriginPattern("*");
                    //但是这样并不安全，我们应该只许可给我们信任的站点
                    cors.setAllowCredentials(true);  //允许跨域请求中携带Cookie
                    cors.addAllowedHeader("*");   //其他的也可以配置，为了方便这里就 * 了
                    cors.addAllowedMethod("*");
                    cors.addExposedHeader("*");
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);  //直接针对于所有地址生效
                    conf.configurationSource(source);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(conf -> {
                    //配置授权相关异常处理器
                    conf.accessDeniedHandler(this::handleProcess);
                    //配置验证相关异常的处理器
                    conf.authenticationEntryPoint(this::handleProcess);
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/auth/logout");
                    conf.logoutSuccessHandler(this::onLogoutSuccess);
                })
                .sessionManagement(conf -> {
                    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                //添加我们用于处理JWT的过滤器到Security过滤器链中，注意要放在UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用 BCrypt 作为默认编码器
        return new BCryptPasswordEncoder();
    }

    public void handleProcess(HttpServletRequest request,
                               HttpServletResponse response,
                               Object exceptionOrAuthentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if(exceptionOrAuthentication instanceof AccessDeniedException exception) {
            writer.write(RestBean.failure(403, exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Exception exception) {
            writer.write(RestBean.failure(401, exception.getMessage()).asJsonString());
        } else if(exceptionOrAuthentication instanceof Authentication authentication){
            User user = (User) authentication.getPrincipal();
            String token = utils.createJwt(user);
            Account account = accountService.findAccountByNameOrEmail(user.getUsername());

            AuthorizeVO vo = account.asViewObject(AuthorizeVO.class, v -> {
                v.setToken(token);
                v.setExpire(utils.expireTime());
            });
//            BeanUt ils.copyProperties(account, vo);

            vo.setExpire(utils.expireTime());
            vo.setToken(token);
            writer.write(RestBean.success(vo).asJsonString());
//                        writer.write(RestBean.success(authentication.getName()).asJsonString());
        }
    }

    public void onLogoutSuccess(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
       if(utils.invalidate(authorization)) {
           writer.write(RestBean.success("退出登录成功").asJsonString());
       } else {
           writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
       }
    }

    public void onAccessDeny(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden(exception.getMessage()).asJsonString());
    }

//    void onAuthenticationFailure(HttpServletRequest request,
//                                 HttpServletResponse response,
//                                 AuthenticationException exception) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        writer.write(RestBean.failure(401, exception.getMessage()).asJsonString());
//    }
//
//    void onAuthenticationSuccess(HttpServletRequest request,
//                                 HttpServletResponse response,
//                                 Authentication authentication) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        writer.write(RestBean.success(authentication.getName()).asJsonString());
//    }
}