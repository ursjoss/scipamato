package ch.difty.sipamato.persistance.jooq.config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;

import org.jooq.TransactionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import ch.difty.sipamato.lib.NullArgumentException;

@RunWith(MockitoJUnitRunner.class)
public class SpringTransactionProviderTest {

    private SpringTransactionProvider tp;

    @Mock
    private DataSourceTransactionManager txMgrMock;

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
        Mockito.verifyNoMoreInteractions(txMgrMock, ctxMock);
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

    @Test
    @Ignore
    // Bug??? http://stackoverflow.com/questions/38669278/after-upgrading-to-spring-boot-1-4-0-nullpointerexception-in-abstractplatformtr
    public void beginning() {
        when(txMgrMock.getTransaction(transactionDefinition)).thenReturn(txMock);

        tp.begin(ctxMock);

        verify(txMgrMock).getTransaction(transactionDefinition);
        verify(ctxMock).transaction(transaction);
    }

    @Test
    @Ignore // TODO 
    public void rollback() {

    }
}
