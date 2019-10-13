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
import ch.difty.scipamato.core.pubmed.api.*
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

internal class JooqPaperServiceTest : AbstractServiceTest<Long, Paper, PaperRepository>() {

    override val repo = mock<PaperRepository>()
    private val newsletterRepoMock = mock<NewsletterRepository>()
    private val filterMock = mock<PaperFilter>()
    private val searchOrderMock = mock<SearchOrder>()
    private val paginationContextMock = mock<PaginationContext>()
    override val entity = mock<Paper>()
    private val paperMock2 = mock<Paper>()
    private val paperMock3 = mock<Paper>()
    private val attachmentMock = mock<PaperAttachment>()

    private val service: JooqPaperService = JooqPaperService(repo, newsletterRepoMock, userRepoMock)

    private val papers = mutableListOf(entity, entity)
    private val articles = mutableListOf<PubmedArticleFacade>()

    public override fun specificTearDown() {
        verifyNoMoreInteractions(repo, filterMock, searchOrderMock, paginationContextMock, entity, paperMock2)
    }

    @Test
    fun findingById_withFoundEntity_returnsOptionalOfIt() {
        val id = 7L
        whenever(repo.findById(id)).thenReturn(entity)
        auditFixture()

        val optPaper = service.findById(id)
        assertThat(optPaper.isPresent).isTrue()
        assertThat(optPaper.get()).isEqualTo(entity)

        verify(repo).findById(id)

        verifyAudit(1)
    }

    @Test
    fun findingById_withNotFoundEntity_returnsOptionalEmpty() {
        val id = 7L
        whenever(repo.findById(id)).thenReturn(null)
        assertThat(service.findById(id).isPresent).isFalse()
        verify(repo).findById(id)
    }

    @Test
    fun findingByFilter_delegatesToRepo() {
        whenever(repo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers)
        auditFixture()
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers)
        verify(repo).findPageByFilter(filterMock, paginationContextMock)
        verifyAudit(2)
    }

    @Test
    fun countingByFilter_delegatesToRepo() {
        whenever(repo.countByFilter(filterMock)).thenReturn(3)
        assertThat(service.countByFilter(filterMock)).isEqualTo(3)
        verify(repo).countByFilter(filterMock)
    }

    @Test
    fun savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        whenever(entity.id).thenReturn(null)
        whenever(repo.add(entity)).thenReturn(entity)
        auditFixture()
        assertThat(service.saveOrUpdate(entity)).isEqualTo(entity)
        verify(repo).add(entity)
        verify(entity).id
        verifyAudit(1)
    }

    @Test
    fun savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        whenever(entity.id).thenReturn(17L)
        whenever(repo.update(entity)).thenReturn(entity)
        auditFixture()
        assertThat(service.saveOrUpdate(entity)).isEqualTo(entity)
        verify(repo).update(entity)
        verify(entity).id
        verifyAudit(1)
    }

    @Test
    fun deleting_withNullEntity_doesNothing() {
        service.remove(null)
        verify(repo, never()).delete(anyLong(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNullId_doesNothing() {
        whenever(entity.id).thenReturn(null)

        service.remove(entity)

        verify(entity).id
        verify(repo, never()).delete(anyLong(), anyInt())
    }

    @Test
    fun deleting_withEntityWithNormalId_delegatesToRepo() {
        whenever(entity.id).thenReturn(3L)
        whenever(entity.version).thenReturn(17)

        service.remove(entity)

        verify(entity, times(2)).id
        verify(entity, times(1)).version
        verify(repo, times(1)).delete(3L, 17)
    }

    @Test
    fun findingBySearchOrder_delegatesToRepo() {
        whenever(repo.findBySearchOrder(searchOrderMock, LC)).thenReturn(papers)
        assertThat(service.findBySearchOrder(searchOrderMock, LC)).containsAll(papers)
        verify(repo).findBySearchOrder(searchOrderMock, LC)
    }

    @Test
    fun findingPagedBySearchOrder_delegatesToRepo() {
        whenever(repo.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC)).thenReturn(papers)
        assertThat(service.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC)).isEqualTo(papers)
        verify(repo).findPageBySearchOrder(searchOrderMock, paginationContextMock, LC)
    }

    @Test
    fun countingBySearchOrder_delegatesToRepo() {
        whenever(repo.countBySearchOrder(searchOrderMock)).thenReturn(2)
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2)
        verify(repo).countBySearchOrder(searchOrderMock)
    }

    @Test
    fun dumpingEmptyListOfArticles_logsWarnMessage() {
        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()
    }

    @Test
    fun dumpingSingleArticle_whichAlreadyExists_doesNotSave() {
        val pmIdValue = 23193287
        val pa = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa))

        // existing papers
        whenever(repo.findExistingPmIdsOutOf(listOf(pmIdValue))).thenReturn(listOf(pmIdValue))

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.warnMessages).contains("PMID $pmIdValue")
        assertThat(sr.errorMessages).isEmpty()

        verify(repo).findExistingPmIdsOutOf(listOf(pmIdValue))
    }

    @Test
    fun dumpingSingleNewArticle_saves() {
        val pmIdValue = 23193287
        val pa = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa))
        assertThat(articles[0].pmId).isNotNull()

        whenever(repo.findExistingPmIdsOutOf(listOf(pmIdValue))).thenReturn(emptyList())
        whenever(repo.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER)).thenReturn(17L)

        whenever(repo.add(any())).thenReturn(paperMock2)
        whenever(paperMock2.id).thenReturn(27L)
        whenever(paperMock2.pmId).thenReturn(pmIdValue)

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        assertThat(sr.infoMessages).contains("PMID $pmIdValue (id 27)")
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()

        verify(repo).findExistingPmIdsOutOf(listOf(pmIdValue))
        verify(repo).findLowestFreeNumberStartingFrom(MINIMUM_NUMBER)
        verify(repo).add(any())
        verify(paperMock2).id
        verify(paperMock2).pmId
    }

    @Test
    fun dumpingSingleNewArticleWithNullPmId_doesNotTryToSave() {
        val pmIdValue = 23193287
        val pa = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa))
        (articles[0] as AbstractPubmedArticleFacade).pmId = null

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        assertThat(sr.infoMessages).isEmpty()
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()

        verify(repo, never()).findExistingPmIdsOutOf(listOf(pmIdValue))
    }

    @Test
    fun dumpingTwoNewArticleOneOfWhichWithNullPmId_savesOnlyOther() {
        val pmIdValue = 23193287
        val pa1 = newPubmedArticle(pmIdValue)
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa1))
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(newPubmedArticle(0)))
        assertThat(articles[1].pmId).isEqualTo("0")
        (articles[1] as AbstractPubmedArticleFacade).pmId = null

        whenever(repo.findExistingPmIdsOutOf(listOf(pmIdValue))).thenReturn(emptyList())
        whenever(repo.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER)).thenReturn(17L)

        whenever(repo.add(any())).thenReturn(paperMock2)
        whenever(paperMock2.id).thenReturn(27L)
        whenever(paperMock2.pmId).thenReturn(pmIdValue)

        val sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER)
        assertThat(sr.infoMessages).hasSize(1).contains("PMID $pmIdValue (id 27)")
        assertThat(sr.warnMessages).isEmpty()
        assertThat(sr.errorMessages).isEmpty()

        verify(repo).findExistingPmIdsOutOf(listOf(pmIdValue))
        verify(repo).findLowestFreeNumberStartingFrom(MINIMUM_NUMBER)
        verify(repo).add(any())
        verify(paperMock2).id
        verify(paperMock2).pmId
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
        whenever(repo.findByNumbers(listOf(1L), LC)).thenReturn(ArrayList())
        val opt = service.findByNumber(1L, LC)
        assertThat(opt.isPresent).isFalse()
        verify(repo).findByNumbers(listOf(1L), LC)
    }

    @Test
    fun findingByNumber_withSingleResultFromRepo_returnsThatAsOptional() {
        whenever(repo.findByNumbers(listOf(1L), LC)).thenReturn(listOf(entity))
        auditFixture()
        testFindingByNumbers()
    }

    private fun testFindingByNumbers() {
        val opt = service.findByNumber(1L, LC)
        assertThat(opt.isPresent).isTrue()
        assertThat(opt.get()).isEqualTo(entity)

        verify(repo).findByNumbers(listOf(1L), LC)
        verify(userRepoMock).findById(CREATOR_ID)
        verify(userRepoMock).findById(MODIFIER_ID)
        verify(entity).createdBy
        verify(entity).lastModifiedBy
        verify(entity).createdByName = "creatingUser"
        verify(entity).createdByFullName = "creatingUserFullName"
        verify(entity).lastModifiedByName = "modifyingUser"
    }

    @Test
    fun findingByNumber_withMultipleRecordsFromRepo_returnsFirstAsOptional() {
        whenever(repo.findByNumbers(listOf(1L), LC)).thenReturn(listOf(entity, paperMock2))
        auditFixture()
        testFindingByNumbers()
    }

    @Test
    fun findingLowestFreeNumberStartingFrom_delegatesToRepo() {
        val minimum = 4L
        whenever(repo.findLowestFreeNumberStartingFrom(minimum)).thenReturn(17L)
        assertThat(service.findLowestFreeNumberStartingFrom(minimum)).isEqualTo(17L)
        verify(repo).findLowestFreeNumberStartingFrom(minimum)
    }

    @Test
    fun findingPageOfIdsByFilter() {
        val ids = listOf(3L, 17L, 5L)
        whenever(repo.findPageOfIdsByFilter(filterMock, paginationContextMock)).thenReturn(ids)
        assertThat(service.findPageOfIdsByFilter(filterMock, paginationContextMock)).isEqualTo(ids)
        verify(repo).findPageOfIdsByFilter(filterMock, paginationContextMock)
    }

    @Test
    fun findingPageOfIdsBySearchOrder() {
        val ids = listOf(3L, 17L, 5L)
        whenever(repo.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(ids)
        assertThat(service.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).isEqualTo(ids)
        verify(repo).findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)
    }

    @Test
    fun excludingFromSearchOrder_delegatesToRepo() {
        val searchOrderId = 4L
        val paperId = 5L
        service.excludeFromSearchOrder(searchOrderId, paperId)
        verify(repo).excludePaperFromSearchOrderResults(searchOrderId, paperId)
    }

    @Test
    fun reincludingFromSearchOrder_delegatesToRepo() {
        val searchOrderId = 4L
        val paperId = 5L
        service.reincludeIntoSearchOrder(searchOrderId, paperId)
        verify(repo).reincludePaperIntoSearchOrderResults(searchOrderId, paperId)
    }

    @Test
    fun savingAttachment_delegatesToRepo() {
        val paMock = Mockito.mock(PaperAttachment::class.java)
        service.saveAttachment(paMock)
        verify(repo).saveAttachment(paMock)
    }

    @Test
    fun loadingAttachmentWithContentById_delegatesToRepo() {
        val id = 7
        whenever(repo.loadAttachmentWithContentBy(id)).thenReturn(attachmentMock)
        assertThat(service.loadAttachmentWithContentBy(id)).isEqualTo(attachmentMock)
        verify(repo).loadAttachmentWithContentBy(id)
    }

    @Test
    fun deletingAttachment_delegatesToRepo() {
        val id = 5
        whenever(repo.deleteAttachment(id)).thenReturn(entity)
        assertThat(service.deleteAttachment(id)).isEqualTo(entity)
        verify(repo).deleteAttachment(id)
    }

    @Test
    fun deletingByIds_delegatesToRepo() {
        val ids = listOf(5L, 7L, 9L)
        service.deletePapersWithIds(ids)
        verify(repo).delete(ids)
    }

    @Test
    fun findingByFilter_withPaperWithNullCreator() {
        whenever(paperMock3.createdBy).thenReturn(null)
        whenever(paperMock3.lastModifiedBy).thenReturn(null)
        papers.clear()
        papers.add(paperMock3)
        whenever(repo.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers)
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers)
        verify(repo).findPageByFilter(filterMock, paginationContextMock)
        verify(paperMock3).createdBy
        verify(paperMock3).createdByName = null
        verify(paperMock3).createdByFullName = null
        verify(paperMock3).lastModifiedByName = null
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
        whenever(newsletterRepoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.of(wipNewsletter))
        whenever(newsletterRepoMock.mergePaperIntoNewsletter(newsletterId, paperId, topicId, langCode))
            .thenReturn(java.util.Optional.of(nl))
        val result = service.mergePaperIntoWipNewsletter(paperId, topicId, langCode)
        assertThat(result).isPresent
        assertThat(result.get()).isEqualTo(nl)

        verify(newsletterRepoMock).newsletterInStatusWorkInProgress
        verify(newsletterRepoMock).mergePaperIntoNewsletter(newsletterId, paperId, topicId, langCode)
    }

    @Test
    fun mergingPaperIntoNewsletter_withNoWipNewsletterPresent_returns0() {
        val paperId: Long = 14
        val topicId = 3
        val languageCode = "en"

        whenever(newsletterRepoMock.newsletterInStatusWorkInProgress).thenReturn(java.util.Optional.empty())
        assertThat(service.mergePaperIntoWipNewsletter(paperId, topicId, languageCode))
            .isEqualTo(java.util.Optional.empty<Any>())
        verify(newsletterRepoMock).newsletterInStatusWorkInProgress
    }

    @Test
    fun removingPaperFromNewsletter_delegatesToNewsletterRepo() {
        val newsletterId = 13
        val paperId: Long = 14
        whenever(newsletterRepoMock.removePaperFromNewsletter(newsletterId, paperId)).thenReturn(1)
        assertThat(service.removePaperFromNewsletter(newsletterId, paperId)).isEqualTo(1)
        verify(newsletterRepoMock).removePaperFromNewsletter(newsletterId, paperId)
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withInvalidFieldNameFails() {
        try {
            service.hasDuplicateFieldNextToCurrent("foo", "fw", 1L)
            fail<Any>("should have thrown exception")
        } catch (ex: Exception) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Field 'foo' is not supported by this validator.")
        }
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withPmIdValidated_withNoDuplicate() {
        val fieldName = "pmId"
        val fieldValue = 11
        val id = 1L
        whenever(repo.isPmIdAlreadyAssigned(fieldValue, id)).thenReturn(java.util.Optional.empty())

        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id)).isNotPresent

        verify(repo).isPmIdAlreadyAssigned(fieldValue, id)
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withPmIdValidated_withDuplicate() {
        val fieldName = "pmId"
        val fieldValue = 10
        val id = 1L
        whenever(repo.isPmIdAlreadyAssigned(fieldValue, id)).thenReturn(java.util.Optional.of("2"))
        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id)).hasValue("2")
        verify(repo).isPmIdAlreadyAssigned(fieldValue, id)
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withDoiValidated_withNoDuplicate() {
        val fieldName = "doi"
        val fieldValue = "fw"
        val id = 1L
        whenever(repo.isDoiAlreadyAssigned(fieldValue, id)).thenReturn(java.util.Optional.empty())
        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id)).isNotPresent
        verify(repo).isDoiAlreadyAssigned(fieldValue, id)
    }

    @Test
    fun hasDuplicateFieldNextToCurrent_withNullFieldValue_isAlwaysFalse() {
        val fieldName = "doi"
        val id = 1L

        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, null, id)).isNotPresent

        verify(repo, never()).isPmIdAlreadyAssigned(anyInt(), eq(id))
        verify(repo, never()).isDoiAlreadyAssigned(anyString(), eq(id))
    }

    companion object {
        private const val MINIMUM_NUMBER = 7L
        private const val LC = "de"
    }
}
