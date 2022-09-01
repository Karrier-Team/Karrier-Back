package com.karrier.mentoring.config;


import com.karrier.mentoring.auth.CustomOAuth2UserService;
import com.karrier.mentoring.handler.LoginSuccessfulHandler;
import com.karrier.mentoring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Autowired
    CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
                .and()
                    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                    .headers().frameOptions().disable()
                .and()
                    .logout()
                            .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                            .logoutSuccessUrl("/")
                .and()
                    .authorizeRequests()
                    .mvcMatchers("/mentors/new").hasRole("USER")
                    .mvcMatchers("community/**").hasAnyRole("USER", "MENTOR_WAIT")
                    .mvcMatchers("members/manage/**", "/members/update-info/**").hasAnyRole("USER", "MENTOR_APPROVE", "MENTOR_WAIT", "ADMIN")
                    .mvcMatchers("/mentors/manage/**").hasRole("MENTOR_APPROVE")
                    .mvcMatchers("/", "/members/**","/members/password/change").permitAll()
                    .mvcMatchers("/admin/**").hasRole("ADMIN")
                    .mvcMatchers("/members/**", "wishList/addWishList", "participation/my/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                    .formLogin()
                        .loginPage("/members/login")
                        .usernameParameter("email")
                        .failureUrl("/members/login/error")
                        .successHandler(new LoginSuccessfulHandler())
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);
        
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }

}
