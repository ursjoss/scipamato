package ch.difty.scipamato.core.auth

import ch.difty.scipamato.core.auth.Roles.ADMIN
import ch.difty.scipamato.core.auth.Roles.USER
import ch.difty.scipamato.core.auth.Roles.VIEWER
import ch.difty.scipamato.core.auth.Roles.dummyMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RolesTest {

    @Test
    fun assertRoleName_User() {
        assertThat(USER).isEqualTo("ROLE_USER")
    }

    @Test
    fun assertRoleName_Admin() {
        assertThat(ADMIN).isEqualTo("ROLE_ADMIN")
    }

    @Test
    fun assertRoleName_Viewer() {
        assertThat(VIEWER).isEqualTo("ROLE_VIEWER")
    }

    @Test
    fun dummyTest() {
        assertThat(dummyMethod()).isNull()
    }
}
