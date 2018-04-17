package ch.difty.scipamato.core.web.config;

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
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class WebDataSourceConfig {

    @Value("${spring.jooq.sql-dialect}")
    private String dialect;

    @Autowired
    @Lazy
    private PlatformTransactionManager txManager;

    /**
     * Scipamato-Core as primary datasource
     */
    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix = "sync.source.datasource")
    public DataSource hikariCoreDataSource() {
        return DataSourceBuilder
            .create()
            .build();
    }

    @Bean
    public TransactionAwareDataSourceProxy dataSource() {
        return new TransactionAwareDataSourceProxy(hikariCoreDataSource());
    }

    /**
     * @return {@link org.jooq.Configuration} for scipamato-core
     */
    @Bean
    public org.jooq.Configuration configuration() {
        return considerSettingDialect(new DefaultConfiguration().derive(dataSource()))
            .derive(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
            .derive(new SpringTransactionProvider(txManager));
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
     * @return {@link DSLContext} for scipamato-core
     */
    @Bean
    public DSLContext dslContext() {
        return DSL.using(configuration());
    }

}
