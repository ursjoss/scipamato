package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.IdScipamatoEntity
import ch.difty.scipamato.core.entity.User
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*

abstract class AbstractServiceTest<ID : Number, T : IdScipamatoEntity<ID>, R : ReadOnlyRepository<T, ID, *>> {

    protected var userRepoMock = mock<UserRepository>()
    protected var creatorMock = mock<User>()
    protected var modifierMock = mock<User>()

    /**
     * @return the service specific repo mock
     */
    protected abstract val repo: R

    /**
     * @return the service specific entity mock
     */
    protected abstract val entity: T

    @BeforeEach
    internal fun setUp() {
        specificSetUp()
    }

    /**
     * Override to set up test fixtures for mocks in the concrete test class
     */
    protected open fun specificSetUp() {}

    protected fun auditFixture() {
        doReturn(CREATOR_ID).whenever(entity).createdBy
        doReturn(MODIFIER_ID).whenever(entity).lastModifiedBy

        doReturn("creatingUser").whenever(creatorMock).displayValue
        doReturn("creatingUserFullName").whenever(creatorMock).fullName
        doReturn("modifyingUser").whenever(modifierMock).displayValue

        doReturn(creatorMock).whenever(userRepoMock).findById(CREATOR_ID)
        doReturn(modifierMock).whenever(userRepoMock).findById(MODIFIER_ID)
    }

    @AfterEach
    internal fun tearDown() {
        verifyNoMoreInteractions(userRepoMock)
        specificTearDown()
    }

    /**
     * Override to verify mocks in the specific test class
     */
    protected open fun specificTearDown() {}

    /**
     * Call this method to verify the audit names have been set.
     *
     * @param times
     * number of times the methods have been called.
     */
    protected fun verifyAudit(times: Int) {
        verify(entity, times(times)).createdBy
        verify<UserRepository>(userRepoMock, times(times)).findById(CREATOR_ID)
        verify(entity, times(times)).createdByName = "creatingUser"
        verify(entity, times(times)).createdByFullName = "creatingUserFullName"
        verify(entity, times(times)).lastModifiedBy
        verify<UserRepository>(userRepoMock, times(times)).findById(MODIFIER_ID)
        verify(entity, times(times)).lastModifiedByName = "modifyingUser"
    }

    companion object {
        const val CREATOR_ID = 10
        const val MODIFIER_ID = 20
    }

}