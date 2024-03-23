package ch.difty.scipamato.core.sync.jobs

import io.mockk.confirmVerified
import io.mockk.mockk
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter

abstract class AbstractItemWriterTest<T, W : ItemWriter<T>> {

    private val dslContextMock = mockk<DSLContext>()

    private lateinit var writer: W

    protected abstract fun newWriter(dslContextMock: DSLContext): W

    @BeforeEach
    internal fun setUp() {
        writer = newWriter(dslContextMock)
    }

    @Test
    internal fun writingEmptyList_doesNotInteractWithJooq() {
        writer.write(Chunk(emptyList()))
        confirmVerified(dslContextMock)
    }
}
