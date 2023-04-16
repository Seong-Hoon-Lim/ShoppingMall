package com.shoppingmall.config;

import com.shoppingmall.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * WebSecurityConfigurerAdapter 상속받는 클래스에 @EnableWebSecurity 을
 * 선언하면 SpringSecurityFilterChain이 자동으로 포함됨
 * 오버라이딩을 통해 보안 설정을 커스터마이징 할 수 있다
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService service;

    /*
     http 요청에 대한 보안을 설정, 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드
     등에 대한 설정을 작성.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")    //로그인 페이지 URL 설정
                .defaultSuccessUrl("/")     //로그인 성공시 이동할 URL 설정
                .usernameParameter("email")     //로그인 시 사용할 파라미터 이름으로 email을 지정
                .failureUrl("/members/login/error")    //로그인 실패시 이동할 URL을 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))     //로그아웃 URL 설정
                .logoutSuccessUrl("/");     //로그아웃 성공 시 이동할 URL 설정

        /* http.authorizeRequests() : 시큐리티 처리에 HttpServletRequest 를 이용 */
        http.authorizeRequests()
                /*
                 permitAll() 을 통해 모든 사용자가 인증(로그인) 없이 해당 경로에 접근할 수 있도록 설정
                 메인 페이지, 회원관련 URL, 상품 상세 페이지, 상품 이미지 불러오는 경로가 해당됨
                 */
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**")
                .permitAll()
                //"/admin" 으로 시작하는 경로는 ADMIN ROLE 일 때만 접근 가능하도록 설정
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                //위에서 설정한 경로를 제외한 나머지 경로들은 모두 인증을 요구하도록 설정
                .anyRequest().authenticated();

        //인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록
        http.exceptionHandling().authenticationEntryPoint
                (new CustomAuthenticationEntryPoint()); 

    }

    /*
     비밀번호를 데이터베이스에 그대로 저장할 경우를 대비하여 BCryptPasswordEncoder 의
     해시함수를 이용하여 비밀번호를 암호화하여 저장하기 위해 빈으로 등록.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     스프링 시큐리티에서 인증은 AuthenticationManager 을 통해 이루어지며 AuthenticationManagerBuilder rk
     AuthenticationManager를 생성해줌
     userDetailService를 구현하고 있는 service 객체를 지정하고 비밀번호 암호화를 지정해줌
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service)
                .passwordEncoder(passwordEncoder());
    }

    /*
     static 디렉터리의 하위 파일은 인증을 무시하도록 설정 (css, js, image 파일 등)
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/images/**");
    }
}
