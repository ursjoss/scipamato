package ch.difty.scipamato.pubmed.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.transform.stream.StreamSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.pubmed.Article;
import ch.difty.scipamato.pubmed.ArticleTitle;
import ch.difty.scipamato.pubmed.Journal;
import ch.difty.scipamato.pubmed.JournalIssue;
import ch.difty.scipamato.pubmed.MedlineCitation;
import ch.difty.scipamato.pubmed.MedlineJournalInfo;
import ch.difty.scipamato.pubmed.PMID;
import ch.difty.scipamato.pubmed.PubDate;
import ch.difty.scipamato.pubmed.PubMed;
import ch.difty.scipamato.pubmed.PubmedArticle;
import ch.difty.scipamato.pubmed.PubmedArticleSet;
import ch.difty.scipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.scipamato.pubmed.entity.ScipamatoPubmedArticleTest;

@RunWith(MockitoJUnitRunner.class)
public class PubmedXmlServiceTest {

    private PubmedXmlService service;

    @Mock
    private Jaxb2Marshaller unmarshallerMock;
    @Mock
    private PubMed pubMedMock;
    @Mock
    private PubmedArticleSet pubmedArticleSetMock;
    @Mock
    private PubmedArticle pubmedArticleMock;
    @Mock
    private MedlineCitation medLineCitationMock;
    @Mock
    private Article articleMock;
    @Mock
    private Journal journalMock;
    @Mock
    private PMID pmidMock;
    @Mock
    private JournalIssue journalIssueMock;
    @Mock
    private PubDate pubDateMock;
    @Mock
    private MedlineJournalInfo medLineJournalInfoMock;
    @Mock
    private ArticleTitle articleTitleMock;

    @Before
    public void setUp() {
        service = new PubmedXmlService(unmarshallerMock, pubMedMock);

        when(pubmedArticleMock.getMedlineCitation()).thenReturn(medLineCitationMock);
        when(medLineCitationMock.getArticle()).thenReturn(articleMock);
        when(articleMock.getJournal()).thenReturn(journalMock);
        when(medLineCitationMock.getPMID()).thenReturn(pmidMock);
        when(journalMock.getJournalIssue()).thenReturn(journalIssueMock);
        when(journalIssueMock.getPubDate()).thenReturn(pubDateMock);
        when(medLineCitationMock.getMedlineJournalInfo()).thenReturn(medLineJournalInfoMock);
        when(articleMock.getArticleTitle()).thenReturn(articleTitleMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(unmarshallerMock, pubMedMock, pubmedArticleSetMock);
    }

    @Test
    public void degenerateConstruction_nullUnmarshaller_throws() {
        try {
            new PubmedXmlService(null, pubMedMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("unmarshaller must not be null.");
        }
    }

    @Test
    public void degenerateConstruction_nullPubMed_throws() {
        try {
            new PubmedXmlService(unmarshallerMock, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubMed must not be null.");
        }
    }

    @Test
    public void unmarshallingNull_throws() {
        try {
            service.unmarshal(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("xmlString must not be null.");
        }
    }

    @Test
    public void gettingPubmedArticleWithPmid_withValidId_returnsArticle() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        final List<Object> objects = new ArrayList<Object>();
        objects.add(pubmedArticleMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isTrue();
        assertThat(pa.get()).isNotNull();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmid_withInvalidId_returnsEmptyOptional() {
        final int pmId = 999999999;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        final List<Object> objects = new ArrayList<Object>();
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isFalse();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmid_withNullObjects_returnsEmptyOoptional() {
        final int pmId = 999999999;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(null);

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isFalse();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void nonValidXml_returnsNull() throws XmlMappingException, IOException {
        assertThat(service.unmarshal("")).isNull();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withUnarshallerException_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenThrow(new UnmarshallingFailureException("boom"));
        assertThat(service.extractArticlesFrom("some invalid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPumbedArticleSetWithoutArticleCollection_returnsEmptyList() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(pubmedArticleSet);
        assertThat(service.extractArticlesFrom("some valid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPumbedArticleSetWithoutArticleCollectionx_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(makeMinimalValidPubmedArticleSet());

        assertThat(service.extractArticlesFrom("some valid xml")).isNotEmpty();

        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    public static PubmedArticleSet makeMinimalValidPubmedArticleSet() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        pubmedArticleSet.getPubmedArticleOrPubmedBookArticle().add(ScipamatoPubmedArticleTest.makeMinimalValidPubmedArticle());
        return pubmedArticleSet;
    }

    @Test
    public void gettingPubmedArticleWithPmid_withNoNetwork_returnsEmptyOptional() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenThrow(new RuntimeException("The network is not reachable"));

        Optional<PubmedArticleFacade> pa = service.getPubmedArticleWithPmid(pmId);
        assertThat(pa.isPresent()).isFalse();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
    }

}
