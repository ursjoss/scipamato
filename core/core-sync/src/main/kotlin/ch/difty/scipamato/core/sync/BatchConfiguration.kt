package ch.difty.scipamato.core.sync

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
@ComponentScan(basePackages = ["ch.difty.scipamato.core"])
@Profile("!wickettest")
open class BatchConfiguration(private val properties: BatchProperties) {

    @Bean
    open fun batchDataSourceInitializer(
        @Qualifier("batchDataSource") dataSource: DataSource,
    ) = BatchDataSourceScriptDatabaseInitializer(dataSource, properties.jdbc)
}
