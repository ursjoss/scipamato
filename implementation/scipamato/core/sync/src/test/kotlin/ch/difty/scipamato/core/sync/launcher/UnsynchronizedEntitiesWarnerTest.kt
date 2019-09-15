package ch.difty.scipamato.core.sync.launcher

import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

internal class UnsynchronizedEntitiesWarnerTest {

    @Test
    fun findingUnsynchronizedPapers_withNoRecords_returnsEmptyOptional() {
        val warnerSpy = spy(UnsynchronizedEntitiesWarner::class.java)
        doReturn(emptyList<Any>()).whenever(warnerSpy).retrieveRecords()
        assertThat(warnerSpy.findUnsynchronizedPapers()).isEmpty
        verify(warnerSpy).retrieveRecords()
    }

    @Test
    fun findingUnsynchronizedPapers_withRecords_returnsResultingMessage() {
        val numbers = listOf(5L, 18L, 3L)
        val warnerSpy = spy(UnsynchronizedEntitiesWarner::class.java)
        doReturn(numbers).whenever(warnerSpy).retrieveRecords()
        assertThat(warnerSpy.findUnsynchronizedPapers().get()).isEqualTo("Papers not synchronized due to missing codes: Number 5, 18, 3.")
        verify(warnerSpy).retrieveRecords()
    }
}