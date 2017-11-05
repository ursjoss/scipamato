package ch.difty.scipamato.persistence.config;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.transaction.TransactionDefinition.*;

import org.jooq.TransactionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import ch.difty.scipamato.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class SpringTransactionProviderTest {

    private SpringTransactionProvider tp;

    // important methods like rollback etc. will still be called and not mocked!!!
    @Mock
    private DataSourceTransactionManager txMgrMock = new DataSourceTransactionManager();

    @Mock
    private TransactionContext ctxMock;

    @Mock
    private TransactionStatus txMock;

    private SpringTransaction transaction;

    private final DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(PROPAGATION_NESTED);

    @Before
    public void setUp() {
        tp = new SpringTransactionProvider(txMgrMock);

        transaction = new SpringTransaction(txMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(txMgrMock, ctxMock, txMock);
    }

    @Test
    public void nullCheck() {
        assertThat(tp).isNotNull();
        assertThat(tp.getTxMgr()).isNotNull();

        assertThat(ctxMock).isNotNull();
    }

    @Test(expected = NullArgumentException.class)
    public void degenerateConstruction() {
        new SpringTransactionProvider(null);
    }

    @Test
    public void constructingSpringTransaction_storesProvidedTx() {
        assertThat(transaction.tx).isEqualTo(txMock);
    }

    // Bug??? http://stackoverflow.com/questions/38669278/after-upgrading-to-spring-boot-1-4-0-nullpointerexception-in-abstractplatformtr
    public void beginning() {
        when(txMgrMock.getTransaction(transactionDefinition)).thenReturn(txMock);

        tp.begin(ctxMock);

        verify(txMgrMock).getTransaction(transactionDefinition);
        verify(ctxMock).transaction(transaction);
    }

    @Test
    public void committing_forAlreadyClosedTransaction_throws() {
        when(ctxMock.transaction()).thenReturn(transaction);
        when(txMock.isCompleted()).thenReturn(true);

        assertThat(tp.getTxMgr()).isEqualTo(txMgrMock);

        try {
            tp.commit(ctxMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalTransactionStateException.class);
        }

        verify(ctxMock).transaction();
        verify(txMock).isCompleted();
    }

    @Test
    public void committing() {
        DefaultTransactionStatus dtxMock = mock(DefaultTransactionStatus.class);
        transaction = new SpringTransaction(dtxMock);

        when(ctxMock.transaction()).thenReturn(transaction);
        when(dtxMock.isCompleted()).thenReturn(false);
        when(dtxMock.isDebug()).thenReturn(false);
        when(dtxMock.isLocalRollbackOnly()).thenReturn(true);
        when(dtxMock.hasSavepoint()).thenReturn(true);

        assertThat(tp.getTxMgr()).isEqualTo(txMgrMock);

        tp.commit(ctxMock);

        verify(ctxMock).transaction();
        verify(dtxMock).isCompleted();
    }

    @Test
    public void rollingback_forAlreadyClosedTransaction_throws() {
        when(ctxMock.transaction()).thenReturn(transaction);
        when(txMock.isCompleted()).thenReturn(true);

        assertThat(tp.getTxMgr()).isEqualTo(txMgrMock);

        try {
            tp.rollback(ctxMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalTransactionStateException.class);
        }

        verify(ctxMock).transaction();
        verify(txMock).isCompleted();
    }

    @Test
    public void rollingback() {
        DefaultTransactionStatus dtxMock = mock(DefaultTransactionStatus.class);
        transaction = new SpringTransaction(dtxMock);

        when(ctxMock.transaction()).thenReturn(transaction);
        when(dtxMock.isCompleted()).thenReturn(false);
        when(dtxMock.isDebug()).thenReturn(false);
        when(dtxMock.hasSavepoint()).thenReturn(true);

        assertThat(tp.getTxMgr()).isEqualTo(txMgrMock);

        tp.rollback(ctxMock);

        verify(ctxMock).transaction();
        verify(dtxMock).isCompleted();
    }
}
