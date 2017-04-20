package ch.difty.sipamato.persistance.jooq.paper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ch.difty.sipamato.entity.Paper;
import ch.difty.sipamato.entity.SearchOrder;
import ch.difty.sipamato.paging.PaginationContext;
import ch.difty.sipamato.persistance.jooq.AbstractServiceTest;
import ch.difty.sipamato.pubmed.Article;
import ch.difty.sipamato.pubmed.ArticleTitle;
import ch.difty.sipamato.pubmed.Journal;
import ch.difty.sipamato.pubmed.JournalIssue;
import ch.difty.sipamato.pubmed.MedlineCitation;
import ch.difty.sipamato.pubmed.MedlineJournalInfo;
import ch.difty.sipamato.pubmed.PMID;
import ch.difty.sipamato.pubmed.PubDate;
import ch.difty.sipamato.pubmed.PubmedArticle;
import ch.difty.sipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.sipamato.pubmed.entity.SipamatoPubmedArticle;
import ch.difty.sipamato.service.ServiceResult;

public class JooqPaperServiceTest extends AbstractServiceTest<Long, Paper, PaperRepository> {

    private final JooqPaperService service = new JooqPaperService();

    @Mock
    private PaperRepository repoMock;
    @Mock
    private PaperFilter filterMock;
    @Mock
    private SearchOrder searchOrderMock;
    @Mock
    private PaginationContext paginationContextMock;
    @Mock
    protected Paper paperMock, paperMock2;

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
        service.setRepository(repoMock);
        service.setUserRepository(userRepoMock);

        papers.add(paperMock);
        papers.add(paperMock);

        when(paperMock.getCreatedBy()).thenReturn(10);
    }

    @Override
    public void specificTearDown() {
        verifyNoMoreInteractions(repoMock, filterMock, searchOrderMock, paginationContextMock, paperMock, paperMock2);
    }

    @Test
    public void findingById_withFoundEntity_returnsOptionalOfIt() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(paperMock);

        Optional<Paper> optPaper = service.findById(id);
        assertThat(optPaper.isPresent()).isTrue();
        assertThat(optPaper.get()).isEqualTo(paperMock);

        verify(repoMock).findById(id);

        verifyAudit(1);
    }

    @Test
    public void findingById_withNotFoundEntity_returnsOptionalEmpty() {
        Long id = 7l;
        when(repoMock.findById(id)).thenReturn(null);

        assertThat(service.findById(id).isPresent()).isFalse();

        verify(repoMock).findById(id);
    }

    @Test
    public void findingByFilter_delegatesToRepo() {
        when(repoMock.findPageByFilter(filterMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageByFilter(filterMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageByFilter(filterMock, paginationContextMock);
        verifyAudit(2);
    }

    @Test
    public void countingByFilter_delegatesToRepo() {
        when(repoMock.countByFilter(filterMock)).thenReturn(3);
        assertThat(service.countByFilter(filterMock)).isEqualTo(3);
        verify(repoMock).countByFilter(filterMock);
    }

    @Test
    public void savingOrUpdating_withPaperWithNullId_hasRepoAddThePaper() {
        when(paperMock.getId()).thenReturn(null);
        when(repoMock.add(paperMock)).thenReturn(paperMock);
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).add(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    public void savingOrUpdating_withPaperWithNonNullId_hasRepoUpdateThePaper() {
        when(paperMock.getId()).thenReturn(17l);
        when(repoMock.update(paperMock)).thenReturn(paperMock);
        assertThat(service.saveOrUpdate(paperMock)).isEqualTo(paperMock);
        verify(repoMock).update(paperMock);
        verify(paperMock).getId();
        verifyAudit(1);
    }

    @Test
    public void deleting_withNullEntity_doesNothing() {
        service.remove(null);
        verify(repoMock, never()).delete(Mockito.anyLong());
    }

    @Test
    public void deleting_withEntityWithNullId_doesNothing() {
        when(paperMock.getId()).thenReturn(null);

        service.remove(paperMock);

        verify(paperMock).getId();
        verify(repoMock, never()).delete(Mockito.anyLong());
    }

    @Test
    public void deleting_withEntityWithNormald_delegatesToRepo() {
        when(paperMock.getId()).thenReturn(3l);

        service.remove(paperMock);

        verify(paperMock, times(2)).getId();
        verify(repoMock, times(1)).delete(3l);
    }

    @Test
    public void findingBySearchOrder_delegatesToRepo() {
        when(repoMock.findBySearchOrder(searchOrderMock)).thenReturn(papers);
        assertThat(service.findBySearchOrder(searchOrderMock)).containsAll(papers);
        verify(repoMock).findBySearchOrder(searchOrderMock);
    }

    @Test
    public void findingPagedBySearchOrder_delegatesToRepo() {
        when(repoMock.findPageBySearchOrder(searchOrderMock, paginationContextMock)).thenReturn(papers);
        assertThat(service.findPageBySearchOrder(searchOrderMock, paginationContextMock)).isEqualTo(papers);
        verify(repoMock).findPageBySearchOrder(searchOrderMock, paginationContextMock);
    }

    @Test
    public void countingBySearchOrder_delegatesToRepo() {
        when(repoMock.countBySearchOrder(searchOrderMock)).thenReturn(2);
        assertThat(service.countBySearchOrder(searchOrderMock)).isEqualTo(2);
        verify(repoMock).countBySearchOrder(searchOrderMock);
    }

    @Test
    public void dumpingEmptyListOfArticles_logsWarnMessage() {
        ServiceResult sr = service.dumpPubmedArticlesToDb(articles);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();
    }

    @Test
    public void dumpingSingleArticle_whichAlreadyExists_doesNotSave() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa = newPubmedArticle(pmIdValue);
        articles.add(SipamatoPubmedArticle.of(pa));

        // existing papers
        when(repoMock.findByPmIds(Arrays.asList(pmIdValue))).thenReturn(Arrays.asList(paperMock));
        when(paperMock.getPmId()).thenReturn(pmIdValue);

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).isEmpty();
        assertThat(sr.getWarnMessages()).hasSize(1).contains("PMID " + pmIdValue);
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock).findByPmIds(Arrays.asList(pmIdValue));
        verify(paperMock).getPmId();
    }

    @Test
    public void dumpingSingleNewArticle_saves() {
        Integer pmIdValue = 23193287;
        PubmedArticle pa = newPubmedArticle(pmIdValue);
        articles.add(SipamatoPubmedArticle.of(pa));

        when(repoMock.findByPmIds(Arrays.asList(pmIdValue))).thenReturn(Arrays.asList());

        when(repoMock.add(Mockito.isA(Paper.class))).thenReturn(paperMock2);
        when(paperMock2.getId()).thenReturn(27l);
        when(paperMock2.getPmId()).thenReturn(pmIdValue);

        ServiceResult sr = service.dumpPubmedArticlesToDb(articles);
        assertThat(sr).isNotNull();
        assertThat(sr.getInfoMessages()).hasSize(1).contains("PMID " + pmIdValue + " (id 27)");
        assertThat(sr.getWarnMessages()).isEmpty();
        assertThat(sr.getErrorMessages()).isEmpty();

        verify(repoMock).findByPmIds(Arrays.asList(pmIdValue));
        verify(repoMock).add(Mockito.isA(Paper.class));
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
}
