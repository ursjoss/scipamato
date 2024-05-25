package ch.difty.scipamato.core.sync.jobs;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@SpringBootTest
class JooqConfigurationTest {

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
    void assertJooqPublicConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqPublicConfig);
    }

    @Test
    void assertJooqCoreConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqCoreConfig);
    }

    @Test
    void assertJooqBatchConfigIsProperlyWired() throws SQLException {
        assertConfiguration(jooqBatchConfig);
    }

    private void assertConfiguration(Configuration config) throws SQLException {
        config.shouldNotBeNull();

        config.dialect() shouldBeEqualTo SQLDialect.POSTGRES;

        // assert Datasource Connection Provider
        config.connectionProvider().shouldNotBeNull();
        assertThat(config.connectionProvider()).isInstanceOf(DataSourceConnectionProvider.class);
        DataSourceConnectionProvider dscp = (DataSourceConnectionProvider) config.connectionProvider();
        assertThat(dscp.dataSource()).isInstanceOf(TransactionAwareDataSourceProxy.class);
        assertThat(dscp
            .dataSource()
            .isWrapperFor(HikariDataSource.class)).isTrue();

        // assert executeListenerProviders
        config.executeListenerProviders() shouldHaveSize 2;
        DefaultExecuteListenerProvider elp = (DefaultExecuteListenerProvider) config.executeListenerProviders()[0];
        assertThat(elp
            .provide()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.ExceptionTranslatorExecuteListener");

        // assert TransactionProvider
        config.transactionProvider().shouldNotBeNull();
        assertThat(config
            .transactionProvider()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider");
    }
}
