package ch.difty.scipamato.core.sync;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Value("${spring.jooq.sql-dialect}")
    private String dialect;

    /**
     * Scipamato-Public datasource.
     */
    @Bean(destroyMethod = "close")
    @Qualifier("scipamatoPublicDataSource")
    @ConfigurationProperties(prefix = "sync.target.datasource")
    public DataSource scipamatoPublicDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Scipamato-Core as batch datasource. Needs to create
     * the batch meta tables.
     */
    @Bean(destroyMethod = "close")
    @Qualifier("batchDataSource")
    @ConfigurationProperties(prefix = "sync.batch.datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
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
        return considerSettingDialect(new DefaultConfiguration().derive(scipamatoPublicDataSource()));
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
        return considerSettingDialect(new DefaultConfiguration().derive(batchDataSource()));
    }

    /**
     * @return {@link DSLContext} for batch on scipamato-core
     */
    @Bean
    public DSLContext batchDslContext() {
        return DSL.using(batchConfiguration());
    }

}
