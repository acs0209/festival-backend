package com.mysite.sbb.config;

import com.mysite.sbb.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*
* 스프링 시큐리티는 스프링 기반 애플리케이션의 인증과 권한을 담당하는 스프링의 하위 프레임워크이다.
  인증(Authenticate)은 로그인을 의미한다.
  권한(Authorize)은 인증된 사용자가 어떤 것을 할 수 있는지를 의미한다.

* 스프링 시큐리티는 기본적으로 인증되지 않은 사용자는 서비스를 사용할 수 없게끔 되어 있다.
* 따라서 인증을 위한 로그인 화면이 나타나는 것이다. 하지만 이러한 기본 기능은 SBB 에 그대로 적용하기에는 곤란하므로
* 시큐리티의 설정을 통해 바로 잡아야 한다.
* SBB 는 로그인 없이도 게시물을 조회할 수 있어야 한다.
* */


//  사용자 정의 보안 클래스
//@Configuration 은 스프링의 환경설정 파일임을 의미하는 애너테이션이다. 여기서는 스프링 시큐리티의 설정을 위해 사용되었다.
//@EnableWebSecurity 는 모든 요청 URL 이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션이다.
//@EnableWebSecurity 애너테이션을 사용하면 내부적으로 SpringSecurityFilterChain 이 동작하여 URL 필터가 적용된다.
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserSecurityService userSecurityService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable();

            http
                .authorizeRequests()
                .antMatchers("/**")
                .permitAll()

//                .and().csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                    .headers()
                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))

                // 스프링 시큐리티의 로그인 설정을 담당하는 부분으로 로그인 페이지의 URL은 /user/login이고
                // 로그인 성공시에 이동하는 디폴트 페이지는 루트 URL(/)임을 의미
                .and()
                    .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")

//     로그아웃을 위한 설정을 추가했다. 로그아웃 URL을 /user/logout으로 설정하고 로그아웃이 성공하면 루트(/) 페이지로 이동하도록 했다.
//     그리고 로그아웃시 생성된 사용자 세션도 삭제하도록 처리했다.
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
    * AuthenticationManager 빈을 생성했다. AuthenticationManager 는 스프링 시큐리티의 인증을 담당한다.
    * AuthenticationManager 빈 생성시 스프링의 내부 동작으로 인해
    * 작성한 UserSecurityService 와 PasswordEncoder 가 자동으로 설정된다.
    * */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
