package com.coolightman.app.config;

import com.coolightman.app.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfiguration(final UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        // Setting Service to find User in the database.
        // And Setting PasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests().antMatchers("/", "/index", "/login", "/logout").permitAll();

        http.authorizeRequests().antMatchers("/userPage")
                .access("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_PUPIL', 'ROLE_PARENT')");

        http.authorizeRequests().antMatchers("/admins/**")
                .access("hasRole('ROLE_ADMIN')");

        http.authorizeRequests().antMatchers("/teachers/**")
                .access("hasAnyRole('ROLE_TEACHER', 'ROLE_ADMIN')");

        http.authorizeRequests().antMatchers("/parents/**")
                .access("hasAnyRole('ROLE_PARENT', 'ROLE_ADMIN')");

        http.authorizeRequests().antMatchers("/pupils/**")
                .access("hasAnyRole('ROLE_PUPIL', 'ROLE_PARENT', 'ROLE_ADMIN')");

        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().and().formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/userPage")
                .failureUrl("/login?error=true")
                .usernameParameter("auth_login")
                .passwordParameter("auth_password")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/index");
    }
}
