package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.persistence.user.JooqUserRepo
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("TooManyFunctions", "FunctionName", "MagicNumber", "SpellCheckingInspection")
@JooqTest
@Testcontainers
internal open class JooqUserRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqUserRepo

    @Test
    fun findingAll() {
        val users = repo.findAll()
        users shouldHaveSize RECORD_COUNT_PREPOPULATED
        users.map { it.id } shouldContainAll listOf(1, 2, 3, 4, 5, 6, 7, 8)
    }

    @Test
    fun findingById_withExistingId_returnsEntity() {
        val user = repo.findById(4) ?: fail { "Unable to find user" }
        user.id shouldBeEqualTo 4
        user.roles.map { it.id } shouldContainSame listOf(1, 2)
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        repo.findById(-1).shouldBeNull()
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val p = makeMinimalUser()
        p.id.shouldBeNull()

        val saved = repo.add(p) ?: fail { "Unable to add user" }
        saved.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        saved.userName shouldBeEqualTo "a"
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
        val user = repo.add(makeMinimalUser()) ?: fail { "Unable to add user" }
        user.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        val id = user.id ?: error("id must no be null now")
        user.userName shouldBeEqualTo "a"

        user.userName = "b"
        repo.update(user)
        user.id as Int shouldBeEqualTo id

        val newCopy = repo.findById(id) ?: fail { "Unable to find user" }
        newCopy shouldNotBeEqualTo user
        newCopy.id shouldBeEqualTo id
        newCopy.userName shouldBeEqualTo "b"
    }

    @Test
    fun deletingRecord() {
        val user = repo.add(makeMinimalUser()) ?: fail { "Unable to add user" }
        user.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        val id = user.id ?: error("id must no be null now")
        user.userName shouldBeEqualTo "a"

        val deleted = repo.delete(id, user.version)
        deleted.id shouldBeEqualTo id

        repo.findById(id).shouldBeNull()
    }

    @Test
    fun findingUserByName_withNonExistingUserName_returnsNull() {
        repo.findByUserName("lkajdsklj").shouldBeNull()
    }

    @Test
    fun findingUserByName_withExistingUserName_returnsUserIncludingRoles() {
        val name = "admin"
        val admin = repo.findByUserName(name) ?: fail { "Unable to find user" }
        admin.userName shouldBeEqualTo name
        admin.roles.shouldNotBeEmpty()
    }

    @Test
    fun updatingAssociatedEntities_addsAndRemovesRoles() {
        val id = newUserAndSave()

        addRoleViewerAndUserToUserWith(id)
        addRoleAdminAndRemoveRoleViewerFrom(id)
        removeRoleAdminFrom(id)
    }

    private fun newUserAndSave(): Int {
        val u = User().apply {
            userName = "test"
            firstName = "fn"
            lastName = "ln"
            isEnabled = false
            email = "u@foo.bar"
            password = "xyz"
        }
        u.id.shouldBeNull()

        val savedUser = repo.add(u) ?: fail { "Unable to add user" }

        savedUser.shouldNotBeNull()
        savedUser.id.shouldNotBeNull()
        savedUser.userName shouldBeEqualTo "test"
        savedUser.roles.shouldBeEmpty()
        savedUser.version shouldBeEqualTo 1

        return savedUser.id ?: error("id must no be null now")
    }

    private fun addRoleViewerAndUserToUserWith(id: Int) {
        val user = repo.findById(id) ?: fail { "Unable to find user" }
        user.addRole(Role.VIEWER)
        user.addRole(Role.USER)
        val viewer = repo.update(user) ?: fail { "Unable to update user" }
        viewer.roles shouldContainSame listOf(Role.VIEWER, Role.USER)
    }

    private fun addRoleAdminAndRemoveRoleViewerFrom(id: Int) {
        val user = repo.findById(id) ?: fail { "Unable to find user" }
        user.addRole(Role.ADMIN)
        user.removeRole(Role.VIEWER)
        val u = repo.update(user) ?: fail { "Unable to update user" }
        u.roles shouldContainSame listOf(Role.USER, Role.ADMIN)
    }

    private fun removeRoleAdminFrom(id: Int) {
        val user = repo.findById(id) ?: fail { "Unable to find user" }
        user.removeRole(Role.ADMIN)
        val u = repo.update(user) ?: fail { "Unable to update user" }
        u.roles shouldContainSame listOf(Role.USER)
    }

    @Test
    fun canUpdateUser_thatHasBeenModifiedElsewhereInTheMeanTime() {
        val user = makeAndValidateNewUser()
        val secondReloaded = loadSameUserIndependentlyAndModifyAndUpdate(user)

        user.lastName = "yetanother"

        @Suppress("TooGenericExceptionCaught")
        try {
            repo.update(user)
            fail { "should have thrown exception" }
        } catch (ex: Exception) {
            ex shouldBeInstanceOf OptimisticLockingException::class
        }

        val reloaded = repo.findById(user.id!!) ?: fail { "Unable to find user" }
        reloaded.version shouldBeEqualTo 2
        reloaded.firstName shouldBeEqualTo secondReloaded.firstName
        reloaded.lastName shouldBeEqualTo secondReloaded.lastName
        reloaded.version shouldBeEqualTo secondReloaded.version
    }

    private fun makeAndValidateNewUser(): User {
        val user = repo.add(makeMinimalUser()) ?: fail { "Unable to add user" }
        user.shouldNotBeNull()
        user.version shouldBeEqualTo 1
        user.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        return user
    }

    private fun loadSameUserIndependentlyAndModifyAndUpdate(user: User): User {
        val secondInstance = repo.findById(user.id!!) ?: fail { "Unable to find user" }
        user.version shouldBeEqualTo secondInstance.version
        secondInstance.firstName = "changed"
        val secondReloaded = repo.update(secondInstance) ?: fail { "Unable to update user" }
        secondReloaded.version shouldBeEqualTo 2
        return secondReloaded
    }

    @Test
    fun cannotDeleteUser_thatHasBeenModifiedElsewhereInTheMeanTime() {
        val user = makeAndValidateNewUser()
        val secondReloaded = loadSameUserIndependentlyAndModifyAndUpdate(user)
        @Suppress("TooGenericExceptionCaught")
        try {
            repo.delete(user.id!!, user.version)
            fail { "Should have thrown exception" }
        } catch (ex: Exception) {
            ex shouldBeInstanceOf OptimisticLockingException::class
        }

        val deletedEntity = repo.delete(user.id!!, secondReloaded.version)
        deletedEntity.version shouldBeEqualTo secondReloaded.version
    }

    companion object {
        private const val MAX_ID_PREPOPULATED = 8
        private const val RECORD_COUNT_PREPOPULATED = 8
    }
}
