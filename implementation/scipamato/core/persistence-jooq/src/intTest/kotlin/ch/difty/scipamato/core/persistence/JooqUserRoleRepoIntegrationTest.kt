package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.persistence.user.JooqUserRoleRepo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("FunctionName", "MagicNumber")
@JooqTest
@Testcontainers
internal open class JooqUserRoleRepoIntegrationTest {

    @Autowired
    private val repo: JooqUserRoleRepo? = null

    @Test
    fun findingRolesForUser_withTwoRoles_returnsBoth() {
        val roles = repo!!.findRolesForUser(4)
        assertThat(roles).containsExactlyInAnyOrder(Role.ADMIN, Role.USER)
    }

    @Test
    fun findingRolesForUser_withNoRoles_returnsNone() {
        val roles = repo!!.findRolesForUser(7)
        assertThat(roles).isEmpty()
    }
}
