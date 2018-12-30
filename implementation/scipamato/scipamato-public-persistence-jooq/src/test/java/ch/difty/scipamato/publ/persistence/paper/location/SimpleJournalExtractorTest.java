package ch.difty.scipamato.publ.persistence.paper.location;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.difty.scipamato.publ.persistence.paper.JournalExtractor;

public class SimpleJournalExtractorTest {

    private final JournalExtractor extractor = new SimpleJournalExtractor();

    @Test
    public void extractingJournal_fromNullLocation_returnsBlank() {
        assertThat(extractor.extractJournal(null)).isBlank();
    }

    @Test
    public void extractingJournal_fromBlankLocation_returnsBlank() {
        assertThat(extractor.extractJournal("")).isBlank();
    }

    @Test
    public void extractingJournal_fromLocationWithoutPeriod_returnsBlank() {
        assertThat(extractor.extractJournal("foo")).isEqualTo("foo");
    }

    @Test
    public void extractingJournal_fromLocationWithDot_returnsJournal() {
        assertThat(extractor.extractJournal("Nature. 2017; 543 (7647): 705-709.")).isEqualTo("Nature");
    }

    @Test
    public void extractingJournal_fromLocationWithDotOnlyLast_returnsJournal() {
        assertThat(extractor.extractJournal("Air Qual Atmos Health (2017) 10: 129-137.")).isEqualTo(
            "Air Qual Atmos Health");
    }

}
