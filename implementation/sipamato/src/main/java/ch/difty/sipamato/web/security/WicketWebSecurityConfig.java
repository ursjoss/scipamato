package ch.difty.sipamato.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WicketWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${management.context-path}")
    private String actuatorEndpoints;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
     // @formatter:off
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers(actuatorEndpoints()).hasAuthority("ADMIN")
                .antMatchers("/**").permitAll()
            .and()
                .logout().permitAll();
        http.headers().frameOptions().disable();
     // @formatter:on
    }

    private String[] actuatorEndpoints() {
        return new String[] { actuatorEndpoints };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // TODO replace stub with real implementation
     // @formatter:off
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").authorities("USER", "ADMIN");
     // @formatter:on
    }
}
