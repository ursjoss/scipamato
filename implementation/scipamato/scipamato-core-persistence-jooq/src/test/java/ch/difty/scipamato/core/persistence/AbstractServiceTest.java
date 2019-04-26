package ch.difty.scipamato.core.persistence;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.entity.IdScipamatoEntity;
import ch.difty.scipamato.core.entity.User;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "ResultOfMethodCallIgnored" })
public abstract class AbstractServiceTest<ID extends Number, T extends IdScipamatoEntity<ID>, R extends ReadOnlyRepository<T, ID, ?>> {

    protected static final int CREATOR_ID  = 10;
    protected static final int MODIFIER_ID = 20;

    @Mock
    protected UserRepository userRepoMock;

    @Mock
    protected User creatorMock;
    @Mock
    protected User modifierMock;

    /**
     * @return the service specific repo mock
     */
    protected abstract R getRepo();

    /**
     * @return the service specific entity mock
     */
    protected abstract T getEntity();

    @BeforeEach
    final void setUp() {
        specificSetUp();
    }

    /**
     * Override to set up test fixtures for mocks in the concrete test class
     */
    protected void specificSetUp() {
    }

    protected void auditFixture() {
        doReturn(CREATOR_ID)
            .when(getEntity())
            .getCreatedBy();
        doReturn(MODIFIER_ID)
            .when(getEntity())
            .getLastModifiedBy();

        doReturn("creatingUser")
            .when(creatorMock)
            .getDisplayValue();
        doReturn("creatingUserFullName")
            .when(creatorMock)
            .getFullName();
        doReturn("modifyingUser")
            .when(modifierMock)
            .getDisplayValue();

        doReturn(creatorMock)
            .when(userRepoMock)
            .findById(CREATOR_ID);
        doReturn(modifierMock)
            .when(userRepoMock)
            .findById(MODIFIER_ID);
    }

    @AfterEach
    final void tearDown() {
        verifyNoMoreInteractions(userRepoMock);
        specificTearDown();
    }

    /**
     * Override to verify mocks in the specific test class
     */
    protected void specificTearDown() {
    }

    /**
     * Call this method to verify the audit names have been set.
     *
     * @param times
     *     number of times the methods have been called.
     */
    protected void verifyAudit(final int times) {
        verify(getEntity(), times(times)).getCreatedBy();
        verify(userRepoMock, times(times)).findById(CREATOR_ID);
        verify(getEntity(), times(times)).setCreatedByName("creatingUser");
        verify(getEntity(), times(times)).setCreatedByFullName("creatingUserFullName");
        verify(getEntity(), times(times)).getLastModifiedBy();
        verify(userRepoMock, times(times)).findById(MODIFIER_ID);
        verify(getEntity(), times(times)).setLastModifiedByName("modifyingUser");
    }

}