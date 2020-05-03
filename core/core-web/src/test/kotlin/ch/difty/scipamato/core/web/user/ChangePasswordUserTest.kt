package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.User
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ChangePasswordUserTest {

    private val user = User()
    private lateinit var cpu: ChangePasswordUser

    @BeforeEach
    fun setUp() {
        user.id = 1
        user.userName = "un"
        user.firstName = "fn"
        user.lastName = "ln"
        user.email = "em"
        user.password = "pw"
        user.isEnabled = true
        user.setRoles(setOf(Role.ADMIN, Role.USER))
    }

    @Test
    fun fromUser_withoutResettingPassword_hasThePasswordButNoCurrentPasswordNorPassword2() {
        cpu = ChangePasswordUser(user)
        assertUserBackedFields(cpu)
        cpu.password shouldBeEqualTo "pw"
        cpu.currentPassword.shouldBeNull()
        cpu.password2.shouldBeNull()
    }

    @Test
    fun fromUser_withResettingPassword_hasNoneOfThePasswords() {
        cpu = ChangePasswordUser(user, true)
        assertUserBackedFields(cpu)
        cpu.password.shouldBeNull()
        cpu.currentPassword.shouldBeNull()
        cpu.password2.shouldBeNull()
    }

    private fun assertUserBackedFields(u: ChangePasswordUser) {
        u.id shouldBeEqualTo 1
        u.userName shouldBeEqualTo "un"
        u.firstName shouldBeEqualTo "fn"
        u.lastName shouldBeEqualTo "ln"
        u.email shouldBeEqualTo "em"
        u.isEnabled shouldBeEqualTo true
        u.roles shouldContainAll listOf(Role.ADMIN, Role.USER)
        u.fullName shouldBeEqualTo "fn ln"
        u.displayValue shouldBeEqualTo "un"
    }

    @get:Test
    val set: Unit
        get() {
            cpu = ChangePasswordUser()
            cpu.id = 1
            cpu.userName = "un"
            cpu.firstName = "fn"
            cpu.lastName = "ln"
            cpu.email = "em"
            cpu.password = "pw"
            cpu.password2 = "pw2"
            cpu.currentPassword = "cpw"
            cpu.isEnabled = true
            cpu.setRoles(listOf(Role.ADMIN, Role.USER))
            assertUserBackedFields(cpu)
            cpu.currentPassword shouldBeEqualTo "cpw"
            cpu.password2 shouldBeEqualTo "pw2"
        }

    @Test
    fun canAddRole() {
        cpu = ChangePasswordUser(user)
        cpu.addRole(Role.VIEWER)
        cpu.setRoles(listOf(Role.ADMIN, Role.USER, Role.VIEWER))
    }

    @Test
    fun canRemoveRole() {
        cpu = ChangePasswordUser(user)
        cpu.removeRole(Role.USER)
        cpu.setRoles(listOf(Role.ADMIN))
    }

    @Test
    fun canGetUser_neverNull() {
        cpu = ChangePasswordUser(user)
        cpu.toUser() shouldBeEqualTo user
    }

    @Test
    fun canGetUser2_neverNull() {
        cpu = ChangePasswordUser()
        cpu.toUser().shouldNotBeNull()
    }

    @Test
    fun canGetUser3_neverNull() {
        cpu = ChangePasswordUser(user, true)
        cpu.toUser().shouldNotBeNull()
    }

    @Test
    fun canGetUser4_neverNull() {
        cpu = ChangePasswordUser(user, false)
        cpu.toUser().shouldNotBeNull()
    }

    @Test
    fun canGetRolesString() {
        cpu = ChangePasswordUser(user)
        cpu.rolesString shouldBeEqualTo "ADMIN, USER"
    }

    @Test
    fun canGetRoles() {
        cpu = ChangePasswordUser(user)
        cpu.roles shouldContainSame listOf(Role.ADMIN, Role.USER)
        cpu.setRoles(listOf(Role.VIEWER, Role.USER))
        cpu.roles shouldContainSame listOf(Role.VIEWER, Role.USER)
    }
}
