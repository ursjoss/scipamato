package ch.difty.scipamato.core.sync.launcher

import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeTrue
import org.junit.jupiter.api.Test

internal class UnsynchronizedEntitiesWarnerTest {

    @Test
    fun findingUnsynchronizedPapers_withNoRecords_returnsEmptyOptional() {
        val warnerSpy = spyk<UnsynchronizedEntitiesWarner> {
            every { retrieveRecords() } returns emptyList()
        }
        warnerSpy.findUnsynchronizedPapers().isEmpty.shouldBeTrue()
        verify { warnerSpy.retrieveRecords() }
    }

    @Test
    fun findingUnsynchronizedPapers_withRecords_returnsResultingMessage() {
        val numbers = listOf(5L, 18L, 3L)
        val warnerSpy = spyk<UnsynchronizedEntitiesWarner> {
            every { retrieveRecords() } returns numbers
        }
        warnerSpy.findUnsynchronizedPapers().get() shouldBeEqualTo
            "Papers not synchronized due to missing codes: Number 5, 18, 3."
        verify { warnerSpy.retrieveRecords() }
    }
}
