package ch.difty.scipamato.core.auth

import ch.difty.scipamato.core.auth.Roles.ADMIN
import ch.difty.scipamato.core.auth.Roles.USER
import ch.difty.scipamato.core.auth.Roles.VIEWER
import ch.difty.scipamato.core.auth.Roles.dummyMethod
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.junit.jupiter.api.Test

internal class RolesTest {

    @Test
    fun assertRoleName_User() {
        USER shouldBeEqualTo "ROLE_USER"
    }

    @Test
    fun assertRoleName_Admin() {
        ADMIN shouldBeEqualTo "ROLE_ADMIN"
    }

    @Test
    fun assertRoleName_Viewer() {
        VIEWER shouldBeEqualTo "ROLE_VIEWER"
    }

    @Test
    fun dummyTest() {
        dummyMethod().shouldBeNull()
    }
}
