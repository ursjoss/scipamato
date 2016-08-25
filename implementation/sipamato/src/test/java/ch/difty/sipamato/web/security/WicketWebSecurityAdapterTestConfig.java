package ch.difty.sipamato.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

/**
 * Overrides the {@link WicketWebSecurityAdapterConfig} used in the application for the test context.
 */
@Configuration
@Primary
@Order(99)
public class WicketWebSecurityAdapterTestConfig extends WicketWebSecurityAdapterConfig {

    private static final String USER = "testuser";
    private static final String PASSWORD = "secretpw";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(USER).password(PASSWORD).authorities("USER", "ADMIN");
    }

}
