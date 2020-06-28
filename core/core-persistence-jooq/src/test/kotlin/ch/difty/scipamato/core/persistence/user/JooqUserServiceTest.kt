package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.entity.search.UserFilter
import ch.difty.scipamato.core.persistence.UserRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

@Suppress("UsePropertyAccessSyntax")
internal class JooqUserServiceTest {

    private val repoMock = mockk<UserRepository>()
    private val passwordEncoderMock = mockk<PasswordEncoder>()
    private val filterMock = mockk<UserFilter>()
    private val paginationContextMock = mockk<PaginationContext>()
    private val userMock = mockk<User>()

    private var service = JooqUserService(repoMock, passwordEncoderMock)

    private val users = listOf(userMock, userMock)

    @AfterEach
    fun tearDown() {
        confirmVerified(repoMock, passwordEncoderMock, filterMock, paginationContextMock, userMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7
        every { repoMock.findById(id) } returns userMock

        val optUser = service.findById(id)
        optUser.isPresent.shouldBeTrue()
        optUser.get() shouldBeEqualTo userMock

        verify { repoMock.findById(id) }
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7
        every { repoMock.findById(id) } returns null
        service.findById(id).isPresent.shouldBeFalse()
        verify { repoMock.findById(id) }
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        every { repoMock.findPageByFilter(filterMock, paginationContextMock) } returns users
        service.findPageByFilter(filterMock, paginationContextMock) shouldBeEqualTo users
        verify { repoMock.findPageByFilter(filterMock, paginationContextMock) }
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        every { repoMock.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repoMock.countByFilter(filterMock) }
    }

    @Test
    fun savingOrUpdating_withUserWithNullId_hasRepoAddTheUser() {
        every { userMock.id } returns null
        every { userMock.password } returns "boo"
        every { passwordEncoderMock.encode("boo") } returns "bar"
        every { userMock.password = "bar" } returns Unit
        every { repoMock.add(userMock) } returns userMock
        service.saveOrUpdate(userMock) shouldBeEqualTo userMock
        verify { repoMock.add(userMock) }
        verify { userMock.id }
        verify { userMock.password }
        verify { userMock.password = "bar" }
        verify { passwordEncoderMock.encode("boo") }
    }

    @Test
    fun savingOrUpdating_withUserWithNonNullId_hasRepoUpdateTheUser() {
        every { userMock.id } returns 17
        every { repoMock.update(userMock) } returns userMock
        every { userMock.password } returns "boo"
        every { passwordEncoderMock.encode("boo") } returns "bar"
        every { userMock.password = "bar" } returns Unit
        service.saveOrUpdate(userMock) shouldBeEqualTo userMock
        verify { repoMock.update(userMock) }
        verify { userMock.id }
        verify { userMock.password }
        verify { passwordEncoderMock.encode("boo") }
        verify { userMock.password = "bar" }
    }

    @Test
    fun savingOrUpdating_withUserWithNullId_withPassword_hasRepoAddTheUserAfterEncodingThePassword() {
        every { userMock.id } returns null
        every { userMock.password } returns "foo"
        every { passwordEncoderMock.encode("foo") } returns "bar"
        every { userMock.password = "bar" } returns Unit
        every { repoMock.add(userMock) } returns userMock

        service.saveOrUpdate(userMock) shouldBeEqualTo userMock

        verify { repoMock.add(userMock) }
        verify { passwordEncoderMock.encode("foo") }
        verify { userMock.id }
        verify { userMock.password }
        verify { userMock.password = "bar" }
    }

    @Test
    fun savingOrUpdating_withUserWithNonNullId_withPassword_hasRepoUpdateTheUserAfterEncodingThePassword() {
        every { userMock.id } returns 17
        every { userMock.password } returns "foo"
        every { passwordEncoderMock.encode("foo") } returns "bar"
        every { userMock.password = "bar" } returns Unit
        every { repoMock.update(userMock) } returns userMock

        service.saveOrUpdate(userMock) shouldBeEqualTo userMock

        verify { repoMock.update(userMock) }
        verify { passwordEncoderMock.encode("foo") }
        verify { userMock.id }
        verify { userMock.password }
        verify { userMock.password = "bar" }
    }

    @Test
    fun findingByUserName_withNullName_returnsEmptyOptional() {
        service.findByUserName(null).isPresent.shouldBeFalse()
    }

    @Test
    fun findingByUserName_whenFindingUser_delegatesToRepoAndReturnsOptionalOfFoundUser() {
        every { repoMock.findByUserName("foo") } returns userMock
        service.findByUserName("foo") shouldBeEqualTo java.util.Optional.of(userMock)
        verify { repoMock.findByUserName("foo") }
    }

    @Test
    fun findingByUserName_whenNotFindingUser_delegatesToRepoAndReturnsOptionalEmpty() {
        every { repoMock.findByUserName("foo") } returns null
        service.findByUserName("foo") shouldBeEqualTo java.util.Optional.empty<Any>()
        verify { repoMock.findByUserName("foo") }
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify(exactly = 0) { repoMock.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        every { userMock.id } returns null

        service.remove(userMock)

        verify { userMock.id }
        verify(exactly = 0) { repoMock.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        every { userMock.id } returns 3
        every { userMock.version } returns 2
        every { repoMock.delete(3, 2) } returns userMock

        service.remove(userMock)

        verify(exactly = 2) { userMock.id }
        verify(exactly = 1) { userMock.version }
        verify(exactly = 1) { repoMock.delete(3, 2) }
    }

    @Test
    fun findingPageOfIdsByFilter_delegatesToRepo() {
        every { repoMock.findPageOfIdsByFilter(filterMock, paginationContextMock) } returns listOf(3, 8, 5)
        service.findPageOfIdsByFilter(filterMock, paginationContextMock) shouldContainAll listOf(3, 8, 5)
        verify { repoMock.findPageOfIdsByFilter(filterMock, paginationContextMock) }
    }
}
