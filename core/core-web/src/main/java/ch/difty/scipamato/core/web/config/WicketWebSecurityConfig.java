package ch.difty.scipamato.core.web.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WicketWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN_ROLE = "ADMIN";

    private final UserDetailsService userDetailsService;

    WicketWebSecurityConfig(@NotNull final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @NotNull
    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(@NotNull final AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @NotNull
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf().disable()
            .authorizeRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                .antMatchers("/actuator/").hasRole(ADMIN_ROLE)
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN_ROLE)
                .antMatchers("/**").permitAll())
            .logout(LogoutConfigurer::permitAll);
        // @formatter:on
    }
}
