package com.ohgiraffers.semiproject.config;

import com.ohgiraffers.semiproject.common.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 정적 리소스에 대한 요청은 시큐리티 설정이 돌지 못하게 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/home/css/**", "/img/**"); // CSS 파일 및 사진 접근 허용
    }


    /* comment. 여기가 설정의 핵심 */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            // 요청 권한 설정
            auth.requestMatchers("/home", "/home/no-search", "/home/no-check", "/home/pass-search", "/", "/user/signup").permitAll();
            auth.requestMatchers("/sidemenu/manager", "/sidemenu/employeeRegister", "/sidemenu/employeeManagement", "/sidemenu/approvalBox").hasAnyAuthority(UserRole.ADMIN.getRole());
            auth.requestMatchers("/main", "/sidemenu/schedule", "/sidemenu/messenger", "/sidemenu/mail", "/sidemenu/adoption", "/sidemenu/animals", "/sidemenu/adoptionComplete", "/sidemenu/stock", "/sidemenu/facilities", "/sidemenu/board", "/sidemenu/mypage").hasAnyAuthority(UserRole.USER.getRole(), UserRole.ADMIN.getRole());
            auth.requestMatchers("/user/info").authenticated();
            auth.requestMatchers("/schedule/checkin").authenticated();
            auth.requestMatchers("/schedule/checkout").authenticated();
            auth.requestMatchers("/api/board").authenticated();
            auth.anyRequest().authenticated();
        }).formLogin(login -> {
            login.loginPage("/home");
            login.usernameParameter("code");
            login.passwordParameter("pass");
            login.defaultSuccessUrl("/main", true);
            login.failureUrl("/home?error=true");
        }).logout(logout -> {
            logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
            logout.deleteCookies("JSESSIONID");
            logout.invalidateHttpSession(true);
            logout.logoutSuccessUrl("/home");
        }).sessionManagement(session -> {
            session.maximumSessions(1);
            session.invalidSessionUrl("/home");
        }).csrf(csrf -> csrf.disable());

        return http.build();
    }
}