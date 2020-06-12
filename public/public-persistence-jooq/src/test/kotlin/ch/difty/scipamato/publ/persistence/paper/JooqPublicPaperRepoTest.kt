package ch.difty.scipamato.publ.persistence.paper

import ch.difty.scipamato.common.ClearAllMocksExtension
import ch.difty.scipamato.common.persistence.JooqSortMapper
import ch.difty.scipamato.publ.db.tables.Paper
import ch.difty.scipamato.publ.db.tables.records.PaperRecord
import ch.difty.scipamato.publ.entity.PublicPaper
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Suppress("SpellCheckingInspection")
@ExtendWith(MockKExtension::class, ClearAllMocksExtension::class)
internal class JooqPublicPaperRepoTest {

    @MockK
    private lateinit var dslMock: DSLContext

    @MockK
    private lateinit var sortMapperMock: JooqSortMapper<PaperRecord, PublicPaper, Paper>

    @MockK
    private lateinit var filterConditionMapperMock: PublicPaperFilterConditionMapper

    @MockK(relaxed = true)
    private lateinit var authorsAbbreviator: AuthorsAbbreviator

    @MockK(relaxed = true)
    private lateinit var journalExtractor: JournalExtractor

    private lateinit var repo: JooqPublicPaperRepo

    @BeforeEach
    fun setUp() {
        repo = object : JooqPublicPaperRepo(
            dslMock,
            sortMapperMock,
            filterConditionMapperMock,
            authorsAbbreviator,
            journalExtractor
        ) {
            override val mainLanguage get() = "de"
        }
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(dslMock, sortMapperMock, filterConditionMapperMock)
    }

    @Test
    fun mapping_withPaperRecordHandingBackNullEvenForAuditDates_doesNotThrow() {
        val pr = mockk<PaperRecord>(relaxed = true) {
            every { created } returns null
            every { lastModified } returns null
        }
        val pp = repo.map(pr)
        pp.created.shouldBeNull()
        pp.lastModified.shouldBeNull()
    }

    @Test
    fun mapping_callsAuthorsAbbreviator_withAuthors() {
        val authors = "authors"
        val authorsAbbr = "auths"
        val pr = mockk<PaperRecord>(relaxed = true)
        every { pr.authors } returns authors
        every { authorsAbbreviator.abbreviate(authors) } returns authorsAbbr

        val pp = repo.map(pr)

        pp.authors shouldBeEqualTo authors
        pp.authorsAbbreviated shouldBeEqualTo authorsAbbr

        verify { authorsAbbreviator.abbreviate(authors) }
    }

    @Test
    fun mapping_callsJournalExtractor_withLocation() {
        val location = "location"
        val journal = "journal"
        val pr = mockk<PaperRecord>(relaxed = true)
        every { pr.location } returns location
        every { journalExtractor.extractJournal(location) } returns journal

        val pp = repo.map(pr)

        pp.location shouldBeEqualTo location
        pp.journal shouldBeEqualTo journal

        verify { journalExtractor.extractJournal(location) }
    }
}
