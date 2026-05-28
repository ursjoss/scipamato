package ch.difty.scipamato.core.sync

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.boot.batch.jdbc.autoconfigure.BatchJdbcProperties
import org.springframework.boot.sql.init.DatabaseInitializationMode
import javax.sql.DataSource

internal class BatchConfigurationTest {

    private val batchProperties = mockk<BatchJdbcProperties> {
        every { platform } returns "platform"
        every { schema } returns "schema"
        every { initializeSchema } returns mockk<DatabaseInitializationMode>()
        every { isContinueOnError } returns true
    }
    private val dataSource = mockk<DataSource>()
    private val jooqCore = mockk<DSLContext>()

    @Test
    fun instantiating_doesNotCallServicesYet() {
        val bc = BatchConfiguration(batchProperties)
        bc.batchDataSourceInitializer(dataSource)
        verify { batchProperties.platform }
        verify { batchProperties.schema }
        verify { batchProperties.initializeSchema }
        verify { batchProperties.isContinueOnError }
        confirmVerified(batchProperties, dataSource, jooqCore)
    }
}
