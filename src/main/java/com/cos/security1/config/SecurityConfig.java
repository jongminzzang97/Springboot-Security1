package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

// 구글 로그인이 완료된 후의 후처리가  필요함
// 1. 코드받기(인증), 2. 엑세스토큰(권한), 3. 사용자 정보를 가져오고 4. 그 정보를 토대로 회원가입을 진행
// 4-1 자동회원가입
// 4-2 추가적인 정보가 필요하다면 추가적인 회원가입창

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터 체인데 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // Secured annotation 활성화, PreAuthorize annotation 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable(); 
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			.userInfoEndpoint()
			.userService(principalOauth2UserService);// 구글 로그인이 완료되면 엑세스토큰 + 사용자프로필정보 한번에 받음 
	}

}
