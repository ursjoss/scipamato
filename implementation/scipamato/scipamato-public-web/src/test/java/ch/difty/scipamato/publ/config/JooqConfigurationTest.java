package ch.difty.scipamato.publ.config;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JooqConfigurationTest {

    @Autowired
    private Configuration jooqConfig;

    @Test
    public void assertJooqConfigIsProperlyWired() throws SQLException {
        assertThat(jooqConfig).isNotNull();

        assertThat(jooqConfig.dialect()).isEqualTo(SQLDialect.POSTGRES);

        // assert Datasource Connection Provider
        assertThat(jooqConfig.connectionProvider()).isNotNull();
        assertThat(jooqConfig.connectionProvider()).isInstanceOf(DataSourceConnectionProvider.class);
        DataSourceConnectionProvider dscp = (DataSourceConnectionProvider) jooqConfig.connectionProvider();
        assertThat(dscp.dataSource()).isInstanceOf(TransactionAwareDataSourceProxy.class);
        assertThat(dscp
            .dataSource()
            .isWrapperFor(HikariDataSource.class)).isTrue();

        // assert executeListenerProviders
        assertThat(jooqConfig.executeListenerProviders()).hasSize(1);
        DefaultExecuteListenerProvider elp = (DefaultExecuteListenerProvider) jooqConfig.executeListenerProviders()[0];
        assertThat(elp
            .provide()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator");

        // assert TransactionProvider
        assertThat(jooqConfig.transactionProvider()).isNotNull();
        assertThat(jooqConfig
            .transactionProvider()
            .getClass()
            .getName()).isEqualTo("org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider");

    }
}
