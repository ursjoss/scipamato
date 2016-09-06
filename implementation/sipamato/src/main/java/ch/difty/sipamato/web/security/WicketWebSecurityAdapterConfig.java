package ch.difty.sipamato.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WicketWebSecurityAdapterConfig extends WebSecurityConfigurerAdapter {

    @Value("${management.context-path}")
    private String actuatorEndpoints;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests() //
                .antMatchers(actuatorEndpoints()).hasAuthority("ADMIN") // 
                .antMatchers("/**").permitAll().and().logout().permitAll();
        http.headers().frameOptions().disable();
    }

    private String[] actuatorEndpoints() {
        return new String[] { actuatorEndpoints };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // TODO replace stub with real implementation
        auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("USER", "ADMIN");
    }
}
