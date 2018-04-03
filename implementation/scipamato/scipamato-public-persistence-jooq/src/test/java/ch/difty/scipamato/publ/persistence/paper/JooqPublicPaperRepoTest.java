package ch.difty.scipamato.publ.persistence.paper;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.persistence.JooqSortMapper;
import ch.difty.scipamato.publ.db.tables.Paper;
import ch.difty.scipamato.publ.db.tables.records.PaperRecord;
import ch.difty.scipamato.publ.entity.PublicPaper;

@RunWith(MockitoJUnitRunner.class)
public class JooqPublicPaperRepoTest {

    private JooqPublicPaperRepo repo;

    @Mock
    private DSLContext                                      dslMock;
    @Mock
    private JooqSortMapper<PaperRecord, PublicPaper, Paper> sortMapperMock;
    @Mock
    private PublicPaperFilterConditionMapper                filterConditionMapperMock;
    @Mock
    private AuthorsAbbreviator                              authorsAbbreviator;
    @Mock
    private JournalExtractor                                journalExtractor;

    @Before
    public void setUp() {
        repo = new JooqPublicPaperRepo(dslMock, sortMapperMock, filterConditionMapperMock, authorsAbbreviator,
                journalExtractor);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dslMock, sortMapperMock, filterConditionMapperMock);
    }

    @Test
    public void findingByNumber_withNullNumber_throws() {
        assertDegenerateSupplierParameter(() -> repo.findByNumber(null), "number");
    }

    @Test
    public void mapping_withPaperRecordHandingBackNullEvenForAuditDates_doesNotThrow() {
        PaperRecord pr = mock(PaperRecord.class);
        PublicPaper pp = repo.map(pr);
        assertThat(pp.getCreated()).isNull();
        assertThat(pp.getLastModified()).isNull();
    }

    @Test
    public void mapping_callsAuthorsAbbreviator_withAuthors() {
        final String authors = "authors";
        final String authorsAbbr = "auts";
        PaperRecord pr = mock(PaperRecord.class);
        when(pr.getAuthors()).thenReturn(authors);
        when(authorsAbbreviator.abbreviate(authors)).thenReturn(authorsAbbr);

        PublicPaper pp = repo.map(pr);

        assertThat(pp.getAuthors()).isEqualTo(authors);
        assertThat(pp.getAuthorsAbbreviated()).isEqualTo(authorsAbbr);

        verify(authorsAbbreviator).abbreviate(authors);
    }

    @Test
    public void mapping_callsJournalExtractor_withLocation() {
        final String location = "location";
        final String journal = "journal";
        PaperRecord pr = mock(PaperRecord.class);
        when(pr.getLocation()).thenReturn(location);
        when(journalExtractor.extractJournal(location)).thenReturn(journal);

        PublicPaper pp = repo.map(pr);

        assertThat(pp.getLocation()).isEqualTo(location);
        assertThat(pp.getJournal()).isEqualTo(journal);

        verify(journalExtractor).extractJournal(location);
    }
}
