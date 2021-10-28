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
        val warner = UnsynchronizedEntitiesWarner(
            jooqCore = dslContextMock,
            paperProvider = ::emptyList,
            newsletterPaperProvider = ::emptyList
        )
        warner.findUnsynchronizedPapers().shouldBeNull()
    }

    @Test
    fun findingUnsynchronizedPapers_withRecords_returnsResultingMessage() {
        val numbers = listOf(5L, 18L, 3L)
        val warner = UnsynchronizedEntitiesWarner(
            jooqCore = dslContextMock,
            paperProvider = { numbers },
            newsletterPaperProvider = ::emptyList
        )
        warner.findUnsynchronizedPapers() shouldBeEqualTo
            "Papers not synchronized due to missing codes: Number 5, 18, 3."
    }

    @Test
    fun findingUnsynchronizedNewsletters_withRecords_returnsResultingMessage() {
        val papers = listOf("100 (2021/10), 120 (2021/10)")
        val warner = UnsynchronizedEntitiesWarner(
            jooqCore = dslContextMock,
            paperProvider = ::emptyList,
            newsletterPaperProvider = { papers }
        )
        warner.findNewsletterswithUnsynchronizedPapers() shouldBeEqualTo
            "Incomplete Newsletters due to papers with missing codes - Papers: 100 (2021/10), 120 (2021/10)."
    }
}
