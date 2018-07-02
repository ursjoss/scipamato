package ch.difty.scipamato.publ.web.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties;

@Configuration
public class WicketWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN_ROLE = "ADMIN";

    private final ScipamatoPublicProperties properties;

    WicketWebSecurityConfig(final ScipamatoPublicProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests()
                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                .antMatchers("/actuator/").hasRole(ADMIN_ROLE)
                       .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN_ROLE)
                .antMatchers("/**").permitAll()
            .and()
                .logout().permitAll();
        // @formatter:on
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
            .withUsername(properties.getManagementUserName())
            .password(passwordEncoder().encode(properties.getManagementUserPassword()))
            .roles(ADMIN_ROLE)
            .build());
        return manager;
    }

}
