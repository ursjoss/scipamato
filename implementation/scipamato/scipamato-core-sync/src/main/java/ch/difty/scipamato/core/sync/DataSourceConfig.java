package ch.difty.scipamato.core.sync;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DataSourceConfig {

    @Value("${spring.jooq.sql-dialect}")
    private String dialect;

    @Autowired
    private PlatformTransactionManager txManager;

    /**
     * Scipamato-Public datasource.
     */
    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix = "sync.target.datasource")
    public DataSource hikariPublicDataSource() {
        return DataSourceBuilder.create()
            .build();
    }

    @Bean
    public TransactionAwareDataSourceProxy publicDataSource() {
        return new TransactionAwareDataSourceProxy(hikariPublicDataSource());
    }

    /**
     * Scipamato-Core as batch datasource. Needs to create the batch meta tables.
     */
    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix = "sync.batch.datasource")
    public DataSource hikariBatchDataSource() {
        return DataSourceBuilder.create()
            .build();
    }

    @Bean
    public TransactionAwareDataSourceProxy batchDataSource() {
        return new TransactionAwareDataSourceProxy(hikariBatchDataSource());
    }

    /**
     * Set dialect into configuration - if possible
     */
    private org.jooq.Configuration considerSettingDialect(org.jooq.Configuration configuration) {
        if (dialect != null)
            return configuration.derive(SQLDialect.valueOf(dialect));
        else
            return configuration;
    }

    /**
     * @return {@link org.jooq.Configuration} for scipamato-public
     */
    @Bean
    public org.jooq.Configuration publicConfiguration() {
        return considerSettingDialect(new DefaultConfiguration().derive(publicDataSource()))
            .derive(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
            .derive(new SpringTransactionProvider(txManager));
    }

    /**
     * @return {@link DSLContext} for scipamato-public
     */
    @Bean
    public DSLContext publicDslContext() {
        return DSL.using(publicConfiguration());
    }

    /**
     * @return {@link org.jooq.Configuration} for spring batch
     */
    @Bean
    public org.jooq.Configuration batchConfiguration() {
        return considerSettingDialect(new DefaultConfiguration().derive(batchDataSource()))
            .derive(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
            .derive(new SpringTransactionProvider(txManager));
    }

    /**
     * @return {@link DSLContext} for batch on scipamato-core
     */
    @Bean
    public DSLContext batchDslContext() {
        return DSL.using(batchConfiguration());
    }

}
