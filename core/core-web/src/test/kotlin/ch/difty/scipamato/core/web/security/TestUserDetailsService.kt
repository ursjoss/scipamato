package ch.difty.scipamato.core.web.security

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.web.authentication.ScipamatoUserDetails
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * This service has precedence over the productive implementations of
 * [UserDetailsService]. It will not be instantiated on production as it
 * is in the test package, so the productive implementation will be the only one.
 *
 *
 * We're using two hard-coded users, ignoring the database entirely.
 *
 * @author u.joss
 */
@Service
@Primary
class TestUserDetailsService : UserDetailsService {

    private val users: MutableMap<String, User> = HashMap()

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = users[username]
        return user?.let { ScipamatoUserDetails(it) }
            ?: throw UsernameNotFoundException("No user found with name $username")
    }

    companion object {
        const val USER_ADMIN = "testadmin"
        const val USER_USER = "testuser"
        const val USER_VIEWER = "testviewer"

        // BCrypt encrypted password 'secretpw' as defined in {@link WicketTest}
        private const val PASSWORD = "$2a$08\$O/YZvh/jf1RWaZkpLPzfUeCkVczIaGLV0.vTKDCbxb0qn37qpj.Je"
    }

    init {
        users[USER_ADMIN] =
            User(1, USER_ADMIN, "a", "a", "a", PASSWORD, true, setOf(Role.ADMIN, Role.USER))
        users[USER_USER] =
            User(2, USER_USER, "t", "u", "tu", PASSWORD, true, setOf(Role.USER))
        users[USER_VIEWER] =
            User(3, USER_VIEWER, "v", "v", "vv", PASSWORD, true, setOf(Role.VIEWER))
    }
}
