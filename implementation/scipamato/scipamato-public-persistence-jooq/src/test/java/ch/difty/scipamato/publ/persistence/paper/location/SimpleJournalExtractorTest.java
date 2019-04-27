package ch.difty.scipamato.publ.persistence.paper.location;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.difty.scipamato.publ.persistence.paper.JournalExtractor;

class SimpleJournalExtractorTest {

    private final JournalExtractor extractor = new SimpleJournalExtractor();

    @Test
    void extractingJournal_fromNullLocation_returnsBlank() {
        assertThat(extractor.extractJournal(null)).isBlank();
    }

    @Test
    void extractingJournal_fromBlankLocation_returnsBlank() {
        assertThat(extractor.extractJournal("")).isBlank();
    }

    @Test
    void extractingJournal_fromLocationWithoutPeriod_returnsBlank() {
        assertThat(extractor.extractJournal("foo")).isEqualTo("foo");
    }

    @Test
    void extractingJournal_fromLocationWithDot_returnsJournal() {
        assertThat(extractor.extractJournal("Nature. 2017; 543 (7647): 705-709.")).isEqualTo("Nature");
    }

    @Test
    void extractingJournal_fromLocationWithDotOnlyLast_returnsJournal() {
        assertThat(extractor.extractJournal("Air Qual Atmos Health (2017) 10: 129-137.")).isEqualTo(
            "Air Qual Atmos Health");
    }

}
