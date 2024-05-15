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
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldNotContain
import org.amshove.kluent.shouldStartWith
import org.jooq.DSLContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.testcontainers.junit.jupiter.Testcontainers

@JooqTest
@Testcontainers
@Suppress("TooManyFunctions", "FunctionName", "MagicNumber", "SpellCheckingInspection")
internal open class JooqPaperRepoIntegrationTest {

    @Autowired
    private lateinit var dsl: DSLContext

    @Autowired
    private lateinit var repo: JooqPaperRepo

    @Test
    fun findingAll() {
        val papers = repo.findAll()
        papers.sortBy { it.id }

        papers shouldHaveSize 36
        papers[0].id shouldBeEqualTo 1
        papers[1].id shouldBeEqualTo 2
        papers[2].id shouldBeEqualTo 3
        papers[3].id shouldBeEqualTo 4
        papers[4].id shouldBeEqualTo 10
        papers[13].id shouldBeEqualTo 19
        papers[22].id shouldBeEqualTo 28
    }

    @Test
    fun findingById_withExistingId_returnsEntity() {
        val id: Long = 4
        val paper = repo.findById(id) ?: fail { "Unable to find paper" }
        paper.id shouldBeEqualTo id
        paper.authors shouldBeEqualTo "Kutlar Joss M, Joss U."
    }

    @Test
    fun findingById_withNonExistingId_returnsNull() {
        repo.findById(-1L).shouldBeNull()
    }

    @Test
    fun addingRecord_savesRecordAndRefreshesId() {
        val p = makeMinimalPaper()
        p.id.shouldBeNull()

        val saved = repo.add(p) ?: fail { "Unable to add paper" }
        saved.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        saved.authors shouldBeEqualTo "a"
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
        val paper = repo.add(makeMinimalPaper()) ?: fail { "Unable to add paper" }
        paper.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        val id = paper.id ?: error("id must no be null now")
        paper.authors shouldBeEqualTo "a"

        paper.authors = "b"
        repo.update(paper)
        paper.id as Long shouldBeEqualTo id

        val newCopy = repo.findById(id) ?: fail { "Unable to find paper" }
        newCopy shouldNotBeEqualTo paper
        newCopy.id shouldBeEqualTo id
        newCopy.authors shouldBeEqualTo "b"
    }

    @Test
    fun savingAssociatedEntitiesOf_withCodes() {
        val paper = repo.add(makeMinimalPaper()) ?: fail { "Unable to add paper" }
        paper.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        val id = paper.id ?: error("id must no be null now")
        paper.authors shouldBeEqualTo "a"

        val cr = dsl.selectFrom(Code.CODE).limit(1).fetchOne()
            ?: fail("unable to fetch record")
        val ccr = dsl
            .selectFrom(CodeClass.CODE_CLASS)
            .where(CodeClass.CODE_CLASS.ID.eq(cr.codeClassId))
            .fetchOne() ?: fail("Unable to fetch record")
        val codeClass = ch.difty.scipamato.core.entity.CodeClass(ccr.id, "", "")
        val code = ch.difty.scipamato.core.entity.Code(
            cr.code, "", "", true,
            codeClass.id!!, codeClass.name, codeClass.description, cr.sort, null, null, null, null, null
        )
        paper.addCode(code)

        repo.update(paper) ?: fail { "Unable to add paper" }
        paper.id as Long shouldBeEqualTo id

        val newCopy = repo.findById(id) ?: fail { "Unable to find paper" }
        newCopy shouldNotBeEqualTo paper
        newCopy.id shouldBeEqualTo id
        newCopy.codes.map { it.code } shouldContainAll listOf(code.code)
    }

    @Test
    fun savingAssociatedEntitiesOf_withNewsletterLink() {
        val paper = repo.add(makeMinimalPaper()) ?: fail { "Unable to add paper" }
        val id = paper.id ?: error("id must no be null now")
        val newsletterLink = Paper.NewsletterLink(2, "whatever", 1, 1, "topic1", "hl")

        paper.newsletterLink = newsletterLink

        repo.update(paper)
        paper.id as Long shouldBeEqualTo id

        val newCopy = repo.findById(id) ?: fail { "Unable to find paper" }
        newCopy shouldNotBeEqualTo paper
        newCopy.id shouldBeEqualTo id
        newCopy.newsletterLink.issue shouldBeEqualTo "1804"
    }

    @Test
    fun deletingRecord() {
        val paper = repo.add(makeMinimalPaper()) ?: fail { "Unable to add paper" }
        paper.id?.shouldBeGreaterThan(MAX_ID_PREPOPULATED)
        val id = paper.id ?: error("id must no be null now")
        paper.authors shouldBeEqualTo "a"

        val deleted = repo.delete(id, paper.version)
        deleted.id shouldBeEqualTo id

        repo.findById(id).shouldBeNull()
    }

    @Test
    @Disabled("TODO Need to fix Paper.toString first")
    fun findingById_forPaper1InGerman() {
        val paper = repo.findById(1L)
        paper.toString() shouldBeEqualTo
            PAPER1_WO_CODE_CLASSES +
            ",codes=[" +
            "codesOfClass1=[" +
            "Code[code=1F,name=Feinstaub, Partikel,comment=<null>,internal=false,codeClass=CodeClass[id=1],sort=1" +
            ",createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821" +
            ",version=1]" +
            "]," +
            "codesOfClass2=[" +
            "Code[code=2N,name=Übrige Länder,comment=<null>,internal=false,codeClass=CodeClass[id=2],sort=2" +
            ",createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821" +
            ",version=1]" +
            "]," +
            "codesOfClass3=[" +
            "Code[code=3C,name=Erwachsene (alle),comment=<null>,internal=false,codeClass=CodeClass[id=3],sort=3" +
            ",createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821," +
            "lastModified=2017-01-01T08:01:33.821,version=1]" +
            "]," +
            "codesOfClass4=[" +
            "Code[code=4G,name=Krebs,comment=<null>,internal=false,codeClass=CodeClass[id=4],sort=7,createdBy=1" +
            ",lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]" +
            "]," +
            "codesOfClass5=[" +
            "Code[code=5H,name=Kohortenstudie,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=7" +
            ",createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821," +
            "lastModified=2017-01-01T08:01:33.821,version=1], " +
            "Code[code=5S,name=Statistik,comment=<null>,internal=false,codeClass=CodeClass[id=5],sort=10," +
            "createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821," +
            "version=1]" +
            "]," +
            "codesOfClass6=[" +
            "Code[code=6M,name=Mensch,comment=<null>,internal=false,codeClass=CodeClass[id=6],sort=1,createdBy=1" +
            ",lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821,version=1]" +
            "]," +
            "codesOfClass7=" +
            "[Code[code=7L,name=Langfristig,comment=<null>,internal=false,codeClass=CodeClass[id=7],sort=2" +
            ",createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821" +
            ",version=1]" +
            "]," +
            "codesOfClass8=[" +
            "Code[code=8O,name=Aussenluft,comment=<null>,internal=false,codeClass=CodeClass[id=8],sort=2," +
            "createdBy=1,lastModifiedBy=1,created=2017-01-01T08:01:33.821,lastModified=2017-01-01T08:01:33.821" +
            ",version=1]" +
            "]" +
            "]" +
            ID_PART +
            "]"
        // @formatter:on
    }

    @Test
    fun findingByIds_returnsRecordForEveryIdExisting() {
        val papers = repo.findByIds(listOf(1L, 2L, 3L, 10L, -17L))
        papers.map { it.id } shouldContainAll listOf(1L, 2L, 3L, 10L)

        // codes not enriched
        papers[0].codes.shouldBeEmpty()
    }

    @Test
    fun findingByIds_returnsEmptyListForEmptyIdList() {
        repo.findByIds(emptyList()).shouldBeEmpty()
    }

    @Test
    fun findingWithCodesByIds_returnsRecordForEveryIdExisting() {
        val papers = repo.findWithCodesByIds(listOf(1L, 2L, 3L, 10L, -17L), LC)
        papers.map { it.id } shouldContainAll listOf(1L, 2L, 3L, 10L)

        // codes are present
        papers[0].codes.shouldNotBeEmpty()
    }

    @Test
    fun findingPapersByPmIds_withThreeValidPmIds_returnsThreePapers() {
        val papers = repo.findByPmIds(listOf(20335815, 27128166, 25104428), LC)
        papers shouldHaveSize 3
        papers.map { it.pmId } shouldContainSame listOf(20335815, 27128166, 25104428)
    }

    @Test
    fun findingPapersByPmIds_withInvalidPmIds_returnsEmptyList() {
        repo.findByPmIds(listOf(-20335815), LC).shouldBeEmpty()
    }

    @Test
    fun findingPapersByPmIds_hasCodesEnriched() {
        val papers = repo.findByPmIds(listOf(20335815), LC)
        papers[0].codes.shouldNotBeEmpty()
    }

    @Test
    fun findingExistingPmIdsOutOf_withThreeValidPmIds_returnsThreePMIDs() {
        val pmids = repo.findExistingPmIdsOutOf(listOf(20335815, 27128166, 25104428))
        pmids shouldHaveSize 3
        pmids shouldContainSame listOf(20335815, 27128166, 25104428)
    }

    @Test
    fun findingExistingPmIdsOutOf_withInvalidPmIds_returnsEmptyList() {
        repo.findExistingPmIdsOutOf(listOf(-20335815)).shouldBeEmpty()
    }

    @Test
    fun findingPapersByNumbers_withThreeValidNumbers_returnsThreePapers() {
        val papers = repo.findByNumbers(listOf(1L, 2L, 3L), LC)
        papers shouldHaveSize 3
        papers.map { it.number } shouldContainSame listOf(1L, 2L, 3L)
    }

    @Test
    fun findingPapersByNumbers_withInvalidNumbers_returnsEmptyList() {
        repo.findByNumbers(listOf(-1L), LC).shouldBeEmpty()
    }

    @Test
    fun findingPapersByNumber_hasCodesEnriched() {
        val papers = repo.findByNumbers(listOf(1L), LC)
        papers[0].codes.shouldNotBeEmpty()
    }

    @Test
    fun findingBySearchOrder() {
        val searchOrder = SearchOrder()
        val sc = SearchCondition()
        sc.authors = "kutlar"
        searchOrder.add(sc)
        val papers = repo.findBySearchOrder(searchOrder, LC)
        papers.shouldNotBeEmpty()
    }

    @Test
    fun findingPageBySearchOrder() {
        val searchOrder = SearchOrder()
        val sc = SearchCondition()
        sc.authors = "kutlar"
        searchOrder.add(sc)
        repo.findPageBySearchOrder(searchOrder, PaginationRequest(Direction.ASC, "authors"), LC).shouldNotBeEmpty()
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_findsFirstGapStartingAboveMinimumValue() {
        val number = repo.findLowestFreeNumberStartingFrom(0L)
        number shouldBeEqualTo 5L
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_withMinimumInMultiNumberGap_ignoresRemainingNumbersOfSameGap() {
        val number = repo.findLowestFreeNumberStartingFrom(5L)
        number shouldBeGreaterOrEqualTo 42L
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_withMinimumBeyondLastGap_findsNextFreeNumber() {
        val number = repo.findLowestFreeNumberStartingFrom(30)
        number shouldBeGreaterOrEqualTo 42L
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_withMinimumBeyondNextFreeNumber_findsMinimumLeavingGap() {
        val number = repo.findLowestFreeNumberStartingFrom(100L)
        number shouldBeGreaterOrEqualTo 100L
    }

    @Test
    fun findingPageOfIdsByFilter() {
        val filter = PaperFilter()
        filter.authorMask = "Kutlar"
        repo.findPageOfIdsByFilter(filter, PaginationRequest(Direction.ASC, "authors")) shouldContainAll listOf(4L)
    }

    @Test
    fun findingPageOfIdsBySearchOrder() {
        val searchOrder = SearchOrder()
        val sc = SearchCondition()
        sc.authors = "kutlar"
        searchOrder.add(sc)
        repo.findPageOfIdsBySearchOrder(searchOrder, PaginationRequest(Direction.ASC, "authors")) shouldContainSame
            listOf(4L)
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

    @Suppress("SameParameterValue")
    private fun ensureRecordNotPresent(searchOrderId: Long, paperId: Long) {
        assertExclusionCount(searchOrderId, paperId, 0)
    }

    private fun assertExclusionCount(searchOrderId: Long, paperId: Long, count: Int) {
        dsl.selectCount()
            .from(SearchExclusion.SEARCH_EXCLUSION)
            .where(SearchExclusion.SEARCH_EXCLUSION.SEARCH_ORDER_ID.eq(searchOrderId))
            .and(SearchExclusion.SEARCH_EXCLUSION.PAPER_ID.eq(paperId))
            .fetchOne(0, Int::class.javaPrimitiveType) shouldBeEqualTo count
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

        results shouldHaveSize 2
        val saved = results[0]

        saved.name shouldBeEqualTo pa1.name
        saved.content.shouldBeNull()
        saved.size shouldBeEqualTo content1.length.toLong()
        saved.contentType shouldBeEqualTo "application/pdf"
        saved.created.toString() shouldBeEqualTo "2016-12-09T06:02:13"
        saved.lastModified.toString() shouldBeEqualTo "2016-12-09T06:02:13"
    }

    private fun newPaperAttachment(name: String, content: String): PaperAttachment {
        return PaperAttachment(
            null,
            TEST_PAPER_ID,
            name,
            content.toByteArray(),
            "application/pdf",
            content.length.toLong()
        )
    }

    @Test
    fun savingAttachment_whenNotExisting_insertsIntoDb() {
        val content = "foo"
        val pa = newPaperAttachment(TEST_FILE_1, content)

        val p = repo.saveAttachment(pa) ?: fail { "Unable to save attachments" }
        val saved = dsl
            .select()
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(PaperAttachment::class.java) ?: fail("Unable to fetch record")

        p.attachments.map { it.id } shouldContainAll listOf(saved.id)

        saved.name shouldBeEqualTo pa.name
        String(saved.content) shouldBeEqualTo content
        saved.size shouldBeEqualTo content.length.toLong()
        saved.contentType shouldBeEqualTo "application/pdf"
        saved.created.toString() shouldBeEqualTo "2016-12-09T06:02:13"
        saved.lastModified.toString() shouldBeEqualTo "2016-12-09T06:02:13"
    }

    @Test
    fun savingAttachment_whenExisted_performsUpdate() {
        val content2 = "bar"
        val pa1 = newPaperAttachment(TEST_FILE_1, "foo")
        val pa2 = newPaperAttachment(TEST_FILE_1, content2)
        pa1.paperId shouldBeEqualTo pa2.paperId
        pa1.name shouldBeEqualTo pa2.name

        repo.saveAttachment(pa1)
        repo.saveAttachment(pa2)

        val saved2 = dsl
            .select()
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(PaperAttachment::class.java) ?: fail("unable to fetch record")

        saved2.name shouldBeEqualTo pa1.name
        saved2.version shouldBeEqualTo 2
        String(saved2.content) shouldBeEqualTo content2
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
            .fetchOneInto(Int::class.java) ?: fail("unable to fetch id")
        val attachment = repo.loadAttachmentWithContentBy(id) ?: fail { "Unable to load attachments" }
        attachment.content.shouldNotBeNull()
        String(attachment.content) shouldBeEqualTo content1
    }

    @Test
    fun deletingAttachment_deletes() {
        repo.saveAttachment(newPaperAttachment(TEST_FILE_1, "foo"))
        val id = dsl
            .select(PAPER_ATTACHMENT.ID)
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID))
            .fetchOneInto(Int::class.java) ?: fail { "id must not be null" }
        val p = repo.deleteAttachment(id) ?: fail { "Unable to delete attachments" }
        p.attachments.map { it.id } shouldNotContain id
        dsl.select(PAPER_ATTACHMENT.ID)
            .from(PAPER_ATTACHMENT)
            .where(PAPER_ATTACHMENT.PAPER_ID.eq(TEST_PAPER_ID)).fetch().shouldBeEmpty()
    }

    /**
     * Verify the rollback is occurring, also that the JooqExceptionTranslator is
     * doing it's job to translate jooq specific exceptions into spring exceptions.
     */
    @Test
    fun testDeclarativeTransaction() {
        val paper = repo.findById(1L) ?: fail { "Unable to find paper" }
        val rollback = try {
            paper.number = null
            repo.update(paper)
            fail { "Should have thrown exception due to null value on non-null column" }
        } catch (dae: org.jooq.exception.DataAccessException) {
            fail { "JooqExceptionTranslator did not translate the jooqException into a spring exception" }
        } catch (dae: DataAccessException) {
            dae shouldBeInstanceOf DataIntegrityViolationException::class
            dae.message?.shouldStartWith("""jOOQ; SQL [update "public"."paper" set "number" = ?""")
            true
        }

        rollback.shouldBeTrue()
    }

    @Test
    fun findingByFilter_filteringByNewsletterId() {
        val filter = PaperFilter()
        filter.newsletterId = 1
        val papers = repo.findPageByFilter(filter, PaginationRequest(0, 10))
        papers shouldHaveSize 5
        papers.map { it.id } shouldContain 31L
    }

    @Test
    fun findingById_populatesNewsLetterWithAllFields() {
        val paper = repo.findById(31L, "en") ?: fail { "Unable to find paper" }
        paper.newsletterLink.shouldNotBeNull()
        assertNewsletterLink(paper, "1802", 1, 1, "Ultrafine Particles", "some headline")
    }

    @Test
    fun findingById_populatesNewsLetterWithMostFields() {
        val paper = repo.findById(20L, "en") ?: fail { "Unable to find paper" }
        paper.newsletterLink.shouldNotBeNull()
        assertNewsletterLink(paper, "1802", 1, 2, "Mortality", null)
    }

    @Test
    fun findingById_populatesNewsLetterWithSomeFields() {
        val paper = repo.findById(39L, "en") ?: fail { "Unable to find paper" }
        paper.newsletterLink.shouldNotBeNull()
        assertNewsletterLink(paper, "1804", 0, null, null, null)
    }

    @Suppress("LongParameterList")
    private fun assertNewsletterLink(
        paper: Paper,
        issue: String,
        statusId: Int,
        topicId: Int?,
        topic: String?,
        headline: String?
    ) {
        paper.newsletterLink.issue shouldBeEqualTo issue
        paper.newsletterLink.publicationStatusId shouldBeEqualTo statusId
        if (topicId != null)
            paper.newsletterLink.topicId shouldBeEqualTo topicId
        else
            paper.newsletterLink.topicId.shouldBeNull()

        if (topic != null)
            paper.newsletterLink.topic shouldBeEqualTo topic
        else
            paper.newsletterLink.topic.shouldBeNull()

        if (headline != null)
            paper.newsletterLink.headline shouldBeEqualTo headline
        else
            paper.newsletterLink.headline.shouldBeNull()
    }

    @Test
    fun isDoiAlreadyAssigned_withDoiInDb_countsOtherPaperAsDuplicate() {
        // it's id 1 that has that doi
        repo.isDoiAlreadyAssigned("10.1093/aje/kwu275", 2L).get() shouldBeEqualTo "1"
    }

    @Test
    fun isDoiAlreadyAssigned_withDoiInDb_doesNotCountCurrentPaperAsDuplicate() {
        repo.isDoiAlreadyAssigned("10.1093/aje/kwu275", 1L).isPresent.shouldBeFalse()
    }

    @Test
    fun isDoiAlreadyAssigned_withDoiNotInDb_reportsNoDuplicate() {
        repo.isDoiAlreadyAssigned("foobar", 1L).isPresent.shouldBeFalse()
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiInDb_countsOtherPaperAsDuplicate() {
        // it's id 1 that has that pmId
        repo.isPmIdAlreadyAssigned(25395026, 2L).get() shouldBeEqualTo "1"
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiInDb_doesNotCountCurrentPaperAsDuplicate() {
        repo.isPmIdAlreadyAssigned(25395026, 1L).isPresent.shouldBeFalse()
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiInDb_andWithNewEntityWithNullId_countsAnyRecordFoundWithPmId() {
        repo.isPmIdAlreadyAssigned(25395026, null).isPresent.shouldBeTrue()
    }

    @Test
    fun isPmIdAlreadyAssigned_withDoiNotInDb_reportsNoDuplicate() {
        repo.isPmIdAlreadyAssigned(-1, 1L).isPresent.shouldBeFalse()
    }

    companion object {

        private const val TEST_PAPER_ID = 1L
        private const val TEST_FILE_1 = "test file"
        private const val TEST_FILE_2 = "test file 2"
        private const val LC = "en_us"

        private const val ID_PART =
            ",id=1,createdBy=1,lastModifiedBy=1,created=2016-12-14T14:47:29.431," +
                "lastModified=2016-12-14T14:47:29.431,version=1"

        private const val PAPER1_WO_CODE_CLASSES = (
            "Paper[number=1,doi=10.1093/aje/kwu275,pmId=25395026" +
                ",authors=Turner MC, Cohen A, Jerrett M, Gapstur SM, Diver WR, Pope CA 3rd, Krewski D, Beckerman BS, " +
                "Samet JM.,firstAuthor=Turner,firstAuthorOverridden=false" +
                ",title=Interactions Between Cigarette Smoking and Fine Particulate Matter in the Risk of Lung " +
                "Cancer Mortality in Cancer Prevention Study II." +
                ",location=Am J Epidemiol. 2014; 180 (12): 1145-1149." +
                ",publicationYear=2014,goals=Neue Analyse der Daten der amerikanischen Krebspräventions" +
                "-Kohertenstudie zur Untersuchung, wie gross das kombinierte Krebsrisiko durch Feinstaub ist." +
                ",population=429'406 Teilnehmer, Frauen und Männer aus 50 Staaten der USA, welche in den Jahren " +
                "1982/1983 im Alter von mindestens 30 Jahren für die Krebsvorsorgestudie der amerikanischen " +
                "Kriegsgesellschaft (ACS) rekrutiert worden waren, in den Jahren 1984, 1986 und 1988 wieder " +
                "kontaktiert worden waren, und seit 1989 mit dem nationalen Sterberegister auf ihr Überleben " +
                "kontaktiert wurden. Nicht in diese Analyse einbezogen wurden Exrauchende und Pfeifen- oder " +
                "Zigarrenraucher. USA.,populationPlace=,populationParticipants=,populationDuration=" +
                ",exposurePollutant=,exposureAssessment=,methods=Da nur bis 1998 individuelle Informationen über das " +
                "Rauchverhalten vorlagen, wurden nur die ersten 6 Studienjahre in diese Analyse einbzeogen. Die " +
                "Abschätzung der Belastung mit Feinstaub wurde mit Landnutzungsmodellen für die geocodierten " +
                "Adressen bei Studieneintritt vorgenommen, welche sich auf Monatsmittelwerte von PM2.5 der Jahre " +
                "1999-2004 von 1464 Messstationen abstützten, unter der Annahme, dass die Feinstaubbelastungen über" +
                " die Jahre eng korreliert seien. Mit proportionalen Hazard-Modellen nach Cox, stratifiziert für " +
                "Alter, Geschlecht und Rasse wurde das Überleben bzw. die Sterblichkeit an Lungenkrebs in den ersten " +
                "6 Jahren in Abhängigkeit der PM2.5-Belastung in verschiedenen Kategorien (über/unter der 50 " +
                "Perzentile, über der 66. vs. unter der 33. Perzentile, sowie über der 75. vs. unter der 25. " +
                "Perzentile) und in Abhängigkeit von Rauchen/nicht Rauchen modelliert. Einbezogen wurden folgende " +
                "invidivuellen Faktoren: Schulbildung, Zivilstand, BMI, Passivrauchen, Ernährung, Alkoholkonsum und " +
                "berufliche Belastung. Die Effektmodifikation bezüglich Lungenkrebsterblichkeit wurde mit drei " +
                "Grössen untersucht: das relative zusätzliche Risiko durch die Interatkion (RERI), der der " +
                "Interaktion anrechenbare Teil des Risikos (AP) und der Synergie-Index (SI). Lungenkrebs, " +
                "Kohortenstudie, Statistik, epidemiologische Methoden. ACS-Studie. USA.,methodStudyDesign=," +
                "methodOutcome=,methodStatistics=,methodConfounders=,result=In 2'509'717 Personen-Jahren der " +
                "Nachkontrolle ereigneten sich 1921 Todesfälle an Lungenkrebs. Die geschätzte Feinstaubbelastung " +
                "lag im Durchschnitt bei 12.6 SD 2.85 µg PM2.5/m3, mit der 25. und 75. Perzentile bei 10.59 und " +
                "14.44 µg PM2.5/m3. Raucher hatten im Vergleich zu Nichtrauchern ein 13.5 fach erhöhtes Risiko " +
                "(95%CI 10.2-17.9), an Lungenkrebs zu sterben, wenn ihre PM2.5-Belastung gering war, d.h. unter " +
                "der 25. Perzentile lag. Nichtraucher hatten ein 1.28 faches Risiko (0.92-1.78), wenn ihre Belastung " +
                "über der 75. Perzentile der PM2.5-Belastung lag, im Vergleich zu Nichtrauchern mit geringer " +
                "Belastung. Raucher hatten ein 16 faches Risiko (12.1-21.1), an Lungenkrebs zu sterben, wenn ihre " +
                "Feinstaubbelastung über der 75. Perzentile lag. Das zusätzliche relative Risiko durch die " +
                "Interaktion (RERI) für die Kombination von Rauchen und schlechter Luft betrug 2.19 (-0.10;+4.83). " +
                "Der Risikoanteil, der dem Kombinationseffekt angerechnet werden kann, betrug 14%, der " +
                "Synergie-Index 1.17. Die Autoren schliessen daraus, dass die Folgen von Rauchen und " +
                "Luftverschmutzung stärker zusammenwiren als nur additiv. Auch wenn die Lungenkrebfälle am " +
                "stärksten durch einen Rückgang des Rauchens abnehmen, kann ein solcher Rückgang mit einer " +
                "Verbesserung der Luftqualität stärker ausfallen als mit einer der beiden Massnahmen allein." +
                ",resultExposureRange=,resultEffectEstimate=,resultMeasuredOutcome=,conclusion=<null>," +
                "comment=Kommentar von Panagiotou AO, Wacholder S: How Big Is That Interaction (in My " +
                "Community)-and I. Which Direction? Am. J. Epidemiol. 2014 180: 1150-1158." +
                ",intern=,originalAbstract=<null>,mainCodeOfCodeclass1=1F,newsletterLink=<null>,attachments=[]"
            )
    }
}
