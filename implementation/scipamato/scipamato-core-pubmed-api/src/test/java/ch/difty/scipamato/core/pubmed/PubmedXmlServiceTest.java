package ch.difty.scipamato.core.pubmed;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.lang.Object;
import java.util.ArrayList;
import java.util.List;

import feign.FeignException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.core.pubmed.api.*;

@ExtendWith(MockitoExtension.class)
public class PubmedXmlServiceTest {

    private PubmedXmlService service;

    @Mock
    private Jaxb2Marshaller    unmarshallerMock;
    @Mock
    private PubMed             pubMedMock;
    @Mock
    private PubmedArticleSet   pubmedArticleSetMock;
    @Mock
    private PubmedArticle      pubmedArticleMock;
    @Mock
    private MedlineCitation    medLineCitationMock;
    @Mock
    private Article            articleMock;
    @Mock
    private Journal            journalMock;
    @Mock
    private PMID               pmidMock;
    @Mock
    private JournalIssue       journalIssueMock;
    @Mock
    private PubDate            pubDateMock;
    @Mock
    private MedlineJournalInfo medLineJournalInfoMock;
    @Mock
    private ArticleTitle       articleTitleMock;
    @Mock
    private FeignException     feignExceptionMock;

    @BeforeEach
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

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(unmarshallerMock, pubMedMock, pubmedArticleSetMock);
    }

    @Test
    public void degenerateConstruction_nullUnmarshaller_throws() {
        assertDegenerateSupplierParameter(() -> new PubmedXmlService(null, pubMedMock), "unmarshaller");
    }

    @Test
    public void degenerateConstruction_nullPubMed_throws() {
        assertDegenerateSupplierParameter(() -> new PubmedXmlService(unmarshallerMock, null), "pubMed");
    }

    @Test
    public void unmarshallingNull_throws() {
        try {
            service.unmarshal(null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("xmlString must not be null.");
        }
    }

    @Test
    public void gettingPubmedArticleWithPmid_withValidId_returnsArticle() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        final List<Object> objects = new ArrayList<>();
        objects.add(pubmedArticleMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        PubmedArticleResult pr = service.getPubmedArticleWithPmid(pmId);
        assertThat(pr.getPubmedArticleFacade()).isNotNull();
        assertThat(pr.getErrorMessage()).isNull();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmid_withInvalidId_returnsNullFacade() {
        final int pmId = 999999999;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        final List<java.lang.Object> objects = new ArrayList<>();
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        PubmedArticleResult pr = service.getPubmedArticleWithPmid(pmId);
        assertThat(pr.getPubmedArticleFacade()).isNull();
        assertThat(pr.getErrorMessage()).isEqualTo("PMID " + pmId + " seems to be undefined in PubMed.");

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmid_withNullObjects_returnsNullFacade() {
        final int pmId = 999999999;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenReturn(pubmedArticleSetMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(null);

        PubmedArticleResult pr = service.getPubmedArticleWithPmid(pmId);
        assertThat(pr.getPubmedArticleFacade()).isNull();
        assertThat(pr.getErrorMessage()).isNull();

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticleWithPmidAndApiKey_withValidId_returnsArticle() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId), "key")).thenReturn(pubmedArticleSetMock);
        final List<java.lang.Object> objects = new ArrayList<>();
        objects.add(pubmedArticleMock);
        when(pubmedArticleSetMock.getPubmedArticleOrPubmedBookArticle()).thenReturn(objects);

        PubmedArticleResult pr = service.getPubmedArticleWithPmidAndApiKey(pmId, "key");
        assertThat(pr.getPubmedArticleFacade()).isNotNull();
        assertThat(pr.getErrorMessage()).isNull();

        verify(pubMedMock).articleWithId(String.valueOf(pmId), "key");
        verify(pubmedArticleSetMock).getPubmedArticleOrPubmedBookArticle();
    }

    @Test
    public void gettingPubmedArticle_withInvalidId_returnsEmptyArticleAndRawExceptionMessage() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId), "key")).thenThrow(new RuntimeException("boom"));

        PubmedArticleResult pr = service.getPubmedArticleWithPmidAndApiKey(pmId, "key");
        assertThat(pr.getPubmedArticleFacade()).isNull();
        assertThat(pr.getErrorMessage()).isEqualTo("boom");

        verify(pubMedMock).articleWithId(String.valueOf(pmId), "key");
    }

    @Test
    public void nonValidXml_returnsNull() throws XmlMappingException, IOException {
        assertThat(service.unmarshal("")).isNull();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withUnmarshallerException_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenThrow(new UnmarshallingFailureException("boom"));
        assertThat(service.extractArticlesFrom("some invalid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPubmedArticleSetWithoutArticleCollection_returnsEmptyList() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(pubmedArticleSet);
        assertThat(service.extractArticlesFrom("some valid xml")).isEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    @Test
    public void gettingArticles_withPubmedArticleSetWithoutArticleCollection2_returnsEmptyList() {
        when(unmarshallerMock.unmarshal(isA(StreamSource.class))).thenReturn(makeMinimalValidPubmedArticleSet());
        assertThat(service.extractArticlesFrom("some valid xml")).isNotEmpty();
        verify(unmarshallerMock).unmarshal(isA(StreamSource.class));
    }

    public static PubmedArticleSet makeMinimalValidPubmedArticleSet() {
        PubmedArticleSet pubmedArticleSet = new PubmedArticleSet();
        pubmedArticleSet
            .getPubmedArticleOrPubmedBookArticle()
            .add(ScipamatoPubmedArticleTest.makeMinimalValidPubmedArticle());
        return pubmedArticleSet;
    }

    @Test
    public void gettingPubmedArticleWithPmid_withParsableHtmlError502_hasHttpStatusPopulated() {
        final int pmId = 25395026;
        feignExceptionFixture(502, "status 502 reading PubMed#articleWithId(String,String); content: \nfoo");
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenThrow(feignExceptionMock);

        PubmedArticleResult pr = service.getPubmedArticleWithPmid(pmId);
        assertThat(pr.getPubmedArticleFacade()).isNull();
        assertThat(pr.getErrorMessage()).isEqualTo(
            "Status 502 BAD_GATEWAY: status 502 reading PubMed#articleWithId(String,String); content: \nfoo");

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
    }

    @Test
    public void gettingPubmedArticleWithPmid_withParsableHtmlError400_hasHttpStatusPopulated() {
        final int pmId = 25395026;
        feignExceptionFixture(400, "status 400 reading PubMed#articleWithId(String,String); content:\n"
                                   + "{\"error\":\"API key invalid\",\"api-key\":\"xxx\",\"type\":\"invalid\",\"status\":\"unknown\"}");
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenThrow(feignExceptionMock);

        PubmedArticleResult pr = service.getPubmedArticleWithPmid(pmId);
        assertThat(pr.getPubmedArticleFacade()).isNull();
        assertThat(pr.getErrorMessage()).isEqualTo("Status 400 BAD_REQUEST: API key invalid");

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
    }

    private void feignExceptionFixture(final int status, final String msg) {
        when(feignExceptionMock.status()).thenReturn(status);
        when(feignExceptionMock.getLocalizedMessage()).thenReturn(msg);
    }

    @Test
    public void gettingPubmedArticleWithPmid_withParsableHtmlError400_hasHttpStatusPopulated2() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenThrow(new RuntimeException(
            "status 400 reading PubMed#articleWithId(String,String); content:\n"
            + "{\"error\":\"API key invalid\",\"api-key\":\"xxx\",\"type\":\"invalid\",\"status\":\"unknown\"}"));

        PubmedArticleResult pr = service.getPubmedArticleWithPmid(pmId);
        assertThat(pr.getPubmedArticleFacade()).isNull();
        assertThat(pr.getErrorMessage()).isEqualTo("status 400 reading PubMed#articleWithId(String,String); content:\n"
                                                   + "{\"error\":\"API key invalid\",\"api-key\":\"xxx\",\"type\":\"invalid\",\"status\":\"unknown\"}");

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
    }

    @Test
    public void gettingPubmedArticleWithPmid_withNoParsableHtmlError_onlyHasMessage() {
        final int pmId = 25395026;
        when(pubMedMock.articleWithId(String.valueOf(pmId))).thenThrow(
            new RuntimeException("The network is not reachable"));

        PubmedArticleResult pr = service.getPubmedArticleWithPmid(pmId);
        assertThat(pr.getPubmedArticleFacade()).isNull();
        assertThat(pr.getErrorMessage()).isEqualTo("The network is not reachable");

        verify(pubMedMock).articleWithId(String.valueOf(pmId));
    }

}
