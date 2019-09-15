package ch.difty.scipamato.core.auth

import ch.difty.scipamato.core.auth.Roles.*
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
