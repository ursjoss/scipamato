package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.user.JooqUserRepo
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
internal open class JooqUserRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqUserRepo

    @Test
    fun findingAll() {
        val users = repo.findAll()
        assertThat(users).hasSize(RECORD_COUNT_PREPOPULATED)
        assertThat(users.map { it.id }).containsExactly(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun findingById_withExistingId_returnsEntity() {
        val user = repo.findById(4)
        assertThat(user.id).isEqualTo(4)
        assertThat(user.roles.map { it.id }).containsExactlyInAnyOrder(1, 2)
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1)).isNull()
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val p = makeMinimalUser()
        assertThat(p.id == null).isTrue()

        val saved = repo.add(p)
        assertThat(saved.id).isGreaterThan(MAX_ID_PREPOPULATED)
        assertThat(saved.userName).isEqualTo("a")
    }

    private fun makeMinimalUser(): User {
        return User().apply {
            userName = "a"
            firstName = "b"
            lastName = "c"
            email = "d"
            password = "e"
        }
    }

    @Test
    fun updatingRecord() {
        val user = repo.add(makeMinimalUser())
        assertThat(user.id).isGreaterThan(MAX_ID_PREPOPULATED)
        val id = user.id
        assertThat(user.userName).isEqualTo("a")

        user.userName = "b"
        repo.update(user)
        assertThat(user.id).isEqualTo(id)

        val newCopy = repo.findById(id)
        assertThat(newCopy).isNotEqualTo(user)
        assertThat(newCopy.id).isEqualTo(id)
        assertThat(newCopy.userName).isEqualTo("b")
    }

    @Test
    fun deletingRecord() {
        val user = repo.add(makeMinimalUser())
        assertThat(user.id).isGreaterThan(MAX_ID_PREPOPULATED)
        val id = user.id
        assertThat(user.userName).isEqualTo("a")

        val deleted = repo.delete(id, user.version)
        assertThat(deleted.id).isEqualTo(id)

        assertThat(repo.findById(id)).isNull()
    }

    @Test
    fun findingUserByName_withNonExistingUserName_returnsNull() {
        assertThat(repo.findByUserName("lkajdsklj") == null).isTrue()
    }

    @Test
    fun findingUserByName_withExistingUserName_returnsUserIncludingRoles() {
        val name = "admin"
        val admin = repo.findByUserName(name)
        assertThat(admin.userName).isEqualTo(name)
        assertThat(admin.roles).isNotEmpty
    }

    @Test
    fun updatingAssociatedEntities_addsAndRemovesRoles() {
        val id = newUserAndSave()

        addRoleViewerAndUserToUserWith(id)
        addRoleAdminAndRemoveRoleViewerFrom(id)
        removeRoleAdminFrom(id)
    }

    private fun newUserAndSave(): Int? {
        val u = User().apply {
            userName = "test"
            firstName = "fn"
            lastName = "ln"
            isEnabled = false
            email = "u@foo.bar"
            password = "xyz"
        }
        assertThat(u.id == null).isTrue()

        val savedUser = repo.add(u)

        assertThat(savedUser).isNotNull
        assertThat(savedUser.id == null).isFalse()
        assertThat(savedUser.userName).isEqualTo("test")
        assertThat(savedUser.roles).isEmpty()
        assertThat(savedUser.version).isEqualTo(1)

        return savedUser.id
    }

    private fun addRoleViewerAndUserToUserWith(id: Int?) {
        val user = repo.findById(id)
        user.addRole(Role.VIEWER)
        user.addRole(Role.USER)
        val viewer = repo.update(user)
        assertThat(viewer.roles).containsOnly(Role.VIEWER, Role.USER)
    }

    private fun addRoleAdminAndRemoveRoleViewerFrom(id: Int?) {
        val user = repo.findById(id)
        user.addRole(Role.ADMIN)
        user.removeRole(Role.VIEWER)
        val u = repo.update(user)
        assertThat(u.roles).containsOnly(Role.USER, Role.ADMIN)
    }

    private fun removeRoleAdminFrom(id: Int?) {
        val user = repo.findById(id)
        user.removeRole(Role.ADMIN)
        val u = repo.update(user)
        assertThat(u.roles).containsOnly(Role.USER)
    }

    @Test
    fun canUpdateUser_thatHasBeenModifiedElsewhereInTheMeanTime() {
        val user = makeAndValidateNewUser()
        val secondReloaded = loadSameUserIndependentlyAndModifyAndUpdate(user)


        user.lastName = "yetanother"

        try {
            repo.update(user)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(OptimisticLockingException::class.java)
        }

        val reloaded = repo.findById(user.id)
        assertThat(reloaded.version).isEqualTo(2)
        assertThat(reloaded.firstName).isEqualTo(secondReloaded.firstName)
        assertThat(reloaded.lastName).isEqualTo(secondReloaded.lastName)
        assertThat(reloaded.version).isEqualTo(secondReloaded.version)
    }

    private fun makeAndValidateNewUser(): User {
        val user = repo.add(makeMinimalUser())
        assertThat(user).isNotNull
        assertThat(user.version).isEqualTo(1)
        assertThat(user.id).isGreaterThan(MAX_ID_PREPOPULATED)
        return user
    }

    private fun loadSameUserIndependentlyAndModifyAndUpdate(user: User): User {
        val secondInstance = repo.findById(user.id)
        assertThat(user.version).isEqualTo(secondInstance.version)
        secondInstance.firstName = "changed"
        val secondReloaded = repo.update(secondInstance)
        assertThat(secondReloaded.version).isEqualTo(2)
        return secondReloaded
    }

    @Test
    fun cannotDeleteUser_thatHasBeenModifiedElsewhereInTheMeanTime() {
        val user = makeAndValidateNewUser()
        val secondReloaded = loadSameUserIndependentlyAndModifyAndUpdate(user)
        try {
            repo.delete(user.id, user.version)
            fail<Any>("Should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(OptimisticLockingException::class.java)
        }

        val deletedEntity = repo.delete(user.id, secondReloaded.version)
        assertThat(deletedEntity.version).isEqualTo(secondReloaded.version)
    }

    companion object {
        private const val MAX_ID_PREPOPULATED = 8
        private const val RECORD_COUNT_PREPOPULATED = 8
    }
}
