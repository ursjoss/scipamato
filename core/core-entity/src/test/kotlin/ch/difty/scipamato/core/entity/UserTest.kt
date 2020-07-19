package ch.difty.scipamato.core.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID
import ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class UserTest {

    private val user = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD)

    private val roles = HashSet<Role>()

    private val role1 = Role.ADMIN
    private val role2 = Role.USER
    private val role3 = Role.VIEWER

    @BeforeEach
    fun setUp() {
        roles.addAll(listOf(role1, role2))
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
        u.id shouldBeEqualTo 1
        u.userName shouldBeEqualTo USER_NAME
        u.firstName shouldBeEqualTo FIRST_NAME
        u.lastName shouldBeEqualTo LAST_NAME
        u.email shouldBeEqualTo EMAIL
        u.password shouldBeEqualTo PASSWORD
        u.isEnabled shouldBeEqualTo ENABLED
        u.roles shouldContainSame listOf(role1, role2)
        u.fullName shouldBeEqualTo "$FIRST_NAME $LAST_NAME"
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
        u.isEnabled.shouldBeFalse()
        u.roles.shouldNotBeNull().shouldBeEmpty()

        u.isEnabled = true
        u.roles = roles
        assertUser(u)
    }

    @Test
    fun displayValue_isEqualToName() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.displayValue shouldBeEqualTo USER_NAME
    }

    @Test
    fun reducedConstructor_leavesSomeFieldsDefault() {
        user.id shouldBeEqualTo 1
        user.userName shouldBeEqualTo USER_NAME
        user.firstName shouldBeEqualTo FIRST_NAME
        user.lastName shouldBeEqualTo LAST_NAME
        user.email shouldBeEqualTo EMAIL
        user.password shouldBeEqualTo PASSWORD

        user.isEnabled.shouldBeFalse()
        user.roles.shouldBeEmpty()
    }

    @Test
    fun settingRoles_reSetsRoles() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.roles shouldContainSame listOf(role1, role2)

        u.roles = setOf(role3, role1)

        u.roles shouldContainSame listOf(role3, role1)
    }

    @Test
    fun settingRole_withNullList_clearsList() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.roles = null
        u.roles.size shouldBeEqualTo 0
    }

    @Test
    fun settingRole_withBlankList_clearsList() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.roles = emptySet()
        u.roles.shouldBeEmpty()
    }

    @Test
    fun removingAssignedRole_removesIt() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.removeRole(role1)
        u.roles shouldContainAll listOf(role2)
    }

    @Test
    fun removingUnassignedRole_doesNothing() {
        val u = User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles)
        u.removeRole(role3)
        u.roles shouldContainSame listOf(role1, role2)
    }

    @Test
    fun addingNullRole_addsNothing() {
        user.addRole(role1)
        user.roles shouldContainSame listOf(role1)
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(User::class.java)
            .withRedefinedSuperclass()
            .usingGetClass()
            .withIgnoredFields(CREATED.fieldName, CREATOR_ID.fieldName, MODIFIED.fieldName, MODIFIER_ID.fieldName)
            .verify()
    }

    @Test
    @Disabled("TODO")
    fun testingToString() {
        user.toString() shouldBeEqualTo
            """User[userName=username,firstName=firstname,lastName=lastname,email=email,password=password
                    |,enabled=false,roles=[],id=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>
                    |,lastModified=<null>,version=0]""".trimMargin()
    }

    @Test
    fun assertEnumFields() {
        User.UserFields.values().map { it.fieldName } shouldContainSame
            listOf("userName", "firstName", "lastName", "email", "password", "enabled", "roles")
    }

    companion object {
        private const val ID = 1
        private const val USER_NAME = "username"
        private const val FIRST_NAME = "firstname"
        private const val LAST_NAME = "lastname"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val ENABLED = true
    }
}
