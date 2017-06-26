package ch.difty.scipamato.pubmed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
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

import ch.difty.scipamato.config.ApplicationProperties;
import ch.difty.scipamato.lib.NullArgumentException;
import ch.difty.scipamato.pubmed.entity.PubmedArticleFacade;
import ch.difty.scipamato.service.PaperService;
import ch.difty.scipamato.service.PubmedArticleService;
import ch.difty.scipamato.service.PubmedImporter;
import ch.difty.scipamato.service.ServiceResult;

@RunWith(MockitoJUnitRunner.class)
public class PubmedImportServiceTest {

    private PubmedImporter pubmedImporter;

    @Mock
    private PubmedArticleService pubmedArticleServiceMock;
    @Mock
    private PaperService paperServiceMock;
    @Mock
    private ApplicationProperties applicationPropertiesMock;
    @Mock
    private PubmedArticleFacade pubmedArticleMock;
    @Mock
    private ServiceResult serviceResultMock;

    private final List<PubmedArticleFacade> pubmedArticles = new ArrayList<>();

    @Before
    public void setUp() {
        pubmedArticles.add(pubmedArticleMock);
        when(applicationPropertiesMock.getMinimumPaperNumberToBeRecycled()).thenReturn(7l);
        pubmedImporter = new PubmedImportService(pubmedArticleServiceMock, paperServiceMock, applicationPropertiesMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(pubmedArticleServiceMock, paperServiceMock, pubmedArticleMock, serviceResultMock, applicationPropertiesMock);
    }

    @Test
    public void degenerateConstruction_withNullPubmedArticleService_throws() {
        try {
            new PubmedImportService(null, paperServiceMock, applicationPropertiesMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("pubmedArticleService must not be null.");
        }
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
    }

    @Test
    public void degenerateConstruction_withNullSaperService_throws() {
        try {
            new PubmedImportService(pubmedArticleServiceMock, null, applicationPropertiesMock);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("paperService must not be null.");
        }
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
    }

    @Test
    public void degenerateConstruction_withNullApplicationProperties_throws() {
        try {
            new PubmedImportService(pubmedArticleServiceMock, paperServiceMock, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NullArgumentException.class).hasMessage("applicationProperties must not be null.");
        }
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
