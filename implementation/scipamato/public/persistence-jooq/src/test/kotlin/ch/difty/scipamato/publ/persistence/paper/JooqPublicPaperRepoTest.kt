package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.persistence.JooqSortMapper
import ch.difty.scipamato.publ.db.tables.Paper
import ch.difty.scipamato.publ.db.tables.records.PaperRecord
import ch.difty.scipamato.publ.entity.PublicPaper
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

internal class JooqPublicPaperRepoTest {

    private val dslMock = mock<DSLContext>()
    private val sortMapperMock = mock<JooqSortMapper<PaperRecord, PublicPaper, Paper>>()
    private val filterConditionMapperMock = mock<PublicPaperFilterConditionMapper>()
    private val authorsAbbreviator = mock<AuthorsAbbreviator>()
    private val journalExtractor = mock<JournalExtractor>()

    private var repo: JooqPublicPaperRepo = object : JooqPublicPaperRepo(dslMock, sortMapperMock, filterConditionMapperMock, authorsAbbreviator, journalExtractor) {
        override fun getMainLanguage() = "de"
    }

    @AfterEach
    fun tearDown() {
        verifyNoMoreInteractions(dslMock, sortMapperMock, filterConditionMapperMock)
    }

    @Test
    fun mapping_withPaperRecordHandingBackNullEvenForAuditDates_doesNotThrow() {
        val pr = mock(PaperRecord::class.java)
        val pp = repo.map(pr)
        assertThat(pp.created).isNull()
        assertThat(pp.lastModified).isNull()
    }

    @Test
    fun mapping_callsAuthorsAbbreviator_withAuthors() {
        val authors = "authors"
        val authorsAbbr = "auths"
        val pr = mock(PaperRecord::class.java)
        whenever(pr.authors).thenReturn(authors)
        whenever(authorsAbbreviator.abbreviate(authors)).thenReturn(authorsAbbr)

        val pp = repo.map(pr)

        assertThat(pp.authors).isEqualTo(authors)
        assertThat(pp.authorsAbbreviated).isEqualTo(authorsAbbr)

        verify(authorsAbbreviator).abbreviate(authors)
    }

    @Test
    fun mapping_callsJournalExtractor_withLocation() {
        val location = "location"
        val journal = "journal"
        val pr = mock(PaperRecord::class.java)
        whenever(pr.location).thenReturn(location)
        whenever(journalExtractor.extractJournal(location)).thenReturn(journal)

        val pp = repo.map(pr)

        assertThat(pp.location).isEqualTo(location)
        assertThat(pp.journal).isEqualTo(journal)

        verify(journalExtractor).extractJournal(location)
    }
}
