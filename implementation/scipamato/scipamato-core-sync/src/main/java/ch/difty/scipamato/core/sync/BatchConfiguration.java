package ch.difty.scipamato.core.sync;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDatabaseInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
@ComponentScan(basePackages = "ch.difty.scipamato")
public class BatchConfiguration {

    private final BatchProperties properties;

    public BatchConfiguration(BatchProperties properties) {
        this.properties = properties;
    }

    @Bean
    public BatchDatabaseInitializer batchDatabaseInitializer(
        final @Qualifier("batchDataSource") DataSource dataSource, final ResourceLoader resourceLoader) {
        return new BatchDatabaseInitializer(dataSource, resourceLoader, this.properties);
    }

}
