package ch.difty.scipamato.core.web.config

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
open class WicketSecurityConfiguration(
    private val userDetailsService: UserDetailsService,
) {

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .securityContext { ctx -> ctx.requireExplicitSave(false) }
            .authorizeHttpRequests {
                it.requestMatchers(EndpointRequest.to("health", "info")).permitAll()
                    .requestMatchers("/actuator/").hasRole(ADMIN_ROLE)
                    .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN_ROLE)
                    .requestMatchers("/**").permitAll()
            }
            .logout(LogoutConfigurer<HttpSecurity>::permitAll)
            .build()

    @Bean
    @Throws(Exception::class)
    open fun authenticationManager(): AuthenticationManager {
        val provider = DaoAuthenticationProvider().apply {
            setPasswordEncoder(passwordEncoder())
            setUserDetailsService(userDetailsService)
        }
        return ProviderManager(provider)
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    companion object {
        private const val ADMIN_ROLE = "ADMIN"
    }
}
