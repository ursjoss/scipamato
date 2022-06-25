package ch.difty.scipamato.publ.web.config

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
open class WicketWebSecurityConfig(
    private val properties: ScipamatoPublicProperties,
) : WebSecurityConfigurerAdapter() {

    @Bean(name = ["authenticationManager"])
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests { r ->
                r.requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                    .antMatchers("/actuator/").hasRole(ADMIN_ROLE)
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN_ROLE)
                    .antMatchers("/**").permitAll()
            }
            .logout { logout -> logout.permitAll() }
    }

    @Bean
    public override fun userDetailsService(): UserDetailsService =
        InMemoryUserDetailsManager().apply {
            createUser(User
                .withUsername(properties.managementUserName)
                .password(passwordEncoder().encode(properties.managementUserPassword))
                .roles(ADMIN_ROLE)
                .build())
        }

    companion object {
        private const val ADMIN_ROLE = "ADMIN"

        @Bean
        fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
    }
}
