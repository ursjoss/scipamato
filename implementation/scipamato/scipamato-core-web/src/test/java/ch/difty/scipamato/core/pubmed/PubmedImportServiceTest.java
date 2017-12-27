package ch.difty.scipamato.core.pubmed;

import static ch.difty.scipamato.common.TestUtils.assertDegenerateSupplierParameter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.difty.scipamato.common.config.core.ApplicationProperties;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.persistence.ServiceResult;

@RunWith(MockitoJUnitRunner.class)
public class PubmedImportServiceTest {

    private PubmedImporter pubmedImporter;

    @Mock
    private PubmedArticleService  pubmedArticleServiceMock;
    @Mock
    private PaperService          paperServiceMock;
    @Mock
    private ApplicationProperties applicationPropertiesMock;
    @Mock
    private PubmedArticleFacade   pubmedArticleMock;
    @Mock
    private ServiceResult         serviceResultMock;

    private final List<PubmedArticleFacade> pubmedArticles = new ArrayList<>();

    @Before
    public void setUp() {
        pubmedArticles.add(pubmedArticleMock);
        when(applicationPropertiesMock.getMinimumPaperNumberToBeRecycled()).thenReturn(7l);
        pubmedImporter = new PubmedImportService(pubmedArticleServiceMock, paperServiceMock, applicationPropertiesMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(pubmedArticleServiceMock, paperServiceMock, pubmedArticleMock, serviceResultMock,
            applicationPropertiesMock);
    }

    @Test
    public void degenerateConstruction_withNullPubmedArticleService_throws() {
        assertDegenerateSupplierParameter(
            () -> new PubmedImportService(null, paperServiceMock, applicationPropertiesMock), "pubmedArticleService");
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
    }

    @Test
    public void degenerateConstruction_withNullSaperService_throws() {
        assertDegenerateSupplierParameter(
            () -> new PubmedImportService(pubmedArticleServiceMock, null, applicationPropertiesMock), "paperService");
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
    }

    @Test
    public void degenerateConstruction_withNullApplicationProperties_throws() {
        assertDegenerateSupplierParameter(
            () -> new PubmedImportService(pubmedArticleServiceMock, paperServiceMock, null), "applicationProperties");
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
    }

    @Test
    public void persistingPubmedArticlesFromXml_withNullXml_fails() {
        final ServiceResult sr = pubmedImporter.persistPubmedArticlesFromXml(null);
        assertThat(sr.getErrorMessages()).containsExactly("xml must not be null.");
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
    }

    @Test
    public void persistingPubmedArticlesFromXml_delegatesExtractionAndPersistingToNestedServices() {
        final long minimumNumber = 7l;
        when(pubmedArticleServiceMock.extractArticlesFrom("content")).thenReturn(pubmedArticles);
        when(paperServiceMock.dumpPubmedArticlesToDb(pubmedArticles, minimumNumber)).thenReturn(serviceResultMock);

        assertThat(pubmedImporter.persistPubmedArticlesFromXml("content")).isEqualTo(serviceResultMock);

        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
        verify(pubmedArticleServiceMock).extractArticlesFrom("content");
        verify(paperServiceMock).dumpPubmedArticlesToDb(pubmedArticles, minimumNumber);
    }
}
