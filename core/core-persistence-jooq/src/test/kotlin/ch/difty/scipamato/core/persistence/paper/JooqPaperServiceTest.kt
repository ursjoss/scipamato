@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.core.persistence.paper

import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.core.entity.Paper
import ch.difty.scipamato.core.entity.PaperAttachment
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.SearchOrder
import ch.difty.scipamato.core.persistence.AbstractServiceTest
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRepository
import ch.difty.scipamato.core.pubmed.AbstractPubmedArticleFacade
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade
import ch.difty.scipamato.core.pubmed.api.Article
import ch.difty.scipamato.core.pubmed.api.ArticleTitle
import ch.difty.scipamato.core.pubmed.api.Journal
import ch.difty.scipamato.core.pubmed.api.JournalIssue
import ch.difty.scipamato.core.pubmed.api.MedlineCitation
import ch.difty.scipamato.core.pubmed.api.MedlineJournalInfo
import ch.difty.scipamato.core.pubmed.api.PMID
import ch.difty.scipamato.core.pubmed.api.PubDate
import ch.difty.scipamato.core.pubmed.api.PubmedArticle
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainSame
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

internal class JooqPaperServiceTest : AbstractServiceTest<Long, Paper, PaperRepository>() {

    override val repo = mockk<PaperRepository>(relaxed = true) {
        every { delete(any(), any()) } returns entity
    }
    private val newsletterRepoMock = mockk<NewsletterRepository>()
    private val filterMock = mockk<PaperFilter>()
    private val searchOrderMock = mockk<SearchOrder>()
    private val paginationContextMock = mockk<PaginationContext>()
    override val entity = mockk<Paper>(relaxed = true)
    private val paperMock2 = mockk<Paper>(relaxed = true)
    private val paperMock3 = mockk<Paper>(relaxed = true)
    private val attachmentMock = mockk<PaperAttachment>()

    private val service: JooqPaperService = JooqPaperService(repo, newsletterRepoMock, userRepoMock)

    private val papers = mutableListOf(entity, entity)
    private val articles = mutableListOf<PubmedArticleFacade>()

    public override fun specificTearDown() {
        confirmVerified(repo, filterMock, searchOrderMock, paginationContextMock, entity, paperMock2)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7L
        every { repo.findById(id) } returns entity
        auditFixture()

        val optPaper = service.findById(id)
        optPaper.isPresent.shouldBeTrue()
        optPaper.get() shouldBeEqualTo entity

        verify { repo.findById(id) }
        verify { entity == entity }
        verify { entity.toString() }

        verifyAudit(1)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7L
        every { repo.findById(id) } returns null
        service.findById(id).isPresent.shouldBeFalse()
        verify { repo.findById(id) }
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        every { repo.findPageByFilter(filterMock, paginationContextMock) } returns papers
        auditFixture()
        service.findPageByFilter(filterMock, paginationContextMock) shouldBeEqualTo papers
        verify { repo.findPageByFilter(filterMock, paginationContextMock) }
        verify { entity.toString() }
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        every { repo.countByFilter(filterMock) } returns 3
        service.countByFilter(filterMock) shouldBeEqualTo 3
        verify { repo.countByFilter(filterMock) }
    }

    @Test
    fun savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        every { entity.id } returns null
        every { repo.add(entity) } returns entity
        auditFixture()
        service.saveOrUpdate(entity) shouldBeEqualTo entity
        verify { repo.add(entity) }
        verify { entity.id }
        verify { entity == entity }
        verify { entity.toString() }
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        every { entity.id } returns 17L
        every { repo.update(entity) } returns entity
        auditFixture()
        service.saveOrUpdate(entity) shouldBeEqualTo entity
        verify { repo.update(entity) }
        verify { entity.id }
        verify { entity == entity }
        verify { entity.toString() }
        verifyAudit(1)
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify(exactly = 0) { repo.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        every { entity.id } returns null

        service.remove(entity)

        verify { entity.id }
        verify(exactly = 0) { repo.delete(any(), any()) }
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        every { entity.id } returns 3L
        every { entity.version } returns 17

        service.remove(entity)

        verify(exactly = 2) { entity.id }
        verify(exactly = 1) { entity.version }
        verify(exactly = 1) { repo.delete(3L, 17) }
    }

    @Test
    fun findingBySearchOrder_delegatesToRepo() {
        every { repo.findBySearchOrder(searchOrderMock, LC) } returns papers
        service.findBySearchOrder(searchOrderMock, LC) shouldContainSame papers
        verify { repo.findBySearchOrder(searchOrderMock, LC) }
        verify { entity == entity }
    }

    @Test
    fun findingPagedBySearchOrder_delegatesToRepo() {
        every { repo.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC) } returns papers
        service.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC) shouldBeEqualTo papers
        verify { repo.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC) }
        verify { entity.toString() }
    }

    @Test
    fun countingBySearchOrder_delegatesToRepo() {
        every { repo.countBySearchOrder(searchOrderMock) } returns 2
        service.countBySearchOrder(searchOrderMock) shouldBeEqualTo 2
        verify { repo.countBySearchOrder(searchOrderMock) }
    }

    @Test
    fun dumpingEmptyListOfArticles_logsWarnMessage() {
        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        sr.infoMessages.shouldBeEmpty()
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()
    }

    @Test
    fun dumpingSingleArticle_whichAlreadyExists_doesNotSave() {
        val pmIdValue = 23193287
        val pa = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa))

        // existing papers
        every { repo.findExistingPmIdsOutOf(listOf(pmIdValue)) } returns listOf(pmIdValue)

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        sr.infoMessages.shouldBeEmpty()
        sr.warnMessages shouldContain "PMID $pmIdValue"
        sr.errorMessages.shouldBeEmpty()

        verify { repo.findExistingPmIdsOutOf(listOf(pmIdValue)) }
    }

    @Test
    fun dumpingSingleNewArticle_saves() {
        val pmIdValue = 23193287
        val pa = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa))
        articles[0].pmId.shouldNotBeNull()

        every { repo.findExistingPmIdsOutOf(listOf(pmIdValue)) } returns emptyList()
        every { repo.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER) } returns 17L

        every { repo.add(any()) } returns paperMock2
        every { paperMock2.id } returns 27L
        every { paperMock2.pmId } returns pmIdValue

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        sr.infoMessages shouldContain "PMID $pmIdValue (id 27)"
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()

        verify { repo.findExistingPmIdsOutOf(listOf(pmIdValue)) }
        verify { repo.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER) }
        verify { repo.add(any()) }
        verify { paperMock2.id }
        verify { paperMock2.pmId }
    }

    @Test
    fun dumpingSingleNewArticleWithNullPmId_doesNotTryToSave() {
        val pmIdValue = 23193287
        val pa = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa))
        (articles[0] as AbstractPubmedArticleFacade).pmId = null

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        sr.infoMessages.shouldBeEmpty()
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()

        verify(exactly = 0) { repo.findExistingPmIdsOutOf(listOf(pmIdValue)) }
    }

    @Test
    fun dumpingTwoNewArticleOneOfWhichWithNullPmId_savesOnlyOther() {
        val pmIdValue = 23193287
        val pa1 = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa1))
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(newPubmedArticle(0)))
        articles[1].pmId shouldBeEqualTo "0"
        (articles[1] as AbstractPubmedArticleFacade).pmId = null

        every { repo.findExistingPmIdsOutOf(listOf(pmIdValue)) } returns emptyList()
        every { repo.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER) } returns 17L

        every { repo.add(any()) } returns paperMock2
        every { paperMock2.id } returns 27L
        every { paperMock2.pmId } returns pmIdValue

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        sr.infoMessages shouldContainSame listOf("PMID $pmIdValue (id 27)")
        sr.warnMessages.shouldBeEmpty()
        sr.errorMessages.shouldBeEmpty()

        verify { repo.findExistingPmIdsOutOf(listOf(pmIdValue)) }
        verify { repo.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER) }
        verify { repo.add(any()) }
        verify { paperMock2.id }
        verify { paperMock2.pmId }
    }

    private fun newPubmedArticle(pmIdValue: Int?): PubmedArticle {
        val pa = PubmedArticle()
        val mc = MedlineCitation()
        val mji = MedlineJournalInfo()
        mc.medlineJournalInfo = mji
        val pmId = PMID()
        pmId.setvalue(pmIdValue.toString())
        mc.pmid = pmId
        val a = Article()
        val at = ArticleTitle()
        a.articleTitle = at
        val j = Journal()
        val ji = JournalIssue()
        val pd = PubDate()
        ji.pubDate = pd
        j.journalIssue = ji
        a.journal = j
        mc.article = a
        pa.medlineCitation = mc
        return pa
    }

    @Test
    fun findingByNumber_withNoResult_returnsOptionalEmpty() {
        every { repo.findByNumbers(listOf(1L), LC) } returns ArrayList()
        val opt = service.findByNumber(1L, LC)
        opt.isPresent.shouldBeFalse()
        verify { repo.findByNumbers(listOf(1L), LC) }
    }

    @Test
    fun findingByNumber_withSingleResultFromRepo_returnsThatAsOptional() {
        every { repo.findByNumbers(listOf(1L), LC) } returns listOf(entity)
        auditFixture()
        testFindingByNumbers()
        verify { entity.toString() }
    }

    private fun testFindingByNumbers() {
        val opt = service.findByNumber(1L, LC)
        opt.isPresent.shouldBeTrue()
        opt.get() shouldBeEqualTo entity

        verify { repo.findByNumbers(listOf(1L), LC) }
        verify { userRepoMock.findById(CREATOR_ID) }
        verify { userRepoMock.findById(MODIFIER_ID) }
        verify { entity.createdBy }
        verify { entity.lastModifiedBy }
        verify { entity.createdByName = "creatingUser" }
        verify { entity.createdByFullName = "creatingUserFullName" }
        verify { entity.lastModifiedByName = "modifyingUser" }
        verify { entity == entity }
    }

    @Test
    fun findingByNumber_withMultipleRecordsFromRepo_returnsFirstAsOptional() {
        every { repo.findByNumbers(listOf(1L), LC) } returns listOf(entity, paperMock2)
        auditFixture()
        testFindingByNumbers()
        verify { entity.toString() }
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_delegatesToRepo() {
        val minimum = 4L
        every { repo.findLowestFreeNumberStartingFrom(minimum) } returns 17L
        service.findLowestFreeNumberStartingFrom(minimum) shouldBeEqualTo 17L
        verify { repo.findLowestFreeNumberStartingFrom(minimum) }
    }

    @Test
    fun findingPageOfIdsByFilter() {
        val ids = listOf(3L, 17L, 5L)
        every { repo.findPageOfIdsByFilter(filterMock, paginationContextMock) } returns ids
        service.findPageOfIdsByFilter(filterMock, paginationContextMock) shouldBeEqualTo ids
        verify { repo.findPageOfIdsByFilter(filterMock, paginationContextMock) }
    }

    @Test
    fun findingPageOfIdsBySearchOrder() {
        val ids = listOf(3L, 17L, 5L)
        every { repo.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock) } returns ids
        service.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock) shouldBeEqualTo ids
        verify { repo.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock) }
    }

    @Test
    fun excludingFromSearchOrder_delegatesToRepo() {
        val searchOrderId = 4L
        val paperId = 5L
        service.excludeFromSearchOrder(searchOrderId, paperId)
        verify { repo.excludePaperFromSearchOrderResults(searchOrderId, paperId) }
    }

    @Test
    fun reincludingFromSearchOrder_delegatesToRepo() {
        val searchOrderId = 4L
        val paperId = 5L
        service.reincludeIntoSearchOrder(searchOrderId, paperId)
        verify { repo.reincludePaperIntoSearchOrderResults(searchOrderId, paperId) }
    }

    @Test
    fun savingAttachment_delegatesToRepo() {
        val paMock = mockk<PaperAttachment>()
        service.saveAttachment(paMock)
        verify { repo.saveAttachment(paMock) }
    }

    @Test
    fun loadingAttachmentWithContentById_delegatesToRepo() {
        val id = 7
        every { repo.loadAttachmentWithContentBy(id) } returns attachmentMock
        service.loadAttachmentWithContentBy(id) shouldBeEqualTo attachmentMock
        verify { repo.loadAttachmentWithContentBy(id) }
    }

    @Test
    fun deletingAttachment_delegatesToRepo() {
        val id = 5
        every { repo.deleteAttachment(id) } returns entity
        service.deleteAttachment(id) shouldBeEqualTo entity
        verify { repo.deleteAttachment(id) }
        verify { entity == entity }
        verify { entity.toString() }
    }

    @Test
    fun deletingByIds_delegatesToRepo() {
        val ids = listOf(5L, 7L, 9L)
        service.deletePapersWithIds(ids)
        verify { repo.delete(ids) }
    }

    @Test
    fun findingByFilter_withPaperWithNullCreator() {
        every { paperMock3.createdBy } returns null
        every { paperMock3.lastModifiedBy } returns null
        papers.clear()
        papers.add(paperMock3)
        every { repo.findPageByFilter(filterMock, paginationContextMock) } returns papers
        service.findPageByFilter(filterMock, paginationContextMock) shouldBeEqualTo papers
        verify { repo.findPageByFilter(filterMock, paginationContextMock) }
        verify { paperMock3.createdBy }
        verify { paperMock3.createdByName = null }
        verify { paperMock3.createdByFullName = null }
        verify { paperMock3.lastModifiedByName = null }
    }

    @Test
    fun mergingPaperIntoNewsletter_withWipNewsletterPresent_delegatesToNewsletterRepo() {
        val newsletterId = 13
        val paperId: Long = 14
        val topicId = 3
        val langCode = "en"

        val wipNewsletter = Newsletter()
        wipNewsletter.id = newsletterId

        val nl = Paper.NewsletterLink(1, "link", 2, 3, "topic", "headline")
        every { newsletterRepoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.of(wipNewsletter)
        every { newsletterRepoMock.mergePaperIntoNewsletter(newsletterId, paperId, topicId, langCode) } returns java.util.Optional.of(nl)
        val result = service.mergePaperIntoWipNewsletter(paperId, topicId, langCode)
        result.isPresent.shouldBeTrue()
        result.get() shouldBeEqualTo nl

        verify { newsletterRepoMock.newsletterInStatusWorkInProgress }
        verify { newsletterRepoMock.mergePaperIntoNewsletter(newsletterId, paperId, topicId, langCode) }
    }

    @Test
    fun mergingPaperIntoNewsletter_withNoWipNewsletterPresent_returns0() {
        val paperId: Long = 14
        val topicId = 3
        val languageCode = "en"

        every { newsletterRepoMock.newsletterInStatusWorkInProgress } returns java.util.Optional.empty()
        service.mergePaperIntoWipNewsletter(paperId, topicId, languageCode) shouldBeEqualTo java.util.Optional.empty<Any>()
        verify { newsletterRepoMock.newsletterInStatusWorkInProgress }
    }

    @Test
    fun removingPaperFromNewsletter_delegatesToNewsletterRepo() {
        val newsletterId = 13
        val paperId: Long = 14
        every { newsletterRepoMock.removePaperFromNewsletter(newsletterId, paperId) } returns 1
        service.removePaperFromNewsletter(newsletterId, paperId) shouldBeEqualTo 1
        verify { newsletterRepoMock.removePaperFromNewsletter(newsletterId, paperId) }
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withInvalidFieldNameFails() {
        invoking {
            service.hasDuplicateFieldNextToCurrent("foo", "fw", 1L)
        } shouldThrow IllegalArgumentException::class withMessage "Field 'foo' is not supported by this validator."
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withPmIdValidated_withNoDuplicate() {
        val fieldName = "pmId"
        val fieldValue = 11
        val id = 1L
        every { repo.isPmIdAlreadyAssigned(fieldValue, id) } returns java.util.Optional.empty()

        service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id).isPresent.shouldBeFalse()

        verify { repo.isPmIdAlreadyAssigned(fieldValue, id) }
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withPmIdValidated_withDuplicate() {
        val fieldName = "pmId"
        val fieldValue = 10
        val id = 1L
        every { repo.isPmIdAlreadyAssigned(fieldValue, id) } returns java.util.Optional.of("2")
        service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id).get() shouldBeEqualTo "2"
        verify { repo.isPmIdAlreadyAssigned(fieldValue, id) }
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withDoiValidated_withNoDuplicate() {
        val fieldName = "doi"
        val fieldValue = "fw"
        val id = 1L
        every { repo.isDoiAlreadyAssigned(fieldValue, id) } returns java.util.Optional.empty()
        service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id).isPresent.shouldBeFalse()
        verify { repo.isDoiAlreadyAssigned(fieldValue, id) }
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withNullFieldValue_isAlwaysFalse() {
        val fieldName = "doi"
        val id = 1L

        service.hasDuplicateFieldNextToCurrent(fieldName, null, id).isPresent.shouldBeFalse()

        verify(exactly = 0) { repo.isPmIdAlreadyAssigned(any(), eq(id)) }
        verify(exactly = 0) { repo.isDoiAlreadyAssigned(any(), eq(id)) }
    }

    companion object {
        private const val MINIMUM_NUMBER = 7L
        private const val LC = "de"
    }
}
