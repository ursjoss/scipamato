package ch.difty.scipamato.core.sync

import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
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
    fun instantiate() {
        val bc = BatchConfiguration(batchProperties)
        assertThat(bc.batchDataSourceInitializer(dataSource, resourceLoader) == null).isFalse()
        verifyNoMoreInteractions(batchProperties, dataSource, resourceLoader, jooqCore)
    }
}
