package ch.difty.scipamato.core.persistence.paper.slim

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.MAX_ID_PREPOPULATED
import ch.difty.scipamato.core.persistence.RECORD_COUNT_PREPOPULATED
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldStartWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
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
        papers shouldHaveSize RECORD_COUNT_PREPOPULATED
    }

    @Test
    fun findingById_withExistingId_returnsEntity() {
        val paper = repo.findById(MAX_ID_PREPOPULATED) ?: fail { "Unable to load paper" }
        @Suppress("ConstantConditionIf")
        if (MAX_ID_PREPOPULATED > 0)
            paper.id shouldBeEqualTo MAX_ID_PREPOPULATED
        else
            paper.shouldBeNull()
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        repo.findById(-1L).shouldBeNull()
    }

    @Test
    fun findingById_withExistingIdAndVersion_returnsEntityWithCorrectIdButMissingVersion() {
        val paper = repo.findById(31L, 11, "en") ?: fail { "Unable to load paper" }
        paper.id shouldBeEqualTo 31L
        paper.number shouldBeEqualTo 31L
        paper.publicationYear shouldBeEqualTo 2016

        paper.firstAuthor shouldBeEqualTo "Lanzinger"
        paper.version shouldBeEqualTo 0
        paper.title shouldStartWith "Ultrafine"
        val newsletterAssociation = paper.newsletterAssociation
        newsletterAssociation.shouldNotBeNull()
        newsletterAssociation.id shouldBeEqualTo 1
        newsletterAssociation.publicationStatusId shouldBeEqualTo 1
        newsletterAssociation.issue shouldBeEqualTo "1802"
        newsletterAssociation.headline shouldBeEqualTo "some headline"
    }

    @Test
    fun findingById_withExistingIdAndWrongVersion_returnsNull() {
        val paper = repo.findById(31L, 1, "en")
        paper.shouldBeNull()
    }

    @Test
    fun findingPageByFilter() {
        val pf = PaperFilter()

        pf.authorMask = "lanz"
        val pc = PaginationRequest(0, 10)
        val papers = repo.findPageByFilter(pf, pc)
        papers shouldHaveSize 2
        papers.map { it.number } shouldContainSame listOf(33L, 31L)
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

        repo.findBySearchOrder(so).shouldBeEmpty()
    }

    @Test
    fun canQueryNewsletterFields() {
        val paper = repo.findById(31L, "en") ?: fail { "Unable to load paper" }

        val na = paper.newsletterAssociation
        na.issue shouldBeEqualTo "1802"
        na.publicationStatusId shouldBeEqualTo 1
        na.headline shouldBeEqualTo "some headline"
    }
}
