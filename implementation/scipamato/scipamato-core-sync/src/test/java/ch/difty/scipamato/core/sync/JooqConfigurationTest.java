package ch.difty.scipamato.core.sync;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JooqConfigurationTest {

    @Autowired
    @Qualifier("publicConfiguration")
    private Configuration jooqPublicConfig;

    @Autowired
    @Qualifier("coreConfiguration")
    private Configuration jooqCoreConfig;

    @Autowired
    @Qualifier("batchConfiguration")
    private Configuration jooqBatchConfig;

    @Test
    public void assertJooqPublicConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqPublicConfig);
    }

    @Test
    public void assertJooqCoreConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqCoreConfig);
    }

    @Test
    public void assertJooqBatchConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqBatchConfig);
    }

    private void assertConfiguration(Configuration config) throws SQLException {
        assertThat(config).isNotNull();

        assertThat(config.dialect()).isEqualTo(SQLDialect.POSTGRES);

        // assert Datasource Connection Provider
        assertThat(config.connectionProvider()).isNotNull();
        assertThat(config.connectionProvider()).isInstanceOf(DataSourceConnectionProvider.class);
        DataSourceConnectionProvider dscp = (DataSourceConnectionProvider) config.connectionProvider();
        assertThat(dscp.dataSource()).isInstanceOf(TransactionAwareDataSourceProxy.class);
        assertThat(dscp
            .dataSource()
            .isWrapperFor(HikariDataSource.class)).isTrue();

        // assert executeListenerProviders
        assertThat(config.executeListenerProviders()).hasSize(1);
        DefaultExecuteListenerProvider elp = (DefaultExecuteListenerProvider) config.executeListenerProviders()[0];
        assertThat(elp
            .provide()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator");

        // assert TransactionProvider
        assertThat(config.transactionProvider()).isNotNull();
        assertThat(config
            .transactionProvider()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider");
    }
}
