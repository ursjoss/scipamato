package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.MAX_ID_PREPOPULATED
import ch.difty.scipamato.core.persistence.RECORD_COUNT_PREPOPULATED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("FunctionName", "MagicNumber", "SpellCheckingInspection")
@JooqTest
@Testcontainers
internal open class JooqPaperSlimRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqPaperSlimRepo

    @Test
    fun findingAll() {
        val papers = repo.findAll()
        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED)
    }

    @Test
    fun findingById_withExistingId_returnsEntity() {
        val paper = repo.findById(MAX_ID_PREPOPULATED)
        @Suppress("ConstantConditionIf")
        if (MAX_ID_PREPOPULATED > 0)
            assertThat(paper.id).isEqualTo(MAX_ID_PREPOPULATED)
        else
            assertThat(paper).isNull()
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1L)).isNull()
    }

    @Test
    fun findingById_withExistingIdAndVersion_returnsEntityWithCorrectIdButMissingVersion() {
        val paper = repo.findById(31L, 11, "en")
        assertThat(paper.id).isEqualTo(31L)
        assertThat(paper.number).isEqualTo(31L)
        assertThat(paper.publicationYear).isEqualTo(2016)

        assertThat(paper.firstAuthor).isEqualTo("Lanzinger")
        assertThat(paper.version).isEqualTo(0)
        assertThat(paper.title).startsWith("Ultrafine")
        val newsletterAssociation = paper.newsletterAssociation
        assertThat(newsletterAssociation == null).isFalse()
        assertThat(newsletterAssociation.id).isEqualTo(1L)
        assertThat(newsletterAssociation.publicationStatusId).isEqualTo(1L)
        assertThat(newsletterAssociation.issue).isEqualTo("1802")
        assertThat(newsletterAssociation.headline).isEqualTo("some headline")
    }

    @Test
    fun findingById_withExistingIdAndWrongVersion_returnsNull() {
        val paper = repo.findById(31L, 1, "en")
        assertThat(paper).isNull()
    }

    @Test
    fun findingPageByFilter() {
        val pf = PaperFilter()

        pf.authorMask = "lanz"
        val pc = PaginationRequest(0, 10)
        val papers = repo.findPageByFilter(pf, pc)
        assertThat(papers).hasSize(2)
        assertThat(papers.map { it.number }).containsExactlyInAnyOrder(33L, 31L)
    }

    @Suppress("DuplicatedCode")
    @Test
    fun bug21_queryingAllFieldsShouldNotThrowAnException() {
        val so = SearchOrder()
        val sc = SearchCondition()
        so.add(sc)

        val x = ""
        sc.id = "1"
        sc.doi = x
        sc.pmId = x
        sc.authors = x
        sc.firstAuthor = x
        sc.isFirstAuthorOverridden = true
        sc.title = x
        sc.location = x
        sc.publicationYear = "1984"

        sc.goals = x
        sc.population = x
        sc.methods = x

        sc.populationPlace = x
        sc.populationParticipants = x
        sc.populationDuration = x
        sc.exposurePollutant = x
        sc.exposureAssessment = x
        sc.methodStudyDesign = x
        sc.methodOutcome = x
        sc.methodStatistics = x
        sc.methodConfounders = x

        sc.result = x
        sc.comment = x
        sc.intern = x

        sc.resultExposureRange = x
        sc.resultEffectEstimate = x

        sc.originalAbstract = x

        assertThat(repo.findBySearchOrder(so)).isEmpty()
    }

    @Test
    fun canQueryNewsletterFields() {
        val paper = repo.findById(31L, "en")

        val na = paper.newsletterAssociation
        assertThat(na.issue).isEqualTo("1802")
        assertThat(na.publicationStatusId).isEqualTo(1)
        assertThat(na.headline).isEqualTo("some headline")
    }
}
