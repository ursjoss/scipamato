package ch.difty.scipamato.core.sync;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
@ComponentScan(basePackages = "ch.difty.scipamato")
public class BatchConfiguration {

    // TODO currently uses the wrong datasource. Should use batchDataSource with the
    // admin permissions but uses the sourceDataSource.
}
