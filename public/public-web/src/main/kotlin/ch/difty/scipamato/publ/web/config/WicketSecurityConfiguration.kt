package ch.difty.scipamato.publ.web.config

import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
open class WicketSecurityConfiguration(
    private val properties: ScipamatoPublicProperties,
) {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http.csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .authorizeHttpRequests {
                it.requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                    .requestMatchers("/actuator/").hasRole(ADMIN_ROLE)
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN_ROLE)
                    .requestMatchers("/**").permitAll()
            }
            .logout(LogoutConfigurer<HttpSecurity>::permitAll)
            .build()

    @Bean
    open fun userDetailsService(): UserDetailsService =
        InMemoryUserDetailsManager().apply {
            createUser(User
                .withUsername(properties.managementUserName)
                .password(BCryptPasswordEncoder().encode(properties.managementUserPassword))
                .roles(ADMIN_ROLE)
                .build())
        }

    @Bean
    @Throws(Exception::class)
    open fun authenticationManager(): AuthenticationManager {
        val provider = DaoAuthenticationProvider().apply {
            setPasswordEncoder(BCryptPasswordEncoder())
            setUserDetailsService(userDetailsService())
        }
        return ProviderManager(provider)
    }

    companion object {
        private const val ADMIN_ROLE = "ADMIN"
    }
}
