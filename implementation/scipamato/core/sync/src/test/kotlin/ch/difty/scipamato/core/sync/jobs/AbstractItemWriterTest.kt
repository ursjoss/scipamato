package ch.difty.scipamato.core.sync.jobs

import com.nhaarman.mockitokotlin2.mock
import org.jooq.DSLContext
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.batch.item.ItemWriter
import java.util.*

abstract class AbstractItemWriterTest<T, W : ItemWriter<T>> {

    private val dslContextMock = mock<DSLContext>()

    private var writer: W = newWriter(dslContextMock)

    protected abstract fun newWriter(dslContextMock: DSLContext?): W

    @Test
    internal fun writingEmptyList_doesNotInteractWithJooq() {
        writer.write(ArrayList())
        verifyNoMoreInteractions(dslContextMock)
    }

}
