package ch.difty.scipamato.common.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WicketWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailService;

    public WicketWebSecurityConfig(final UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Autowired
    public void configAuthentication(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
     // @formatter:off
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/**").permitAll()
            .and()
                .logout().permitAll();
     // @formatter:on
    }

}
