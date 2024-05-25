package ch.difty.scipamato.core.sync;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import ch.difty.scipamato.core.sync.launcher.Warner;

@SpringBootTest
class DataSourceConfigIntegrationTest {

    @Autowired
    private Configuration coreConfiguration;

    @Autowired
    private Configuration publicConfiguration;

    @Autowired
    private Configuration batchConfiguration;

    @Autowired
    private Warner warner;

    @Test
    void assertJooqSourceConfigIsProperlyWired() throws SQLException {
        assertConfiguration(coreConfiguration);
    }

    @Test
    void assertJooqTargetConfigIsProperlyWired() throws SQLException {
        assertConfiguration(publicConfiguration);
    }

    @Test
    void assertJooqBatchConfigIsProperlyWired() throws SQLException {
        assertConfiguration(batchConfiguration);
    }

    private void assertConfiguration(Configuration config) throws SQLException {
        config.shouldNotBeNull();

        config.dialect() shouldBeEqualTo SQLDialect.POSTGRES;
        assertThat(config
            .settings()
            .isExecuteWithOptimisticLocking()).isTrue();

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

    @Test
    void assertWarner() {
        warner.shouldNotBeNull();
    }

}
