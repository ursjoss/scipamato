package ch.difty.sipamato.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.difty.sipamato.auth.Role;

@EnableWebSecurity
public class WicketWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${management.context-path}")
    private String actuatorEndpoints;

    @Autowired
    private UserDetailsService userDetailService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
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
                .antMatchers(actuatorEndpoints()).hasRole(Role.ADMIN.name())
                .antMatchers("/**").permitAll()
            .and()
                .logout().permitAll();
        http.headers().frameOptions().disable();
     // @formatter:on
    }

    private String[] actuatorEndpoints() {
        return new String[] { actuatorEndpoints };
    }

}
