package ch.difty.scipamato.core.sync

import com.nhaarman.mockitokotlin2.mock
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.core.io.ResourceLoader
import javax.sql.DataSource

internal class BatchConfigurationTest {

    private val batchProperties = mock<BatchProperties>()
    private val dataSource = mock<DataSource>()
    private val resourceLoader = mock<ResourceLoader>()
    private val jooqCore = mock<DSLContext>()

    @Test
    fun instantiating_doesNotCallServicesYet() {
        val bc = BatchConfiguration(batchProperties)
        bc.batchDataSourceInitializer(dataSource, resourceLoader)
        verifyNoMoreInteractions(batchProperties, dataSource, resourceLoader, jooqCore)
    }
}
