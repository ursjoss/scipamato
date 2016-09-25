package ch.difty.sipamato.persistance.jooq.config;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.UncategorizedSQLException;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionTranslatorTest {

    private static final String STATE = "baz state";
    private static final String SQL = "select foo from bar";
    private static final int SQL_ERROR_CODE = 11;
    private static final String MSG = "some custom message";
    private static final String EXCEPTION_MSG = "jOOQ; uncategorized SQLException for SQL [select foo from bar]; SQL state [baz state]; error code [11]; some custom message; nested exception is sqlExceptionMock";

    private final ExceptionTranslator translator = new ExceptionTranslator();

    @Mock
    private ExecuteContext ctxMock;
    @Mock
    private SQLException sqlExceptionMock;

    @After
    public void tearDown() {
        verifyNoMoreInteractions(ctxMock, sqlExceptionMock);
    }

    @Test
    public void translatingException_withNullSqlException_bailsOut() {
        when(ctxMock.sqlException()).thenReturn(null);
        translator.exception(ctxMock);
        verify(ctxMock).sqlException();
    }

    @Test
    public void translatingException_withNullDialect_translatesToUncategorizedSQLException() {
        when(ctxMock.sqlException()).thenReturn(sqlExceptionMock);
        when(ctxMock.sql()).thenReturn(SQL);
        when(ctxMock.dialect()).thenReturn(null);

        when(sqlExceptionMock.getSQLState()).thenReturn(STATE);
        when(sqlExceptionMock.getErrorCode()).thenReturn(SQL_ERROR_CODE);
        when(sqlExceptionMock.getMessage()).thenReturn(MSG);

        translator.exception(ctxMock);

        verify(ctxMock, times(2)).sqlException();
        verify(ctxMock).dialect();
        verify(ctxMock).sql();
        verify(ctxMock).exception(argThat(new UncategorizedSQLExceptionMatcher()));

        verify(sqlExceptionMock, times(2)).getSQLState();
        verify(sqlExceptionMock).getErrorCode();
        verify(sqlExceptionMock).getMessage();
    }

    protected class UncategorizedSQLExceptionMatcher extends ArgumentMatcher<RuntimeException> {
        @Override
        public boolean matches(Object arg) {
            if (arg != null && arg instanceof UncategorizedSQLException) {
                UncategorizedSQLException use = (UncategorizedSQLException) arg;
                return SQL.equals(use.getSql()) && EXCEPTION_MSG.equals(use.getMessage());
            }
            return false;
        }
    }

    @Test
    public void translatingException_withPostgres95Dialect_translatesToUncategorizedSQLException() {
        when(ctxMock.sqlException()).thenReturn(sqlExceptionMock);
        when(ctxMock.sql()).thenReturn(SQL);
        when(ctxMock.dialect()).thenReturn(SQLDialect.POSTGRES_9_5);

        when(sqlExceptionMock.getSQLState()).thenReturn(STATE);
        when(sqlExceptionMock.getErrorCode()).thenReturn(SQL_ERROR_CODE);
        when(sqlExceptionMock.getMessage()).thenReturn(MSG);

        translator.exception(ctxMock);

        verify(ctxMock, times(2)).sqlException();
        verify(ctxMock).dialect();
        verify(ctxMock).sql();
        verify(ctxMock).exception(argThat(new UncategorizedSQLExceptionMatcher()));

        verify(sqlExceptionMock, times(3)).getSQLState();
        verify(sqlExceptionMock).getErrorCode();
        verify(sqlExceptionMock).getMessage();
    }

}
