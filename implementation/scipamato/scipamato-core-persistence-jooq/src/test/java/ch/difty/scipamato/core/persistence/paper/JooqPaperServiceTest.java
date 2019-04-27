package ch.difty.scipamato.core.persistence.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.core.entity.Paper;
import ch.difty.scipamato.core.entity.PaperAttachment;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.search.PaperFilter;
import ch.difty.scipamato.core.entity.search.SearchOrder;
import ch.difty.scipamato.core.persistence.AbstractServiceTest;
import ch.difty.scipamato.core.persistence.ServiceResult;
import ch.difty.scipamato.core.persistence.newsletter.NewsletterRepository;
import ch.difty.scipamato.core.pubmed.AbstractPubmedArticleFacade;
import ch.difty.scipamato.core.pubmed.PubmedArticleFacade;
import ch.difty.scipamato.core.pubmed.api.*;

@SuppressWarnings({ "ResultOfMethodCallIgnored", "OptionalGetWithoutIsPresent" })
class JooqPaperServiceTest extends AbstractServiceTest<Long, Paper, PaperRepository> {

    private static final long   MINIMUM_NUMBER = 7L;
    private static final String LC             = "de";

    private JooqPaperService service;

    @Mock
    private PaperRepository      repoMock;
    @Mock
    private NewsletterRepository newsletterRepoMock;
    @Mock
    private PaperFilter          filterMock;
    @Mock
    private SearchOrder          searchOrderMock;
    @Mock
    private PaginationContext    paginationContextMock;
    @Mock
    private Paper                paperMock, paperMock2, paperMock3;
    @Mock
    private PaperAttachment attachmentMock;

    private final List<PubmedArticleFacade> articles = new ArrayList<>();

    @Override
    protected Paper getEntity() {
        return paperMock;
    }

    @Override
    protected PaperRepository getRepo() {
        return repoMock;
    }

    private final List<Paper> papers = new ArrayList<>();

    @Override
    public void specificSetUp() {
        service = new JooqPaperService(repoMock, newsletterRepoMock, userRepoMock);

        papers.add(paperMock);
        papers.add(paperMock);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, searchOrderMock, paginationContextMock, paperMock, paperMock2);
    }

    @Test
    void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7L;
        when(repoMock.findById(id)).thenReturn(paperMock);
        auditFixture();

        Optional<Paper> optPaper = service.findById(id);
        assertThat(optPaper.isPresent()).isTrue();
        assertThat(optPaper.get()).isEqualTo(paperMock);

        verify(repoMock).findById(id);

        verifyAudit(1);
    }

    @Test
    void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Long id = 7L;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service
            .findById(id)
            .isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    void findingByFilter_delegatesToRepo() {
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        auditFixture();
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verifyAudit(2);
    }

    @Test
    void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    void savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        when(paperMock.getId()).thenReturn(null);
        when(repoMock.add(paperMock)).thenReturn(paperMock);
        auditFixture();
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).add(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    void savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        when(paperMock.getId()).thenReturn(17L);
        when(repoMock.update(paperMock)).thenReturn(paperMock);
        auditFixture();
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).update(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    void deleting_withNullEntity_doesNothing() {
        service.remove(null);
        verify(repoMock, never()).delete(anyLong(), anyInt());
    }

    @Test
    void deleting_withEntityWithNullId_doesNothing() {
        when(paperMock.getId()).thenReturn(null);

        service.remove(paperMock);

        verify(paperMock).getId();
        verify(repoMock, never()).delete(anyLong(), anyInt());
    }

    @Test
    void deleting_withEntityWithNormalId_delegatesToRepo() {
        when(paperMock.getId()).thenReturn(3L);
        when(paperMock.getVersion()).thenReturn(17);

        service.remove(paperMock);

        verify(paperMock, times(2)).getId();
        verify(paperMock, times(1)).getVersion();
        verify(repoMock, times(1)).delete(3L, 17);
    }

    @Test
    void findingBySearchOrder_delegatesToRepo() {
        when(repoMock.findBySearchOrder(searchOrderMock, LC)).thenReturn(papers);
        assertThat(service.findBySearchOrder(searchOrderMock, LC)).containsAll(papers);
        verify(repoMock).findBySearchOrder(searchOrderMock, LC);
    }

    @Test
    void findingPagedBySearchOrder_delegatesToRepo() {
        when(repoMock.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC)).thenReturn(papers);
        assertThat(service.findPageBySearchOrder(searchOrderMock, paginationContextMock, LC)).isEqualTo(papers);
        verify(repoMock).findPageBySearchOrder(searchOrderMock, paginationContextMock, LC);
    }

    @Test
    void countingBySearchOrder_delegatesToRepo() {
        when(repoMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2);
        verify(repoMock).countBySearchOrder(searchOrderMock);
    }

    @Test
    void dumpingEmptyListOfArticles_logsWarnMessage() {
        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();
    }

    @Test
    void dumpingSingleArticle_whichAlreadyExists_doesNotSave() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa = newPubmedArticle(pmIdValue);
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa));

        // existing papers
        when(repoMock.findExistingPmIdsOutOf(Collections.singletonList(pmIdValue))).thenReturn(
            Collections.singletonList(pmIdValue));

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages())
            .hasSize(1)
            .contains("PMID " + pmIdValue);
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock).findExistingPmIdsOutOf(Collections.singletonList(pmIdValue));
    }

    @Test
    void dumpingSingleNewArticle_saves() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa = newPubmedArticle(pmIdValue);
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa));
        assertThat(articles
            .get(0)
            .getPmId()).isNotNull();

        when(repoMock.findExistingPmIdsOutOf(Collections.singletonList(pmIdValue))).thenReturn(Collections.emptyList());
        when(repoMock.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER)).thenReturn(17L);

        when(repoMock.add(isA(Paper.class))).thenReturn(paperMock2);
        when(paperMock2.getId()).thenReturn(27L);
        when(paperMock2.getPmId()).thenReturn(pmIdValue);

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages())
            .hasSize(1)
            .contains("PMID " + pmIdValue + " (id 27)");
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock).findExistingPmIdsOutOf(Collections.singletonList(pmIdValue));
        verify(repoMock).findLowestFreeNumberStartingFrom(MINIMUM_NUMBER);
        verify(repoMock).add(isA(Paper.class));
        verify(paperMock2).getId();
        verify(paperMock2).getPmId();
    }

    @Test
    void dumpingSingleNewArticleWithNullPmId_doesNotTryToSave() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa = newPubmedArticle(pmIdValue);
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa));
        ((AbstractPubmedArticleFacade) articles.get(0)).setPmId(null);

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock, never()).findExistingPmIdsOutOf(Collections.singletonList(pmIdValue));
    }

    @Test
    void dumpingTwoNewArticleOneOfWhichWithNullPmId_savesOnlyOther() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa1 = newPubmedArticle(pmIdValue);
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(pa1));
        articles.add(PubmedArticleFacade.newPubmedArticleFrom(newPubmedArticle(0)));
        assertThat(articles
            .get(1)
            .getPmId()).isEqualTo("0");
        ((AbstractPubmedArticleFacade) articles.get(1)).setPmId(null);

        when(repoMock.findExistingPmIdsOutOf(Collections.singletonList(pmIdValue))).thenReturn(Collections.emptyList());
        when(repoMock.findLowestFreeNumberStartingFrom(MINIMUM_NUMBER)).thenReturn(17L);

        when(repoMock.add(isA(Paper.class))).thenReturn(paperMock2);
        when(paperMock2.getId()).thenReturn(27L);
        when(paperMock2.getPmId()).thenReturn(pmIdValue);

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles, MINIMUM_NUMBER);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages())
            .hasSize(1)
            .contains("PMID " + pmIdValue + " (id 27)");
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock).findExistingPmIdsOutOf(Collections.singletonList(pmIdValue));
        verify(repoMock).findLowestFreeNumberStartingFrom(MINIMUM_NUMBER);
        verify(repoMock).add(isA(Paper.class));
        verify(paperMock2).getId();
        verify(paperMock2).getPmId();
    }

    private PubmedArticle newPubmedArticle(Integer pmIdValue) {
        PubmedArticle pa = new PubmedArticle();
        MedlineCitation mc = new MedlineCitation();
        MedlineJournalInfo mji = new MedlineJournalInfo();
        mc.setMedlineJournalInfo(mji);
        PMID pmId = new PMID();
        pmId.setvalue(String.valueOf(pmIdValue));
        mc.setPMID(pmId);
        Article a = new Article();
        ArticleTitle at = new ArticleTitle();
        a.setArticleTitle(at);
        Journal j = new Journal();
        JournalIssue ji = new JournalIssue();
        PubDate pd = new PubDate();
        ji.setPubDate(pd);
        j.setJournalIssue(ji);
        a.setJournal(j);
        mc.setArticle(a);
        pa.setMedlineCitation(mc);
        return pa;
    }

    @Test
    void findingByNumber_withNoResult_returnsOptionalEmpty() {
        when(repoMock.findByNumbers(Collections.singletonList(1L), LC)).thenReturn(new ArrayList<>());
        Optional<Paper> opt = service.findByNumber(1L, LC);
        assertThat(opt.isPresent()).isFalse();
        verify(repoMock).findByNumbers(Collections.singletonList(1L), LC);
    }

    @Test
    void findingByNumber_withSingleResultFromRepo_returnsThatAsOptional() {
        when(repoMock.findByNumbers(Collections.singletonList(1L), LC)).thenReturn(
            Collections.singletonList(paperMock));
        auditFixture();
        testFindingByNumbers();
    }

    private void testFindingByNumbers() {
        Optional<Paper> opt = service.findByNumber(1L, LC);
        assertThat(opt.isPresent()).isTrue();
        assertThat(opt.get()).isEqualTo(paperMock);

        verify(repoMock).findByNumbers(Collections.singletonList(1L), LC);
        verify(userRepoMock).findById(CREATOR_ID);
        verify(userRepoMock).findById(MODIFIER_ID);
        verify(paperMock).getCreatedBy();
        verify(paperMock).getLastModifiedBy();
        verify(paperMock).setCreatedByName("creatingUser");
        verify(paperMock).setCreatedByFullName("creatingUserFullName");
        verify(paperMock).setLastModifiedByName("modifyingUser");
    }

    @Test
    void findingByNumber_withMultipleRecordsFromRepo_returnsFirstAsOptional() {
        when(repoMock.findByNumbers(Collections.singletonList(1L), LC)).thenReturn(
            Arrays.asList(paperMock, paperMock2));
        auditFixture();
        testFindingByNumbers();
    }

    @Test
    void findingLowestFreeNumberStartingFrom_delegatesToRepo() {
        long minimum = 4L;
        when(repoMock.findLowestFreeNumberStartingFrom(minimum)).thenReturn(17L);
        assertThat(service.findLowestFreeNumberStartingFrom(minimum)).isEqualTo(17L);
        verify(repoMock).findLowestFreeNumberStartingFrom(minimum);
    }

    @Test
    void findingPageOfIdsByFilter() {
        final List<Long> ids = Arrays.asList(3L, 17L, 5L);
        when(repoMock.findPageOfIdsByFilter(filterMock, paginationContextMock)).thenReturn(ids);
        assertThat(service.findPageOfIdsByFilter(filterMock, paginationContextMock)).isEqualTo(ids);
        verify(repoMock).findPageOfIdsByFilter(filterMock, paginationContextMock);
    }

    @Test
    void findingPageOfIdsBySearchOrder() {
        final List<Long> ids = Arrays.asList(3L, 17L, 5L);
        when(repoMock.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(ids);
        assertThat(service.findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock)).isEqualTo(ids);
        verify(repoMock).findPageOfIdsBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    void excludingFromSearchOrder_delegatesToRepo() {
        long searchOrderId = 4L;
        long paperId = 5L;
        service.excludeFromSearchOrder(searchOrderId, paperId);
        verify(repoMock).excludePaperFromSearchOrderResults(searchOrderId, paperId);
    }

    @Test
    void reincludingFromSearchOrder_delegatesToRepo() {
        long searchOrderId = 4L;
        long paperId = 5L;
        service.reincludeIntoSearchOrder(searchOrderId, paperId);
        verify(repoMock).reincludePaperIntoSearchOrderResults(searchOrderId, paperId);
    }

    @Test
    void savingAttachment_delegatesToRepo() {
        PaperAttachment paMock = mock(PaperAttachment.class);
        service.saveAttachment(paMock);
        verify(repoMock).saveAttachment(paMock);
    }

    @Test
    void loadingAttachmentWithContentById_delegatesToRepo() {
        final Integer id = 7;
        when(repoMock.loadAttachmentWithContentBy(id)).thenReturn(attachmentMock);
        assertThat(service.loadAttachmentWithContentBy(id)).isEqualTo(attachmentMock);
        verify(repoMock).loadAttachmentWithContentBy(id);
    }

    @Test
    void deletingAttachment_delegatesToRepo() {
        Integer id = 5;
        when(repoMock.deleteAttachment(id)).thenReturn(paperMock);
        assertThat(service.deleteAttachment(id)).isEqualTo(paperMock);
        verify(repoMock).deleteAttachment(id);
    }

    @Test
    void deletingByIds_delegatesToRepo() {
        List<Long> ids = Arrays.asList(5L, 7L, 9L);
        service.deletePapersWithIds(ids);
        verify(repoMock).delete(ids);
    }

    @Test
    void findingByFilter_withPaperWithNullCreator() {
        when(paperMock3.getCreatedBy()).thenReturn(null);
        when(paperMock3.getLastModifiedBy()).thenReturn(null);
        papers.clear();
        papers.add(paperMock3);
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verify(paperMock3).getCreatedBy();
        verify(paperMock3).setCreatedByName(null);
        verify(paperMock3).setCreatedByFullName(null);
        verify(paperMock3).setLastModifiedByName(null);
    }

    @Test
    void mergingPaperIntoNewsletter_withWipNewsletterPresent_delegatesToNewsletterRepo() {
        final int newsletterId = 13;
        final long paperId = 14;
        final Integer topicId = 3;
        final String langCode = "en";

        Newsletter wipNewsletter = new Newsletter();
        wipNewsletter.setId(newsletterId);

        Paper.NewsletterLink nl = mock(Paper.NewsletterLink.class);
        when(newsletterRepoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.of(wipNewsletter));
        when(newsletterRepoMock.mergePaperIntoNewsletter(newsletterId, paperId, topicId, langCode)).thenReturn(
            Optional.of(nl));
        Optional<Paper.NewsletterLink> result = service.mergePaperIntoWipNewsletter(paperId, topicId, langCode);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(nl);

        verify(newsletterRepoMock).getNewsletterInStatusWorkInProgress();
        verify(newsletterRepoMock).mergePaperIntoNewsletter(newsletterId, paperId, topicId, langCode);
    }

    @Test
    void mergingPaperIntoNewsletter_withNoWipNewsletterPresent_returns0() {
        final long paperId = 14;
        final Integer topicId = 3;
        final String languageCode = "en";

        when(newsletterRepoMock.getNewsletterInStatusWorkInProgress()).thenReturn(Optional.empty());
        assertThat(service.mergePaperIntoWipNewsletter(paperId, topicId, languageCode)).isEqualTo(Optional.empty());
        verify(newsletterRepoMock).getNewsletterInStatusWorkInProgress();
    }

    @Test
    void removingPaperFromNewsletter_delegatesToNewsletterRepo() {
        final int newsletterId = 13;
        final long paperId = 14;
        when(newsletterRepoMock.removePaperFromNewsletter(newsletterId, paperId)).thenReturn(1);
        assertThat(service.removePaperFromNewsletter(newsletterId, paperId)).isEqualTo(1);
        verify(newsletterRepoMock).removePaperFromNewsletter(newsletterId, paperId);
    }

    @Test
    void hasDuplicateFieldNextToCurrent_withDegenerateConstruction_nullFieldName_throws() {
        TestUtils.assertDegenerateSupplierParameter(() -> service.hasDuplicateFieldNextToCurrent(null, "fw", 1L),
            "fieldName");
    }

    @Test
    void hasDuplicateFieldNextToCurrent_withInvalidFieldNameFails() {
        try {
            service.hasDuplicateFieldNextToCurrent("foo", "fw", 1L);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Field 'foo' is not supported by this validator.");
        }
    }

    @Test
    void hasDuplicateFieldNextToCurrent_withPmIdValidated_withNoDuplicate() {
        final String fieldName = "pmId";
        final int fieldValue = 11;
        final Long id = 1L;
        when(repoMock.isPmIdAlreadyAssigned(fieldValue, id)).thenReturn(Optional.empty());

        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id)).isNotPresent();

        verify(repoMock).isPmIdAlreadyAssigned(fieldValue, id);
    }

    @Test
    void hasDuplicateFieldNextToCurrent_withPmIdValidated_withDuplicate() {
        final String fieldName = "pmId";
        final int fieldValue = 10;
        final Long id = 1L;
        when(repoMock.isPmIdAlreadyAssigned(fieldValue, id)).thenReturn(Optional.of("2"));

        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id)).hasValue("2");

        verify(repoMock).isPmIdAlreadyAssigned(fieldValue, id);
    }

    @Test
    void hasDuplicateFieldNextToCurrent_withDoiValidated_withNoDuplicate() {
        final String fieldName = "doi";
        final String fieldValue = "fw";
        final Long id = 1L;
        when(repoMock.isDoiAlreadyAssigned(fieldValue, id)).thenReturn(Optional.empty());

        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, id)).isNotPresent();

        verify(repoMock).isDoiAlreadyAssigned(fieldValue, id);
    }

    @Test
    void hasDuplicateFieldNextToCurrent_withNullFieldValue_isAlwaysFalse() {
        final String fieldName = "doi";
        final Long id = 1L;

        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, null, id)).isNotPresent();

        verify(repoMock, never()).isPmIdAlreadyAssigned(anyInt(), eq(id));
        verify(repoMock, never()).isDoiAlreadyAssigned(anyString(), eq(id));
    }

    @Test
    void hasDuplicateFieldNextToCurrent_withNullId_withNoOtherStudyMatchingPmId() {
        final String fieldName = "pmId";
        final int fieldValue = 10;

        when(repoMock.isPmIdAlreadyAssigned(fieldValue, null)).thenReturn(Optional.empty());

        assertThat(service.hasDuplicateFieldNextToCurrent(fieldName, fieldValue, null)).isNotPresent();

        verify(repoMock).isPmIdAlreadyAssigned(fieldValue, null);
    }

}
