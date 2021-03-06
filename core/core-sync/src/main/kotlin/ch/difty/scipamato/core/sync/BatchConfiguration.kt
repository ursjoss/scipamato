package ch.difty.scipamato.core.sync

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.BatchDataSourceInitializer
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ResourceLoader
import javax.sql.DataSource

@Configuration
@ComponentScan(basePackageClasses = [DefaultBatchConfigurer::class], basePackages = ["ch.difty.scipamato.core"])
@Profile("!wickettest")
open class BatchConfiguration(private val properties: BatchProperties) {

    @Bean
    open fun batchDataSourceInitializer(
        @Qualifier("batchDataSource") dataSource: DataSource,
        resourceLoader: ResourceLoader
    ): BatchDataSourceInitializer = BatchDataSourceInitializer(dataSource, resourceLoader, properties)
}
