package ch.difty.scipamato.core.sync;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties({ JooqProperties.class })
public class DataSourceConfig {

    private final JooqProperties jooqProperties;

    public DataSourceConfig(JooqProperties jooqProperties) {
        this.jooqProperties = jooqProperties;
    }

    @Bean
    public Settings settings() {
        return new DefaultConfiguration()
            .settings()
            .withExecuteWithOptimisticLocking(true);
    }

    @Bean
    public ExecuteListenerProvider executeListenerProvider() {
        return new DefaultExecuteListenerProvider(new JooqExceptionTranslator());
    }

    /**
     * @return Scipamato-Core datasource.
     */
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.source.datasource")
    public DataSource hikariCoreDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(destroyMethod = "")
    @Primary
    public TransactionAwareDataSourceProxy dataSource() {
        return new TransactionAwareDataSourceProxy(hikariCoreDataSource());
    }

    @Bean
    @Primary
    public PlatformTransactionManager coreTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    @Primary
    public SpringTransactionProvider coreTransactionProvider() {
        return new SpringTransactionProvider(coreTransactionManager());
    }

    /**
     * @return {@link org.jooq.Configuration} for scipamato-core
     */
    @Bean
    @Primary
    public org.jooq.Configuration coreConfiguration() {
        return newJooqConfiguration(dataSource(), coreTransactionProvider());
    }

    private org.jooq.Configuration newJooqConfiguration(final TransactionAwareDataSourceProxy datasource,
        final SpringTransactionProvider transactionProvider) {
        return new DefaultConfiguration()
            .derive(datasource)
            .derive(executeListenerProvider())
            .derive(transactionProvider)
            .set(jooqProperties.determineSqlDialect(datasource))
            .set(settings());
    }

    /**
     * @return {@link DSLContext} for scipamato-core
     */
    @Bean
    @Primary
    public DSLContext dslContext() {
        return DSL.using(coreConfiguration());
    }

    /**
     * @return Scipamato-Public datasource.
     */
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.target.datasource")
    public DataSource hikariPublicDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean(destroyMethod = "")
    public TransactionAwareDataSourceProxy publicDataSource() {
        return new TransactionAwareDataSourceProxy(hikariPublicDataSource());
    }

    @Bean
    public PlatformTransactionManager publicTransactionManager() {
        return new DataSourceTransactionManager(publicDataSource());
    }

    @Bean
    public SpringTransactionProvider publicTransactionProvider() {
        return new SpringTransactionProvider(publicTransactionManager());
    }

    /**
     * @return {@link org.jooq.Configuration} for scipamato-public
     */
    @Bean
    public org.jooq.Configuration publicConfiguration() {
        return newJooqConfiguration(publicDataSource(), publicTransactionProvider());
    }

    /**
     * @return {@link DSLContext} for scipamato-public
     */
    @Bean
    public DSLContext publicDslContext() {
        return DSL.using(publicConfiguration());
    }

    /**
     * @return Scipamato-Core as batch datasource. Needs to create the batch meta tables.
     */
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.batch.datasource")
    public DataSource hikariBatchDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    public TransactionAwareDataSourceProxy batchDataSource() {
        return new TransactionAwareDataSourceProxy(hikariBatchDataSource());
    }

    @Bean
    public PlatformTransactionManager batchTransactionManager() {
        return new DataSourceTransactionManager(batchDataSource());
    }

    @Bean
    public SpringTransactionProvider batchTransactionProvider() {
        return new SpringTransactionProvider(batchTransactionManager());
    }

    /**
     * @return {@link org.jooq.Configuration} for spring batch
     */
    @Bean
    public org.jooq.Configuration batchConfiguration() {
        return newJooqConfiguration(batchDataSource(), batchTransactionProvider());
    }

    /**
     * @return {@link DSLContext} for batch on scipamato-core
     */
    @Bean
    public DSLContext batchDslContext() {
        return DSL.using(batchConfiguration());
    }

}
