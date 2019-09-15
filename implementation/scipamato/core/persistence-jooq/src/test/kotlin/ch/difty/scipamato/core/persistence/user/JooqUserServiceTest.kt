package ch.difty.scipamato.core.persistence.user

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.entity.search.UserFilter
import ch.difty.scipamato.core.persistence.UserRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

internal class JooqUserServiceTest {

    private val repoMock = mock<UserRepository>()
    private val passwordEncoderMock = mock<PasswordEncoder>()
    private val filterMock = mock<UserFilter>()
    private val paginationContextMock = mock<PaginationContext>()
    private val userMock = mock<User>()

    private var service = JooqUserService(repoMock, passwordEncoderMock)

    private val users = listOf(userMock, userMock)

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(repoMock, passwordEncoderMock, filterMock, paginationContextMock, userMock)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7
        whenever(repoMock.findById(id)).thenReturn(userMock)

        val optUser = service.findById(id)
        assertThat(optUser.isPresent).isTrue()
        assertThat(optUser.get()).isEqualTo(userMock)

        verify(repoMock).findById(id)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7
        whenever(repoMock.findById(id)).thenReturn(null)
        assertThat(service.findById(id).isPresent).isFalse()
        verify(repoMock).findById(id)
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        whenever(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(users)
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(users)
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        whenever(repoMock.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repoMock).countByFilter(filterMock)
    }

    @Test
    fun savingOrUpdating_withNullUser_simplyReturnsNull() {
        assertThat(service.saveOrUpdate(null)).isNull()
    }

    @Test
    fun savingOrUpdating_withUserWithNullId_hasRepoAddTheUser() {
        whenever(userMock.id).thenReturn(null)
        whenever(repoMock.add(userMock)).thenReturn(userMock)
        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock)
        verify(repoMock).add(userMock)
        verify(userMock).id
        verify(userMock).password
        verify(userMock, never()).password = anyString()
    }

    @Test
    fun savingOrUpdating_withUserWithNonNullId_hasRepoUpdateTheUser() {
        whenever(userMock.id).thenReturn(17)
        whenever(repoMock.update(userMock)).thenReturn(userMock)
        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock)
        verify(repoMock).update(userMock)
        verify(userMock).id
        verify(userMock).password
        verify(userMock, never()).password = anyString()
    }

    @Test
    fun savingOrUpdating_withUserWithNullId_withPassword_hasRepoAddTheUserAfterEncodingThePassword() {
        whenever(userMock.id).thenReturn(null)
        whenever(userMock.password).thenReturn("foo")
        whenever(passwordEncoderMock.encode("foo")).thenReturn("bar")
        whenever(repoMock.add(userMock)).thenReturn(userMock)

        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock)

        verify(repoMock).add(userMock)
        verify(passwordEncoderMock).encode("foo")
        verify(userMock).id
        verify(userMock).password
        verify(userMock).password = "bar"
    }

    @Test
    fun savingOrUpdating_withUserWithNonNullId_withPassword_hasRepoUpdateTheUserAfterEncodingThePassword() {
        whenever(userMock.id).thenReturn(17)
        whenever(userMock.password).thenReturn("foo")
        whenever(passwordEncoderMock.encode("foo")).thenReturn("bar")
        whenever(repoMock.update(userMock)).thenReturn(userMock)

        assertThat(service.saveOrUpdate(userMock)).isEqualTo(userMock)

        verify(repoMock).update(userMock)
        verify(passwordEncoderMock).encode("foo")
        verify(userMock).id
        verify(userMock).password
        verify(userMock).password = "bar"
    }

    @Test
    fun findingByUserName_withNullName_returnsEmptyOptional() {
        assertThat(service.findByUserName(null).isPresent).isFalse()
    }

    @Test
    fun findingByUserName_whenFindingUser_delegatesToRepoAndReturnsOptionalOfFoundUser() {
        whenever(repoMock.findByUserName("foo")).thenReturn(userMock)
        assertThat(service.findByUserName("foo")).isEqualTo(Optional.of(userMock))
        verify(repoMock).findByUserName("foo")
    }

    @Test
    fun findingByUserName_whenNotFindingUser_delegatesToRepoAndReturnsOptionalEmpty() {
        whenever(repoMock.findByUserName("foo")).thenReturn(null)
        assertThat(service.findByUserName("foo")).isEqualTo(Optional.empty<Any>())
        verify(repoMock).findByUserName("foo")
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify<UserRepository>(repoMock, never()).delete(anyInt(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        whenever(userMock.id).thenReturn(null)

        service.remove(userMock)

        verify(userMock).id
        verify<UserRepository>(repoMock, never()).delete(anyInt(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        whenever(userMock.id).thenReturn(3)
        whenever(userMock.version).thenReturn(2)

        service.remove(userMock)

        verify(userMock, times(2)).id
        verify(userMock, times(1)).version
        verify<UserRepository>(repoMock, times(1)).delete(3, 2)
    }

    @Test
    fun findingPageOfIdsByFilter_delegatesToRepo() {
        whenever(repoMock.findPageOfIdsByFilter(filterMock, paginationContextMock)).thenReturn(listOf(3, 8, 5))
        assertThat(service.findPageOfIdsByFilter(filterMock, paginationContextMock)).containsExactly(3, 8, 5)
        verify(repoMock).findPageOfIdsByFilter(filterMock, paginationContextMock)
    }

}
