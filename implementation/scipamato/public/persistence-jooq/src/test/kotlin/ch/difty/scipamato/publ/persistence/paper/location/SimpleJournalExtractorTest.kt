package ch.difty.scipamato.publ.persistence.paper.location

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SimpleJournalExtractorTest {

    private val extractor = SimpleJournalExtractor()

    @Test
    fun extractingJournal_fromNullLocation_returnsBlank() {
        assertThat(extractor.extractJournal(null)).isBlank()
    }

    @Test
    fun extractingJournal_fromBlankLocation_returnsBlank() {
        assertThat(extractor.extractJournal("")).isBlank()
    }

    @Test
    fun extractingJournal_fromLocationWithoutPeriod_returnsBlank() {
        assertThat(extractor.extractJournal("foo")).isEqualTo("foo")
    }

    @Test
    fun extractingJournal_fromLocationWithDot_returnsJournal() {
        assertThat(extractor.extractJournal("Nature. 2017; 543 (7647): 705-709.")).isEqualTo("Nature")
    }

    @Test
    fun extractingJournal_fromLocationWithDotOnlyLast_returnsJournal() {
        assertThat(extractor.extractJournal("Air Qual Atmos Health (2017) 10: 129-137.")).isEqualTo(
                "Air Qual Atmos Health")
    }

}
