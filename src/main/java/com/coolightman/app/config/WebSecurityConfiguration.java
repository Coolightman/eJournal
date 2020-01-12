package com.coolightman.app.config;

import com.coolightman.app.security.TokenRequestFilter;
import com.coolightman.app.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private TokenRequestFilter tokenRequestFilter;

    public WebSecurityConfiguration(final UserDetailsServiceImpl userDetailsService,
                                    final TokenRequestFilter tokenRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.tokenRequestFilter = tokenRequestFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/img/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/", "/index", "/login", "/logout", "/authenticate")
                .permitAll();

        http.authorizeRequests().and()
                .exceptionHandling()
                .accessDeniedPage("/403");

        http.authorizeRequests().and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/userPage")
                .failureUrl("/login?error=true");

        http.authorizeRequests().and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/index")
                .addLogoutHandler(new CustomLogoutHandler());

        http.authorizeRequests().anyRequest().authenticated();

        http.authorizeRequests().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(tokenRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
