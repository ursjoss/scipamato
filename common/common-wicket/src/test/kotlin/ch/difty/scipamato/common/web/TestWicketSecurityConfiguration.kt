package ch.difty.scipamato.common.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
open class TestWicketSecurityConfiguration {

    @Bean
    @Throws(Exception::class)
    open fun authenticationManager(): AuthenticationManager {
        val provider = DaoAuthenticationProvider().apply {
            setPasswordEncoder(BCryptPasswordEncoder())
            setUserDetailsService(TestUserDetailsService())
        }
        return ProviderManager(provider)
    }

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .securityContext { ctx -> ctx.requireExplicitSave(false) }
            .csrf().disable()
            .authorizeHttpRequests { it.requestMatchers("/**").permitAll() }
            .logout(LogoutConfigurer<HttpSecurity>::permitAll)
            .build()

    /**
     * This service has precedence over the productive implementations of
     * [UserDetailsService]. It will not be instantiated on production as it
     * is in the test package, so the productive implementation will be the only one.
     *
     * We're using two hard-coded users, ignoring the database entirely.
     */
    @Suppress("SpellCheckingInspection")
    class TestUserDetailsService : UserDetailsService {

        private val users = mapOf(
            ADMIN to User(
                username = ADMIN, password = PASSWORD,
                roles = listOf(Role("ROLE_ADMIN"), Role("ROLE_USER"))
            ),
            USER to User(username = USER, password = PASSWORD, roles = listOf(Role("ROLE_USER"))),
            VIEWER to User(username = VIEWER, password = PASSWORD, roles = listOf(Role("ROLE_VIEWER")))
        )

        override fun loadUserByUsername(username: String): UserDetails =
            users[username] ?: throw UsernameNotFoundException("No user found with name $username")

        @Suppress("LongParameterList")
        class User(
            private val accountNonExpired: Boolean = true,
            private val accountNonLocked: Boolean = true,
            private val credentialsNonExpired: Boolean = true,
            private val enabled: Boolean = true,
            private val username: String,
            private val password: String?,
            private val roles: List<Role>,
        ) : UserDetails {
            override fun getUsername() = username
            override fun isCredentialsNonExpired() = credentialsNonExpired
            override fun getPassword() = password
            override fun isAccountNonExpired() = accountNonExpired
            override fun isAccountNonLocked() = accountNonLocked
            override fun isEnabled() = enabled
            override fun getAuthorities(): Collection<GrantedAuthority> =
                AuthorityUtils.commaSeparatedStringToAuthorityList(roles.joinToString { "," })
        }

        class Role(private val authority: String?) : GrantedAuthority {
            override fun getAuthority(): String? = authority
        }

        companion object {
            private const val ADMIN = "testadmin"
            private const val USER = "testuser"
            private const val VIEWER = "testviewer"

            // BCrypt encrypted password 'secretpw' as defined in {@link WicketTest}
            private const val PASSWORD = "$2a$08\$O/YZvh/jf1RWaZkpLPzfUeCkVczIaGLV0.vTKDCbxb0qn37qpj.Je"
        }
    }
}
