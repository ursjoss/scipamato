package ch.difty.scipamato.publ.persistence.paper.location

import org.amshove.kluent.shouldBeBlank
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class SimpleJournalExtractorTest {

    private val extractor = SimpleJournalExtractor()

    @Test
    fun extractingJournal_fromNullLocation_returnsBlank() {
        extractor.extractJournal(null).shouldBeBlank()
    }

    @Test
    fun extractingJournal_fromBlankLocation_returnsBlank() {
        extractor.extractJournal("").shouldBeBlank()
    }

    @Test
    fun extractingJournal_fromLocationWithoutPeriod_returnsBlank() {
        extractor.extractJournal("foo") shouldBeEqualTo "foo"
    }

    @Test
    fun extractingJournal_fromLocationWithDot_returnsJournal() {
        extractor.extractJournal("Nature. 2017; 543 (7647): 705-709.") shouldBeEqualTo "Nature"
    }

    @Test
    fun extractingJournal_fromLocationWithDotOnlyLast_returnsJournal() {
        extractor.extractJournal("Air Qual Atmos Health (2017) 10: 129-137.") shouldBeEqualTo "Air Qual Atmos Health"
    }
}
