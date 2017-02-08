package ch.difty.sipamato.persistance.jooq;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.sipamato.entity.IdSipamatoEntity;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.persistance.jooq.user.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractServiceTest<ID extends Number, T extends IdSipamatoEntity<ID>, R extends ReadOnlyRepository<T, ID, ?>> {

    private static final int CREATOR_ID = 10;
    private static final int MODIFIER_ID = 20;

    @Mock
    protected UserRepository userRepoMock;

    @Mock
    private User creatorMock, modifierMock;

    /**
     * @return the service specific repo mock
     */
    protected abstract R getRepo();

    /**
     * @return the service specific entity mock
     */
    protected abstract T getEntity();

    @Before
    public final void setUp() {
        specificSetUp();
        auditFixture();
    }

    /**
     * Override to set up test fixtures for mocks in the concrete test class
     */
    protected void specificSetUp() {
    }

    protected void auditFixture() {
        when(getEntity().getCreatedBy()).thenReturn(CREATOR_ID);
        when(getEntity().getLastModifiedBy()).thenReturn(MODIFIER_ID);

        when(creatorMock.getDisplayValue()).thenReturn("creatingUser");
        when(creatorMock.getFullName()).thenReturn("creatingUserFullName");
        when(modifierMock.getDisplayValue()).thenReturn("modifyingUser");

        when(userRepoMock.findById(CREATOR_ID)).thenReturn(creatorMock);
        when(userRepoMock.findById(MODIFIER_ID)).thenReturn(modifierMock);
    }

    @After
    public final void tearDown() {
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
     * @param times number of times the methods have been called.
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
