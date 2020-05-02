package ch.difty.scipamato.publ.persistence

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import ch.difty.scipamato.publ.entity.Code
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.entity.PopulationCode
import ch.difty.scipamato.publ.entity.StudyDesignCode
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter
import ch.difty.scipamato.publ.persistence.paper.JooqPublicPaperRepo
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "FunctionName", "MagicNumber", "SpellCheckingInspection")
internal open class JooqPublicPaperRepoIntegrationTest {

    @Autowired
    private lateinit var repo: JooqPublicPaperRepo

    private val pc = PaginationRequest(0, 10)

    private val filter = PublicPaperFilter()

    private val allSorted = PaginationRequest(Direction.ASC, "authors")

    @Test
    fun findingByNumber_withExistingNumber_returnsEntity() {
        val number: Long = 1
        val paper = repo.findByNumber(number) ?: fail { "Unable to find paper with number $number" }
        paper.id shouldBeEqualTo number
        paper.pmId shouldBeEqualTo 25395026
        paper.authors shouldBeEqualTo
            "Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM."
        paper.location shouldBeEqualTo "Am J Epidemiol. 2014; 180 (12): 1145-1149."
        paper.journal shouldBeEqualTo "Am J Epidemiol"
    }

    @Test
    fun findingByNumber_withNonExistingNumber_returnsNull() {
        repo.findByNumber(-1L).shouldBeNull()
    }

    @Test
    fun findingPageByFilter_forPapersAsOf2017_findsOne_() {
        filter.publicationYearFrom = 2016
        repo.findPageByFilter(filter, pc) shouldHaveSize 3
    }

    @Test
    fun findingPageByFilter_forAnyPaper() {
        repo.findPageByFilter(filter, pc) shouldHaveSize 10
    }

    @Test
    fun countingByFilter_withNoFilterCriteria_findsTwo() {
        repo.countByFilter(filter) shouldBeEqualTo 13
    }

    @Test
    fun countingByFilter_withAuthorMask_findsOne() {
        filter.authorMask = "Gapstur"
        repo.countByFilter(filter) shouldBeEqualTo 1
    }

    @Test
    fun countingByFilter_withTitleMask_findsOne() {
        filter.titleMask = "ambient"
        repo.countByFilter(filter) shouldBeEqualTo 3
    }

    @Test
    fun countingByFilter_withMethodsMask_findsOne() {
        filter.methodsMask = "Sensitivitätsanalysen"
        repo.countByFilter(filter) shouldBeEqualTo 1
    }

    @Test
    fun findingPageByFilter_adultsOnly() {
        filter.populationCodes = listOf(PopulationCode.ADULTS)
        repo.findPageByFilter(filter, pc) shouldHaveSize 2
    }

    @Test
    fun findingPageByFilter_overViewMethodologyOnly() {
        filter.studyDesignCodes = listOf(StudyDesignCode.OVERVIEW_METHODOLOGY)
        repo.findPageByFilter(filter, pc) shouldHaveSize 1
    }

    @Test
    fun findingPageOfNumbersByFilter() {
        filter.publicationYearFrom = 2015
        filter.publicationYearUntil = 2018
        repo.findPageOfNumbersByFilter(filter, allSorted) shouldContainAll
            listOf(8984L, 8934L, 8924L, 2L, 8933L, 8983L, 8993L, 8861L, 8916L, 8973L, 8897L)
    }

    private fun newCodes(vararg codes: String): List<Code> = mutableListOf<Code>().apply {
        codes.forEach { add(Code.builder().code(it).build()) }
    }

    @Test
    fun findingPageByFilter_withCodes1Fand5H_finds2() {
        filter.codesOfClass1 = newCodes("1F")
        filter.codesOfClass5 = newCodes("5H")
        repo.findPageByFilter(filter, allSorted).map { it.number } shouldContainSame listOf(1L, 2L)
    }

    @Test
    fun findingPageByFilter_withCodes6Mand7L_finds3() {
        filter.codesOfClass6 = newCodes("6M")
        filter.codesOfClass7 = newCodes("7L")
        repo.findPageByFilter(filter, allSorted).map { it.number } shouldContainSame listOf(1L, 2L, 3L)
    }

    @Test
    fun findingPageByFilter_withCode2R_finds1() {
        filter.codesOfClass2 = newCodes("2R")
        repo.findPageByFilter(filter, allSorted).map { it.number } shouldContainAll listOf(3L)
    }

    @Test
    fun findingPageByFilter_withOriginallySetAndThenClearedFilter_findsAll() {
        filter.codesOfClass2 = newCodes("2R")
        filter.codesOfClass2.clear()
        repo.findPageByFilter(filter, allSorted).map { it.number } shouldContainSame
            listOf(8984L, 8934L, 8924L, 2L, 8933L, 8983L, 8993L, 8861L, 8916L, 8973L, 1L, 3L, 8897L)
    }

    @Test
    fun findingPageByFilter_withCodes6Mand7L_andWithKeywordWithId3_findsOnlyOne() {
        filter.codesOfClass6 = newCodes("6M")
        filter.codesOfClass7 = newCodes("7L")
        filter.keywords = listOf(Keyword(1, 3, "en", "foo", null))
        repo.findPageByFilter(filter, allSorted).map { it.number } shouldContainSame listOf(3L)
    }

    @Test
    fun findingPageByFilter_withKeywordWithId3And6_findsOnlyOne() {
        val kw1 = Keyword(1, 3, "en", "foo", null)
        filter.keywords = listOf(kw1)
        repo.findPageByFilter(filter, allSorted).isEmpty().shouldBeFalse()

        val kw2 = Keyword(7, 2, "en", "bar", null)
        filter.keywords = listOf(kw1, kw2)
        repo.findPageByFilter(filter, allSorted).shouldBeEmpty()
    }
}
