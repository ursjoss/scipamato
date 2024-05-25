package ch.difty.scipamato.core.sync

import ch.difty.scipamato.core.sync.launcher.UnsynchronizedEntitiesWarner
import ch.difty.scipamato.core.sync.launcher.Warner
import com.zaxxer.hikari.HikariDataSource
import org.jooq.DSLContext
import org.jooq.ExecuteListenerProvider
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jooq.ExceptionTranslatorExecuteListener
import org.springframework.boot.autoconfigure.jooq.JooqProperties
import org.springframework.boot.autoconfigure.jooq.SpringTransactionProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(JooqProperties::class)
@Profile("!wickettest")
@Suppress("TooManyFunctions")
open class DataSourceConfig(
    private val jooqProperties: JooqProperties
) {

    @Bean
    open fun settings(): Settings =
        DefaultConfiguration()
            .settings()
            .withExecuteWithOptimisticLocking(true)

    @Bean
    open fun executeListenerProvider(): ExecuteListenerProvider =
        DefaultExecuteListenerProvider(ExceptionTranslatorExecuteListener.DEFAULT)

    /**
     * @return Scipamato-Core datasource.
     */
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.source.datasource")
    open fun hikariCoreDataSource(): DataSource =
        DataSourceBuilder
            .create()
            .type(HikariDataSource::class.java)
            .build()

    @Bean(destroyMethod = "")
    @Primary
    open fun dataSource(): TransactionAwareDataSourceProxy =
        TransactionAwareDataSourceProxy(hikariCoreDataSource())

    @Bean
    @Primary
    open fun coreTransactionManager(): PlatformTransactionManager =
        DataSourceTransactionManager(dataSource())

    @Bean
    @Primary
    open fun coreTransactionProvider(): SpringTransactionProvider =
        SpringTransactionProvider(coreTransactionManager())

    /**
     * @return [org.jooq.Configuration] for scipamato-core
     */
    @Bean
    @Primary
    open fun coreConfiguration(): org.jooq.Configuration =
        newJooqConfiguration(dataSource(), coreTransactionProvider())

    private fun newJooqConfiguration(
        datasource: TransactionAwareDataSourceProxy,
        transactionProvider: SpringTransactionProvider
    ): org.jooq.Configuration = DefaultConfiguration()
        .derive(datasource)
        .derive(executeListenerProvider())
        .derive(transactionProvider)
        .set(jooqProperties.determineSqlDialect(datasource))
        .set(settings())

    /**
     * @return [DSLContext] for scipamato-core
     */
    @Bean
    @Primary
    open fun dslContext(): DSLContext =
        DSL.using(coreConfiguration())

    /**
     * @return Scipamato-Public datasource.
     */
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.target.datasource")
    open fun hikariPublicDataSource(): DataSource =
        DataSourceBuilder
            .create()
            .type(HikariDataSource::class.java)
            .build()

    @Bean(destroyMethod = "")
    open fun publicDataSource(): TransactionAwareDataSourceProxy =
        TransactionAwareDataSourceProxy(hikariPublicDataSource())

    @Bean
    open fun publicTransactionManager(): PlatformTransactionManager =
        DataSourceTransactionManager(publicDataSource())

    @Bean
    open fun publicTransactionProvider(): SpringTransactionProvider =
        SpringTransactionProvider(publicTransactionManager())

    /**
     * @return [org.jooq.Configuration] for scipamato-public
     */
    @Bean
    open fun publicConfiguration(): org.jooq.Configuration {
        return newJooqConfiguration(publicDataSource(), publicTransactionProvider())
    }

    /**
     * @return [DSLContext] for scipamato-public
     */
    @Bean
    open fun publicDslContext(): DSLContext = DSL.using(publicConfiguration())

    /**
     * @return Scipamato-Core as batch datasource. Needs to create the batch meta tables.
     */
    @Bean(destroyMethod = "")
    @ConfigurationProperties(prefix = "sync.batch.datasource")
    open fun hikariBatchDataSource(): DataSource = DataSourceBuilder
        .create()
        .type(HikariDataSource::class.java)
        .build()

    @Bean
    open fun batchDataSource(): TransactionAwareDataSourceProxy =
        TransactionAwareDataSourceProxy(hikariBatchDataSource())

    @Bean
    open fun batchTransactionManager(): PlatformTransactionManager =
        DataSourceTransactionManager(batchDataSource())

    @Bean
    open fun batchTransactionProvider(): SpringTransactionProvider =
        SpringTransactionProvider(batchTransactionManager())

    /**
     * @return [org.jooq.Configuration] for spring batch
     */
    @Bean
    open fun batchConfiguration(): org.jooq.Configuration =
        newJooqConfiguration(batchDataSource(), batchTransactionProvider())

    /**
     * @return [DSLContext] for batch on scipamato-core
     */
    @Bean
    open fun batchDslContext(): DSLContext =
        DSL.using(batchConfiguration())

    @Bean
    open fun unsynchronizedEntitiesWarner(@Qualifier("dslContext") jooqCore: DSLContext): Warner =
        UnsynchronizedEntitiesWarner(jooqCore)
}
