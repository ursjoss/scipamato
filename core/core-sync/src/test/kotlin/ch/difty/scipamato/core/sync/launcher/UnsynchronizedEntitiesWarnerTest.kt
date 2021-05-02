package ch.difty.scipamato.core.sync.launcher

import io.mockk.mockk
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.jooq.DSLContext
import org.junit.jupiter.api.Test

internal class UnsynchronizedEntitiesWarnerTest {

    val dslContextMock = mockk<DSLContext>()

    @Test
    fun findingUnsynchronizedPapers_withNoRecords_returnsEmptyOptional() {
        val warner = UnsynchronizedEntitiesWarner(dslContextMock) { emptyList() }
        warner.findUnsynchronizedPapers().shouldBeNull()
    }

    @Test
    fun findingUnsynchronizedPapers_withRecords_returnsResultingMessage() {
        val numbers = listOf(5L, 18L, 3L)
        val warner = UnsynchronizedEntitiesWarner(dslContextMock) { numbers }
        warner.findUnsynchronizedPapers() shouldBeEqualTo
            "Papers not synchronized due to missing codes: Number 5, 18, 3."
    }
}
