package ch.difty.scipamato.core.sync;

import javax.sql.DataSource;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;

@Configuration
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
@ComponentScan(basePackages = "ch.difty.scipamato.core")
@Profile("!wickettest")
public class BatchConfiguration {

    private final BatchProperties properties;

    public BatchConfiguration(@NotNull BatchProperties properties) {
        this.properties = properties;
    }

    @NotNull
    @Bean
    public BatchDataSourceInitializer batchDataSourceInitializer(
        final @Qualifier("batchDataSource") DataSource dataSource, final ResourceLoader resourceLoader) {
        return new BatchDataSourceInitializer(dataSource, resourceLoader, this.properties);
    }
}