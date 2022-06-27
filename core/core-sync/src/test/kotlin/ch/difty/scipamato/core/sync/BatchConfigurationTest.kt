package ch.difty.scipamato.core.sync

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.batch.BatchProperties
import javax.sql.DataSource

internal class BatchConfigurationTest {

    private val batchProperties = mockk<BatchProperties> {
        every { jdbc } returns mockk(relaxed = true)
    }
    private val dataSource = mockk<DataSource>()
    private val jooqCore = mockk<DSLContext>()

    @Test
    fun instantiating_doesNotCallServicesYet() {
        val bc = BatchConfiguration(batchProperties)
        bc.batchDataSourceInitializer(dataSource)
        verify { batchProperties.jdbc }
        confirmVerified(batchProperties, dataSource, jooqCore)
    }
}
