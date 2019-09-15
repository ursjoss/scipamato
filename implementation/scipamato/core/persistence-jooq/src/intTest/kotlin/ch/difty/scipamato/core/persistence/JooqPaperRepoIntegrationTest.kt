package ch.difty.scipamato.core.persistence

import ch.difty.scipamato.common.persistence.paging.PaginationRequest
import ch.difty.scipamato.common.persistence.paging.Sort.Direction
import ch.difty.scipamato.core.db.Tables.PAPER_ATTACHMENT
import ch.difty.scipamato.core.db.tables.Code
import ch.difty.scipamato.core.db.tables.CodeClass
import ch.difty.scipamato.core.db.tables.SearchExclusion
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperAttachment
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchCondition
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.paper.JooqPaperRepo
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jooq.DSLContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("FunctionName", "SpellCheckingInspection")
internal open class JooqPaperRepoIntegrationTest {

    @Autowired
    private lateinit var dsl: DSLContext

    @Autowired
    private lateinit var repo: JooqPaperRepo

    @Test
    fun findingAll() {
        val papers = repo.findAll()
        papers.sortBy { it.id }

        assertThat(papers).hasSize(RECORD_COUNT_PREPOPULATED)
        assertThat(papers[0].id).isEqualTo(1)
        assertThat(papers[1].id).isEqualTo(2)
        assertThat(papers[2].id).isEqualTo(3)
        assertThat(papers[3].id).isEqualTo(4)
        assertThat(papers[4].id).isEqualTo(10)
        assertThat(papers[13].id).isEqualTo(19)
        assertThat(papers[22].id).isEqualTo(28)
    }

    @Test
    fun findingById_withExistingId_returnsEntity() {
        val id: Long = 4
        val paper = repo.findById(id)
        assertThat(paper.id).isEqualTo(id)
        assertThat(paper.authors).isEqualTo("Kutlar Joss M, Joss U.")
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        assertThat(repo.findById(-1L) == null).isTrue()
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val p = makeMinimalPaper()
        assertThat(p.id == null).isTrue()

        val saved = repo.add(p)
        assertThat(saved.id)
                .isNotNull()
                .isGreaterThan(MAX_ID_PREPOPULATED)
        assertThat(saved.authors).isEqualTo("a")
    }

    private fun makeMinimalPaper(): Paper {
        val p = Paper()
        p.number = 100L
        p.authors = "a"
        p.firstAuthor = "b"
        p.isFirstAuthorOverridden = true
        p.title = "t"
        p.location = "l"
        p.goals = "g"
        return p
    }

    @Test
    fun updatingRecord() {
        val paper = repo.add(makeMinimalPaper())
        assertThat(paper.id)
                .isNotNull()
                .isGreaterThan(MAX_ID_PREPOPULATED)
        val id = paper.id
        assertThat(paper.authors).isEqualTo("a")

        paper.authors = "b"
        repo.update(paper)
        assertThat(paper.id).isEqualTo(id)

        val newCopy = repo.findById(id)
        assertThat(newCopy).isNotEqualTo(paper)
        assertThat(newCopy.id).isEqualTo(id)
        assertThat(newCopy.authors).isEqualTo("b")
    }

    @Test
    fun savingAssociatedEntitiesOf_withCodes() {
        val paper = repo.add(makeMinimalPaper())
        assertThat(paper.id)
                .isNotNull()
                .isGreaterThan(MAX_ID_PREPOPULATED)
        val id = paper.id
        assertThat(paper.authors).isEqualTo("a")

        val cr = dsl.selectFrom(Code.CODE).limit(1).fetchOne()
        val ccr = dsl
                .selectFrom(CodeClass.CODE_CLASS)
                .where(CodeClass.CODE_CLASS.ID.eq(cr.codeClassId))
                .fetchOne()
        val codeClass = ch.difty.scipamato.core.entity.CodeClass(ccr.id, "", "")
        val code = ch.difty.scipamato.core.entity.Code(cr.code, "", "", true,
                codeClass.id, codeClass.name, codeClass.description, cr.sort, null, null, null, null, null)
        paper.addCode(code)

        repo.update(paper)
        assertThat(paper.id).isEqualTo(id)

        val newCopy = repo.findById(id)
        assertThat(newCopy).isNotEqualTo(paper)
        assertThat(newCopy.id).isEqualTo(id)
        assertThat(newCopy.codes.map { it.code }).containsExactly(code.code)
    }

    @Test
    fun savingAssociatedEntitiesOf_withNewsletterLink() {
        val paper = repo.add(makeMinimalPaper())
        val id = paper.id
        val newsletterLink = Paper.NewsletterLink(2, "whatever", 1, 1, "topic1", "hl")

        paper.newsletterLink = newsletterLink

        repo.update(paper)
        assertThat(paper.id).isEqualTo(id)

        val newCopy = repo.findById(id)
        assertThat(newCopy).isNotEqualTo(paper)
        assertThat(newCopy.id).isEqualTo(id)
        assertThat(newCopy.newsletterLink.issue).isEqualTo("1804")
    }

    @Test
    fun deletingRecord() {
        val paper = repo.add(makeMinimalPaper())
        assertThat(paper.id)
                .isNotNull()
                .isGreaterThan(MAX_ID_PREPOPULATED)
        val id = paper.id
        assertThat(paper.authors).isEqualTo("a")

        val deleted = repo.delete(id, paper.version)
        assertThat(deleted.id).isEqualTo(id)

        assertThat(repo.findById(id)).isNull()
    }

    @Test
    @Disabled("TODO Need to fix Paper.toString first")
    fun findingById_forPaper1InGerman() {
        val paper = repo.findById(1L)
        assertThat(paper.toString()).isEqualTo(
                PAPER1_WO_CODE_CLASSES
                        + ",codes=["
                        + "codesOfClass1=["
                        + "Code[code=1F,name=Feinstaub, Partikel,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "],"
                        + "codesOfClass2=["
                        + "Code[code=2N,name=Übrige Länder,comment=<null>,internal=false,codeClass=CodeClass[id=2],sort=2,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "],"
                        + "codesOfClass3=["
                        + "Code[code=3C,name=Erwachsene (alle),comment=<null>,internal=false,codeClass=CodeClass[id=3],sort=3,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "],"
                        + "codesOfClass4=["
                        + "Code[code=4G,name=Krebs,comment=<null>,internal=false,codeClass=CodeClass[id=4],sort=7,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "],"
                        + "codesOfClass5=["
                        + "Code[code=5H,name=Kohortenstudie,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=7,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1], "
                        + "Code[code=5S,name=Statistik,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=10,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "],"
                        + "codesOfClass6=["
                        + "Code[code=6M,name=Mensch,comment=<null>,internal=false,codeClass=CodeClass[id=6],sort=1,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "],"
                        + "codesOfClass7="
                        + "[Code[code=7L,name=Langfristig,comment=<null>,internal=false,codeClass=CodeClass[id=7],sort=2,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "],"
                        + "codesOfClass8=["
                        + "Code[code=8O,name=Aussenluft,comment=<null>,internal=false,codeClass=CodeClass[id=8],sort=2,createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]"
                        + "]"
                        + "]"
                        + ID_PART
                        + "]")
        // @formatter:on
    }

    @Test
    fun findingByIds_returnsRecordForEveryIdExisting() {
        val papers = repo.findByIds(listOf(1L, 2L, 3L, 10L, -17L))
        assertThat(papers.map { it.id }).containsExactly(1L, 2L, 3L, 10L)

        // codes not enriched
        assertThat(papers[0].codes).isEmpty()
    }

    @Test
    fun findingByIds_returnsEmptyListForEmptyIdList() {
        assertThat(repo.findByIds(emptyList())).isEmpty()
    }

    @Test
    fun findingWithCodesByIds_returnsRecordForEveryIdExisting() {
        val papers = repo.findWithCodesByIds(listOf(1L, 2L, 3L, 10L, -17L), LC)
        assertThat(papers.map { it.id }).containsExactly(1L, 2L, 3L, 10L)

        // codes are present
        assertThat(papers[0].codes).isNotEmpty
    }

    @Test
    fun findingPapersByPmIds_withThreeValidPmIds_returnsThreePapers() {
        val papers = repo.findByPmIds(listOf(20335815, 27128166, 25104428), LC)
        assertThat(papers).hasSize(3)
        assertThat(papers.map { it.pmId }).containsOnly(20335815, 27128166, 25104428)
    }

    @Test
    fun findingPapersByPmIds_withInvalidPmIds_returnsEmptyList() {
        assertThat(repo.findByPmIds(listOf(-20335815), LC)).isEmpty()
    }

    @Test
    fun findingPapersByPmIds_hasCodesEnriched() {
        val papers = repo.findByPmIds(listOf(20335815), LC)
        assertThat(papers[0].codes).isNotEmpty
    }

    @Test
    fun findingExistingPmIdsOutOf_withThreeValidPmIds_returnsThreePMIDs() {
        val pmids = repo.findExistingPmIdsOutOf(listOf(20335815, 27128166, 25104428))
        assertThat(pmids).hasSize(3)
        assertThat(pmids).containsOnly(20335815, 27128166, 25104428)
    }

    @Test
    fun findingExistingPmIdsOutOf_withInvalidPmIds_returnsEmptyList() {
        assertThat(repo.findExistingPmIdsOutOf(listOf(-20335815))).isEmpty()
    }

    @Test
    fun findingPapersByNumbers_withThreeValidNumbers_returnsThreePapers() {
        val papers = repo.findByNumbers(listOf(1L, 2L, 3L), LC)
        assertThat(papers).hasSize(3)
        assertThat(papers.map { it.number }).containsOnly(1L, 2L, 3L)
    }

    @Test
    fun findingPapersByNumbers_withInvalidNumbers_returnsEmptyList() {
        assertThat(repo.findByNumbers(listOf(-1L), LC)).isEmpty()
    }

    @Test
    fun findingPapersByNumber_hasCodesEnriched() {
        val papers = repo.findByNumbers(listOf(1L), LC)
        assertThat(papers[0].codes).isNotEmpty
    }

    @Test
    fun findingBySearchOrder() {
        val searchOrder = SearchOrder()
        val sc = SearchCondition()
        sc.authors = "kutlar"
        searchOrder.add(sc)
        val papers = repo.findBySearchOrder(searchOrder, LC)
        assertThat(papers).isNotEmpty
    }

    @Test
    fun findingPageBySearchOrder() {
        val searchOrder = SearchOrder()
        val sc = SearchCondition()
        sc.authors = "kutlar"
        searchOrder.add(sc)
        assertThat(repo.findPageBySearchOrder(searchOrder, PaginationRequest(Direction.ASC, "authors"), LC)).isNotEmpty
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_findsFirstGapStartingAboveMinimumValue() {
        val number = repo.findLowestFreeNumberStartingFrom(0L)
        assertThat(number).isEqualTo(5L)
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_withMinimumInMultiNumberGap_ignoresRemainingNumbersOfSameGap() {
        val number = repo.findLowestFreeNumberStartingFrom(5L)
        assertThat(number).isGreaterThanOrEqualTo(42L)
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_withMinimumBeyondLastGap_findsNextFreeNumber() {
        val number = repo.findLowestFreeNumberStartingFrom(30)
        assertThat(number).isGreaterThanOrEqualTo(42L)
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_withMinimumBeyondNextFreeNumber_findsMinimumLeavingGap() {
        val number = repo.findLowestFreeNumberStartingFrom(100L)
        assertThat(number).isGreaterThanOrEqualTo(100L)
    }

    @Test
    fun findingPageOfIdsByFilter() {
        val filter = PaperFilter()
        filter.authorMask = "Kutlar"
        assertThat(repo.findPageOfIdsByFilter(filter, PaginationRequest(Direction.ASC, "authors"))).containsExactly(4L)
    }

    @Test
    fun findingPageOfIdsBySearchOrder() {
        val searchOrder = SearchOrder()
        val sc = SearchCondition()
        sc.authors = "kutlar"
        searchOrder.add(sc)
        assertThat(repo.findPageOfIdsBySearchOrder(searchOrder, PaginationRequest(Direction.ASC, "authors"))).containsExactly(4L)
    }

    @Test
    fun excludingPaperFromSearch_addsOneRecord_reIncluding_removesItAgain() {
        val searchOrderId: Long = 1
        val paperId: Long = 1
        ensureRecordNotPresent(searchOrderId, paperId)

        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId)
        assertExclusionCount(searchOrderId, paperId, 1)

        repo.reincludePaperIntoSearchOrderResults(searchOrderId, paperId)
        ensureRecordNotPresent(searchOrderId, paperId)
    }

    private fun ensureRecordNotPresent(searchOrderId: Long, paperId: Long) {
        assertExclusionCount(searchOrderId, paperId, 0)
    }

    private fun assertExclusionCount(searchOrderId: Long, paperId: Long, count: Int) {
        assertThat(dsl
                .selectCount()
                .from(SearchExclusion.SEARCH_EXCLUSION)
                .where(SearchExclusion.SEARCH_EXCLUSION.SEARCH_ORDER_ID.eq(searchOrderId))
                .and(SearchExclusion.SEARCH_EXCLUSION.PAPER_ID.eq(paperId))
                .fetchOne(0, Int::class.javaPrimitiveType)).isEqualTo(count)
    }

    @Test
    fun excludingPaperFromSearch_whenAddingMultipleTimes_ignoresAllButFirst() {
        val searchOrderId: Long = 1
        val paperId: Long = 1
        ensureRecordNotPresent(searchOrderId, paperId)

        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId)
        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId)
        repo.excludePaperFromSearchOrderResults(searchOrderId, paperId)
        assertExclusionCount(searchOrderId, paperId, 1)
    }

    @Test
    fun loadingSlimAttachment_loadsEverythingExceptContent() {
        val content1 = "baz"
        val pa1 = newPaperAttachment(TEST_FILE_1, content1)
        repo.saveAttachment(pa1)

        val content2 = "blup"
        val pa2 = newPaperAttachment(TEST_FILE_2, content2)
        repo.saveAttachment(pa2)

        val results = repo.loadSlimAttachment(TEST_PAPER_ID)

        assertThat(results).hasSize(2)
        val saved = results[0]

        assertThat(saved.name).isEqualTo(pa1.name)
        assertThat(saved.content).isNull()
        assertThat(saved.size).isEqualTo(content1.length.toLong())
        assertThat(saved.contentType).isEqualTo("application/pdf")
        assertThat(saved.created.toString()).isEqualTo("2016-12-09T06:02:13")
        assertThat(saved.lastModified.toString()).isEqualTo("2016-12-09T06:02:13")
    }

    private fun newPaperAttachment(name: String, content: String): PaperAttachment {
        return PaperAttachment(null, TEST_PAPER_ID, name, content.toByteArray(), "application/pdf", content.length.toLong())
    }

    @Test
    fun savingAttachment_whenNotExisting_insertsIntoDb() {
        val content = "foo"
        val pa = newPaperAttachment(TEST_FILE_1, content)

        val p = repo.saveAttachment(pa)
        val saved = dsl
                .select()
                .from(PAPER_ATTACHMENT)
                .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
                .fetchOneInto(PaperAttachment::class.java)

        assertThat(p.attachments.map { it.id }).contains(saved.id)

        assertThat(saved.name).isEqualTo(pa.name)
        assertThat(String(saved.content)).isEqualTo(content)
        assertThat(saved.size).isEqualTo(content.length.toLong())
        assertThat(saved.contentType).isEqualTo("application/pdf")
        assertThat(saved.created.toString()).isEqualTo("2016-12-09T06:02:13")
        assertThat(saved.lastModified.toString()).isEqualTo("2016-12-09T06:02:13")
    }

    @Test
    fun savingAttachment_whenExisted_performsUpdate() {
        val content2 = "bar"
        val pa1 = newPaperAttachment(TEST_FILE_1, "foo")
        val pa2 = newPaperAttachment(TEST_FILE_1, content2)
        assertThat(pa1.paperId).isEqualTo(pa2.paperId)
        assertThat(pa1.name).isEqualTo(pa2.name)

        repo.saveAttachment(pa1)
        repo.saveAttachment(pa2)

        val saved2 = dsl
                .select()
                .from(PAPER_ATTACHMENT)
                .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
                .fetchOneInto(PaperAttachment::class.java)

        assertThat(saved2.name).isEqualTo(pa1.name)
        assertThat(saved2.version).isEqualTo(2)
        assertThat(String(saved2.content)).isEqualTo(content2)
    }

    @Test
    fun loadingAttachmentWithContentById() {
        val content1 = "baz"
        val pa1 = newPaperAttachment(TEST_FILE_1, content1)
        repo.saveAttachment(pa1)

        val id = dsl
                .select(PAPER_ATTACHMENT.ID)
                .from(PAPER_ATTACHMENT)
                .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
                .fetchOneInto(Int::class.java)
        val attachment = repo.loadAttachmentWithContentBy(id)
        assertThat(attachment.content).isNotNull()
        assertThat(String(attachment.content)).isEqualTo(content1)
    }

    @Test
    fun deletingAttachment_deletes() {
        repo.saveAttachment(newPaperAttachment(TEST_FILE_1, "foo"))
        val id = dsl
                .select(PAPER_ATTACHMENT.ID)
                .from(PAPER_ATTACHMENT)
                .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
                .fetchOneInto(Int::class.java)
        assertThat(id).isNotNull()
        val p = repo.deleteAttachment(id)
        assertThat(p.attachments)
                .extracting("id")
                .doesNotContain(id)
        assertThat(dsl
                .select(PAPER_ATTACHMENT.ID)
                .from(PAPER_ATTACHMENT)
                .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
                .fetchOneInto(Int::class.java) == null).isTrue()
    }

    /**
     * Verify the rollback is occurring, also that the JooqExceptionTranslator is
     * doing it's job to translate jooq specific exceptions into spring exceptions.
     */
    @Test
    fun testDeclarativeTransaction() {
        var rollback = false
        val paper = repo.findById(1L)
        try {
            paper.number = null
            repo.update(paper)
            fail<Any>("Should have thrown exception due to null value on non-null column")
        } catch (dae: org.jooq.exception.DataAccessException) {
            fail<Any>("JooqExceptionTranslator did not translate the jooqException into a spring exception")
        } catch (dae: DataAccessException) {
            rollback = true
            assertThat(dae).isInstanceOf(DataIntegrityViolationException::class.java)
            assertThat(dae.message).startsWith("""jOOQ; SQL [update "public"."paper" set "number" = ?""")
        }

        assertThat(rollback).isTrue()
    }

    @Test
    fun findingByFilter_filteringByNewsletterId() {
        val filter = PaperFilter()
        filter.newsletterId = 1
        val papers = repo.findPageByFilter(filter, PaginationRequest(0, 10))
        assertThat(papers).hasSize(5)
        assertThat(papers.map { it.id }).contains(31L)
    }

    @Test
    fun findingById_populatesNewsLetterWithAllFields() {
        val paper = repo.findById(31L, "en")
        assertThat(paper.newsletterLink == null).isFalse()
        assertNewsletterLink(paper, "1802", 1, 1, "Ultrafine Particles", "some headline")
    }

    @Test
    fun findingById_populatesNewsLetterWithMostFields() {
        val paper = repo.findById(20L, "en")
        assertThat(paper.newsletterLink == null).isFalse()
        assertNewsletterLink(paper, "1802", 1, 2, "Mortality", null)
    }

    @Test
    fun findingById_populatesNewsLetterWithSomeFields() {
        val paper = repo.findById(39L, "en")
        assertThat(paper.newsletterLink == null).isFalse()
        assertNewsletterLink(paper, "1804", 0, null, null, null)
    }

    private fun assertNewsletterLink(paper: Paper, issue: String, statusId: Int, topicId: Int?, topic: String?, headline: String?) {
        assertThat(paper.newsletterLink.issue).isEqualTo(issue)
        assertThat(paper.newsletterLink.publicationStatusId).isEqualTo(statusId)
        if (topicId != null) assertThat(paper.newsletterLink.topicId).isEqualTo(topicId) else assertThat(paper.newsletterLink.topicId == null).isTrue()
        if (topic != null) assertThat(paper.newsletterLink.topic).isEqualTo(topic) else assertThat(paper.newsletterLink.topic == null).isTrue()
        if (headline != null) assertThat(paper.newsletterLink.headline).isEqualTo(headline) else assertThat(paper.newsletterLink.headline == null).isTrue()
    }

    @Test
    fun isDoiAlreadyAssigned_withDoiInDb_countsOtherPaperAsDuplicate() {
        // it's id 1 that has that doi
        assertThat(repo.isDoiAlreadyAssigned("10.1093/aje/kwu275", 2L)).hasValue("1")
    }

    @Test
    fun isDoiAlreadyAssigned_withDoiInDb_doesNotCountCurrentPaperAsDuplicate() {
        assertThat(repo.isDoiAlreadyAssigned("10.1093/aje/kwu275", 1L)).isNotPresent
    }

    @Test
    fun isDoiAlreadyAssigned_withDoiNotInDb_reportsNoDuplicate() {
        assertThat(repo.isDoiAlreadyAssigned("foobar", 1L)).isNotPresent
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiInDb_countsOtherPaperAsDuplicate() {
        // it's id 1 that has that pmId
        assertThat(repo.isPmIdAlreadyAssigned(25395026, 2L)).hasValue("1")
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiInDb_doesNotCountCurrentPaperAsDuplicate() {
        assertThat(repo.isPmIdAlreadyAssigned(25395026, 1L)).isNotPresent
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiInDb_andWithNewEntityWithNullId_countsAnyRecordFoundWithPmId() {
        assertThat(repo.isPmIdAlreadyAssigned(25395026, null)).isPresent
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiNotInDb_reportsNoDuplicate() {
        assertThat(repo.isPmIdAlreadyAssigned(-1, 1L)).isNotPresent
    }

    companion object {

        private const val TEST_PAPER_ID = 1L
        private const val TEST_FILE_1 = "test file"
        private const val TEST_FILE_2 = "test file 2"
        private const val LC = "en_us"

        private const val ID_PART = ",id=1,createdBy=1,lastModifiedBy=1,created=2016-12-14T14:47:29.431,lastModified=2016-12-14T14:47:29.431,version=1"

        private const val PAPER1_WO_CODE_CLASSES = (
                "Paper[number=1,doi=10.1093/aje/kwu275,pmId=25395026"
                        + ",authors=Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, Samet JM.,firstAuthor=Turner,firstAuthorOverridden=false"
                        + ",title=Interactions Between Cigarette Smoking and Fine Particulate Matter in the Risk of Lung Cancer Mortality in Cancer Prevention Study II."
                        + ",location=Am J Epidemiol. 2014; 180 (12): 1145-1149."
                        + ",publicationYear=2014,goals=Neue Analyse der Daten der amerikanischen Krebspräventions-Kohertenstudie zur Untersuchung, wie gross das kombinierte Krebsrisiko durch Feinstaub ist."
                        + ",population=429'406 Teilnehmer, Frauen und Männer aus 50 Staaten der USA, welche in den Jahren 1982/1983 im Alter von mindestens 30 Jahren für die Krebsvorsorgestudie der amerikanischen Kriegsgesellschaft (ACS) rekrutiert worden waren, in den Jahren 1984, 1986 und 1988 wieder kontaktiert worden waren, und seit 1989 mit dem nationalen Sterberegister auf ihr Überleben kontaktiert wurden. Nicht in diese Analyse einbezogen wurden Exrauchende und Pfeifen- oder Zigarrenraucher. USA.,populationPlace=,populationParticipants=,populationDuration=,exposurePollutant=,exposureAssessment=,methods=Da nur bis 1998 individuelle Informationen über das Rauchverhalten vorlagen, wurden nur die ersten 6 Studienjahre in diese Analyse einbzeogen. Die Abschätzung der Belastung mit Feinstaub wurde mit Landnutzungsmodellen für die geocodierten Adressen bei Studieneintritt vorgenommen, welche sich auf Monatsmittelwerte von PM2.5 der Jahre 1999-2004 von 1464 Messstationen abstützten, unter der Annahme, dass die Feinstaubbelastungen über die Jahre eng korreliert seien. Mit proportionalen Hazard-Modellen nach Cox, stratifiziert für Alter, Geschlecht und Rasse wurde das Überleben bzw. die Sterblichkeit an Lungenkrebs in den ersten 6 Jahren in Abhängigkeit der PM2.5-Belastung in verschiedenen Kategorien (über/unter der 50 Perzentile, über der 66. vs. unter der 33. Perzentile, sowie über der 75. vs. unter der 25. Perzentile) und in Abhängigkeit von Rauchen/nicht Rauchen modelliert. Einbezogen wurden folgende invidivuellen Faktoren: Schulbildung, Zivilstand, BMI, Passivrauchen, Ernährung, Alkoholkonsum und berufliche Belastung. Die Effektmodifikation bezüglich Lungenkrebsterblichkeit wurde mit drei Grössen untersucht: das relative zusätzliche Risiko durch die Interatkion (RERI), der der Interaktion anrechenbare Teil des Risikos (AP) und der Synergie-Index (SI). Lungenkrebs, Kohortenstudie, Statistik, epidemiologische Methoden. ACS-Studie. USA.,methodStudyDesign=,methodOutcome=,methodStatistics=,methodConfounders=,result=In 2'509'717 Personen-Jahren der Nachkontrolle ereigneten sich 1921 Todesfälle an Lungenkrebs. Die geschätzte Feinstaubbelastung lag im Durchschnitt bei 12.6 SD 2.85 µg PM2.5/m3, mit der 25. und 75. Perzentile bei 10.59 und 14.44 µg PM2.5/m3. Raucher hatten im Vergleich zu Nichtrauchern ein 13.5 fach erhöhtes Risiko (95%CI 10.2-17.9), an Lungenkrebs zu sterben, wenn ihre PM2.5-Belastung gering war, d.h. unter der 25. Perzentile lag. Nichtraucher hatten ein 1.28 faches Risiko (0.92-1.78), wenn ihre Belastung über der 75. Perzentile der PM2.5-Belastung lag, im Vergleich zu Nichtrauchern mit geringer Belastung. Raucher hatten ein 16 faches Risiko (12.1-21.1), an Lungenkrebs zu sterben, wenn ihre Feinstaubbelastung über der 75. Perzentile lag. Das zusätzliche relative Risiko durch die Interaktion (RERI) für die Kombination von Rauchen und schlechter Luft betrug 2.19 (-0.10;+4.83). Der Risikoanteil, der dem Kombinationseffekt angerechnet werden kann, betrug 14%, der Synergie-Index 1.17. Die Autoren schliessen daraus, dass die Folgen von Rauchen und Luftverschmutzung stärker zusammenwiren als nur additiv. Auch wenn die Lungenkrebfälle am stärksten durch einen Rückgang des Rauchens abnehmen, kann ein solcher Rückgang mit einer Verbesserung der Luftqualität stärker ausfallen als mit einer der beiden Massnahmen allein."
                        + ",resultExposureRange=,resultEffectEstimate=,resultMeasuredOutcome=,conclusion=<null>,comment=Kommentar von Panagiotou AO, Wacholder S: How Big Is That Interaction (in My Community)-and I. Which Direction? Am. J. Epidemiol. 2014 180: 1150-1158."
                        + ",intern=,originalAbstract=<null>,mainCodeOfCodeclass1=1F,newsletterLink=<null>,attachments=[]")
    }
}
