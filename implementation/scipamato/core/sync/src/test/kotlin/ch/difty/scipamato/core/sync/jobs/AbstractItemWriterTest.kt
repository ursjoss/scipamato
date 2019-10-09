package ch.difty.scipamato.core.sync.jobs

import com.nhaarman.mockitokotlin2.mock
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.batch.item.ItemWriter

abstract class AbstractItemWriterTest<T, W : ItemWriter<T>> {

    private val dslContextMock = mock<DSLContext>()

    private lateinit var writer: W

    protected abstract fun newWriter(dslContextMock: DSLContext?): W

    @BeforeEach
    internal fun setUp() {
        writer = newWriter(dslContextMock)
    }

    @Test
    internal fun writingEmptyList_doesNotInteractWithJooq() {
        writer.write(ArrayList())
        verifyNoMoreInteractions(dslContextMock)
    }
}
