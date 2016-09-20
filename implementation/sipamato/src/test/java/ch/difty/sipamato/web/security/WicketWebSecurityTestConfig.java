package ch.difty.sipamato.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

/**
 * Overrides the {@link WicketWebSecurityConfig} used in the application for the test context.
 */
@Configuration
@Primary
@Order(101)
public class WicketWebSecurityTestConfig extends WicketWebSecurityConfig {

    private static final String USER = "testuser";
    private static final String ADMIN = "testadmin";
    private static final String PASSWORD = "secretpw";

    @Override
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
     // @formatter:off
        auth
            .inMemoryAuthentication()
                .withUser(USER).password(PASSWORD).roles("USER")
                .and()
                .withUser(ADMIN).password(PASSWORD).roles("USER", "ADMIN");
     // @formatter:on
    }

}
