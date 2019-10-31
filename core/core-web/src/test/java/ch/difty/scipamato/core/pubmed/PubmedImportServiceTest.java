package ch.difty.scipamato.core.pubmed;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.persistence.PaperService;
import ch.difty.scipamato.core.persistence.ServiceResult;

@ExtendWith(MockitoExtension.class)
class PubmedImportServiceTest {

    private PubmedImporter pubmedImporter;

    @Mock
    private PubmedArticleService      pubmedArticleServiceMock;
    @Mock
    private PaperService              paperServiceMock;
    @Mock
    private ApplicationCoreProperties applicationPropertiesMock;
    @Mock
    private PubmedArticleFacade       pubmedArticleMock;
    @Mock
    private ServiceResult             serviceResultMock;

    private final List<PubmedArticleFacade> pubmedArticles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        pubmedArticles.add(pubmedArticleMock);
        when(applicationPropertiesMock.getMinimumPaperNumberToBeRecycled()).thenReturn(7L);
        pubmedImporter = new PubmedImportService(pubmedArticleServiceMock, paperServiceMock, applicationPropertiesMock);
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(pubmedArticleServiceMock, paperServiceMock, pubmedArticleMock, serviceResultMock,
            applicationPropertiesMock);
    }

    @Test
    void persistingPubmedArticlesFromXml_withNullXml_fails() {
        final ServiceResult sr = pubmedImporter.persistPubmedArticlesFromXml(null);
        assertThat(sr.getErrorMessages()).containsExactly("xml must not be null.");
        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
    }

    @Test
    void persistingPubmedArticlesFromXml_delegatesExtractionAndPersistingToNestedServices() {
        final long minimumNumber = 7L;
        when(pubmedArticleServiceMock.extractArticlesFrom("content")).thenReturn(pubmedArticles);
        when(paperServiceMock.dumpPubmedArticlesToDb(pubmedArticles, minimumNumber)).thenReturn(serviceResultMock);

        assertThat(pubmedImporter.persistPubmedArticlesFromXml("content")).isEqualTo(serviceResultMock);

        verify(applicationPropertiesMock).getMinimumPaperNumberToBeRecycled();
        verify(pubmedArticleServiceMock).extractArticlesFrom("content");
        verify(paperServiceMock).dumpPubmedArticlesToDb(pubmedArticles, minimumNumber);
    }
}
