package ch.difty.scipamato.core.sync;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.ExecuteListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import ch.difty.scipamato.core.sync.launcher.UnsynchronizedEntitiesWarner;
import ch.difty.scipamato.core.sync.launcher.Warner;

@Configuration
@EnableConfigurationProperties({ JooqProperties.class })
@Profile("!wickettest")
public class DataSourceConfig {

    private final JooqProperties jooqProperties;

    public DataSourceConfig(@NotNull JooqProperties jooqProperties) {
        this.jooqProperties = jooqProperties;
    }

    @NotNull
    @Bean
    public Settings settings() {
        return new DefaultConfiguration()
            .settings()
            .withExecuteWithOptimisticLocking(true);
    }

    @NotNull
    @Bean
    public ExecuteListenerProvider executeListenerProvider() {
        return new DefaultExecuteListenerProvider(new JooqExceptionTranslator());
    }

    /**
     * @return Scipamato-Core datasource.
     */
    @NotNull
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.source.datasource")
    public DataSource hikariCoreDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @NotNull
    @Bean(destroyMethod = "")
    @Primary
    public TransactionAwareDataSourceProxy dataSource() {
        return new TransactionAwareDataSourceProxy(hikariCoreDataSource());
    }

    @NotNull
    @Bean
    @Primary
    public PlatformTransactionManager coreTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @NotNull
    @Bean
    @Primary
    public SpringTransactionProvider coreTransactionProvider() {
        return new SpringTransactionProvider(coreTransactionManager());
    }

    /**
     * @return {@link org.jooq.Configuration} for scipamato-core
     */
    @NotNull
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
    @NotNull
    @Bean
    @Primary
    public DSLContext dslContext() {
        return DSL.using(coreConfiguration());
    }

    /**
     * @return Scipamato-Public datasource.
     */
    @NotNull
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.target.datasource")
    public DataSource hikariPublicDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @NotNull
    @Bean(destroyMethod = "")
    public TransactionAwareDataSourceProxy publicDataSource() {
        return new TransactionAwareDataSourceProxy(hikariPublicDataSource());
    }

    @NotNull
    @Bean
    public PlatformTransactionManager publicTransactionManager() {
        return new DataSourceTransactionManager(publicDataSource());
    }

    @NotNull
    @Bean
    public SpringTransactionProvider publicTransactionProvider() {
        return new SpringTransactionProvider(publicTransactionManager());
    }

    /**
     * @return {@link org.jooq.Configuration} for scipamato-public
     */
    @NotNull
    @Bean
    public org.jooq.Configuration publicConfiguration() {
        return newJooqConfiguration(publicDataSource(), publicTransactionProvider());
    }

    /**
     * @return {@link DSLContext} for scipamato-public
     */
    @NotNull
    @Bean
    public DSLContext publicDslContext() {
        return DSL.using(publicConfiguration());
    }

    /**
     * @return Scipamato-Core as batch datasource. Needs to create the batch meta tables.
     */
    @NotNull
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.batch.datasource")
    public DataSource hikariBatchDataSource() {
        return DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .build();
    }

    @NotNull
    @Bean
    public TransactionAwareDataSourceProxy batchDataSource() {
        return new TransactionAwareDataSourceProxy(hikariBatchDataSource());
    }

    @NotNull
    @Bean
    public PlatformTransactionManager batchTransactionManager() {
        return new DataSourceTransactionManager(batchDataSource());
    }

    @NotNull
    @Bean
    public SpringTransactionProvider batchTransactionProvider() {
        return new SpringTransactionProvider(batchTransactionManager());
    }

    /**
     * @return {@link org.jooq.Configuration} for spring batch
     */
    @NotNull
    @Bean
    public org.jooq.Configuration batchConfiguration() {
        return newJooqConfiguration(batchDataSource(), batchTransactionProvider());
    }

    /**
     * @return {@link DSLContext} for batch on scipamato-core
     */
    @NotNull
    @Bean
    public DSLContext batchDslContext() {
        return DSL.using(batchConfiguration());
    }

    @NotNull
    @Bean
    public Warner unsynchronizedEntitiesWarner(@Qualifier("dslContext") DSLContext jooqCore) {
        return new UnsynchronizedEntitiesWarner(jooqCore);
    }
}
