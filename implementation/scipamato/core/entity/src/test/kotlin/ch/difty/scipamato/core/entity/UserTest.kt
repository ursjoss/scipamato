package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

internal class UserTest {

    private val user = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)

    private val roles = HashSet<Role>()

    private val role1 = Role.ADMIN
    private val role2 = Role.USER
    private val role3 = Role.VIEWER

    @BeforeEach
    fun setUp() {
        roles.addAll(Arrays.asList(role1, role2))
    }

    @Test
    fun setAndGet() {
        val u = User()
        u.id = ID
        u.userName = USER_NAME
        u.firstName = FIRST_NAME
        u.lastName = LAST_NAME
        u.email = EMAIL
        u.password = PASSWORD
        u.isEnabled = ENABLED
        u.roles = roles

        assertUser(u)
    }

    private fun assertUser(u: User) {
        assertThat(u.id).isEqualTo(1)
        assertThat(u.userName).isEqualTo(USER_NAME)
        assertThat(u.firstName).isEqualTo(FIRST_NAME)
        assertThat(u.lastName).isEqualTo(LAST_NAME)
        assertThat(u.email).isEqualTo(EMAIL)
        assertThat(u.password).isEqualTo(PASSWORD)
        assertThat(u.isEnabled).isEqualTo(ENABLED)
        assertThat(u.roles).containsExactlyInAnyOrder(role1, role2)
        assertThat(u.fullName).isEqualTo("$FIRST_NAME $LAST_NAME")
    }

    @Test
    fun constructingByUser() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        val u1 = User(u)
        assertUser(u1)
    }

    @Test
    fun constructingByUser2() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)
        assertThat(u.isEnabled).isFalse()
        assertThat(u.roles)
                .isNotNull
                .isEmpty()

        u.isEnabled = true
        u.roles = roles
        assertUser(u)
    }

    @Test
    fun displayValue_isEqualToName() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        assertThat(u.displayValue).isEqualTo(USER_NAME)
    }

    @Test
    fun reducedConstructor_leavesSomeFieldsDefault() {
        assertThat(user.id).isEqualTo(1)
        assertThat(user.userName).isEqualTo(USER_NAME)
        assertThat(user.firstName).isEqualTo(FIRST_NAME)
        assertThat(user.lastName).isEqualTo(LAST_NAME)
        assertThat(user.email).isEqualTo(EMAIL)
        assertThat(user.password).isEqualTo(PASSWORD)

        assertThat(user.isEnabled).isFalse()
        assertThat(user.roles).isEmpty()
    }

    @Test
    fun settingRoles_reSetsRoles() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        assertThat(u.roles).containsExactlyInAnyOrder(role1, role2)

        u.roles = setOf(role3, role1)

        assertThat(u.roles).containsExactlyInAnyOrder(role3, role1)
    }

    @Test
    fun settingRole_withNullList_clearsList() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.roles = null
        assertEquals(0, u.roles.size)
    }

    @Test
    fun settingRole_withBlankList_clearsList() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.roles = emptySet()
        assertThat(u.roles).isEmpty()
    }

    @Test
    fun removingAssignedRole_removesIt() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.removeRole(role1)
        assertThat(u.roles).containsExactly(role2)
    }

    @Test
    fun removingUnassignedRole_doesNothing() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.removeRole(role3)
        assertThat(u.roles).containsExactlyInAnyOrder(role1, role2)
    }

    @Test
    fun addingNullRole_addsNothing() {
        user.addRole(role1)
        assertThat(user.roles).containsExactly(role1)
    }

    @Test
    fun equals() {
        EqualsVerifier
                .forClass(User::class.java)
                .withRedefinedSuperclass()
                .usingGetClass()
                .withIgnoredFields(CREATED.fieldName, CREATOR_ID.fieldName, MODIFIED.fieldName, MODIFIER_ID.fieldName)
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify()
    }

    @Test
    @Disabled("TODO")
    fun testingToString() {
        assertThat(user.toString()).isEqualTo(
                "User[userName=username,firstName=firstname,lastName=lastname,email=email,password=password,enabled=false,roles=[],id=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]")
    }

    @Test
    fun assertEnumFields() {
        assertThat(User.UserFields.values()).extracting("name")
                .containsExactly("userName", "firstName", "lastName", "email", "password", "enabled", "roles")
    }

    companion object {
        private val ID = 1
        private val USER_NAME = "username"
        private val FIRST_NAME = "firstname"
        private val LAST_NAME = "lastname"
        private val EMAIL = "email"
        private val PASSWORD = "password"
        private val ENABLED = true
    }

}
