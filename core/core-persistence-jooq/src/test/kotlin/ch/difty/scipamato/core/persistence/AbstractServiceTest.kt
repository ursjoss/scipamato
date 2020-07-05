package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.core.entity.IdScipamatoEntity
import ch.difty.scipamato.core.entity.User
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class AbstractServiceTest<ID : Number, T : IdScipamatoEntity<ID>, R : ReadOnlyRepository<T, ID, *>> {

    protected var userRepoMock = mockk<UserRepository>()
    private var creatorMock = mockk<User>()
    private var modifierMock = mockk<User>()

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
        every { entity.createdBy } returns CREATOR_ID
        every { entity.lastModifiedBy } returns MODIFIER_ID

        every { creatorMock.displayValue } returns "creatingUser"
        every { creatorMock.fullName } returns "creatingUserFullName"
        every { modifierMock.displayValue } returns "modifyingUser"

        every { userRepoMock.findById(CREATOR_ID) } returns creatorMock
        every { userRepoMock.findById(MODIFIER_ID) } returns modifierMock
    }

    @AfterEach
    internal fun tearDown() {
        confirmVerified(userRepoMock)
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
        verify(exactly = times) { entity.createdBy }
        verify(exactly = times) { userRepoMock.findById(CREATOR_ID) }
        verify(exactly = times) { entity.createdByName = "creatingUser" }
        verify(exactly = times) { entity.createdByFullName = "creatingUserFullName" }
        verify(exactly = times) { entity.lastModifiedBy }
        verify(exactly = times) { userRepoMock.findById(MODIFIER_ID) }
        verify(exactly = times) { entity.lastModifiedByName = "modifyingUser" }
    }

    companion object {
        const val CREATOR_ID = 10
        const val MODIFIER_ID = 20
    }
}
