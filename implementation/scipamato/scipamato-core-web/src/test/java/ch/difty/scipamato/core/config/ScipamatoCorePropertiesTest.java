package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.config.MavenProperties;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

@SuppressWarnings("ResultOfMethodCallIgnored")
@ExtendWith(MockitoExtension.class)
public class ScipamatoCorePropertiesTest {

    private ScipamatoCoreProperties prop;

    @Mock
    private ScipamatoProperties scipamatoPropMock;
    @Mock
    private MavenProperties     mavenPropMock;

    @BeforeEach
    public void setUp() {
        prop = new ScipamatoCoreProperties(scipamatoPropMock, mavenPropMock);
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(scipamatoPropMock, mavenPropMock);
    }

    @Test
    void gettingBrand_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getBrand()).thenReturn("brand");
        assertThat(prop.getBrand()).isEqualTo("brand");
        verify(scipamatoPropMock).getBrand();
    }

    @Test
    public void gettingTitleOrBrand_withPageTitleDefined_delegatesToScipamatoProps_andReturnsPageTitle() {
        when(scipamatoPropMock.getPageTitle()).thenReturn("pt");
        assertThat(prop.getTitleOrBrand()).isEqualTo("pt");
        verify(scipamatoPropMock).getPageTitle();
        verify(scipamatoPropMock, never()).getBrand();
    }

    @Test
    public void gettingTitleOrBrand_withPageTitleNotDefined_delegatesToScipamatoProps_andReturnsBrand() {
        when(scipamatoPropMock.getPageTitle()).thenReturn(null);
        assertThat(prop.getTitleOrBrand()).isEqualTo("brand");
        verify(scipamatoPropMock).getPageTitle();
        verify(scipamatoPropMock).getBrand();
    }

    @Test
    void gettingDefaultLocalization_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getDefaultLocalization()).thenReturn("dl");
        assertThat(prop.getDefaultLocalization()).isEqualTo("dl");
        verify(scipamatoPropMock).getDefaultLocalization();
    }

    @Test
    void gettingPubmedBaseUrl_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getPubmedBaseUrl()).thenReturn("pbUrl");
        assertThat(prop.getPubmedBaseUrl()).isEqualTo("pbUrl");
        verify(scipamatoPropMock).getPubmedBaseUrl();
    }

    @Test
    void gettingBuildVersion_delegatesToMavenProp() {
        when(mavenPropMock.getVersion()).thenReturn("0.0.1-SNAPSHOT");
        assertThat(prop.getBuildVersion()).isEqualTo("0.0.1-SNAPSHOT");
        verify(mavenPropMock).getVersion();
    }

    @Test
    void gettingAuthorParserStrategy_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.PUBMED);
        assertThat(prop.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.PUBMED);
        verify(scipamatoPropMock).getAuthorParserStrategy();
    }

    @Test
    void gettingPaperNumberMin2BeRecycled_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getPaperNumberMinimumToBeRecycled()).thenReturn(100);
        assertThat(prop.getMinimumPaperNumberToBeRecycled()).isEqualTo(100);
        verify(scipamatoPropMock).getPaperNumberMinimumToBeRecycled();
    }

    @Test
    void gettingRedirectFromPort_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getRedirectFromPort()).thenReturn(5678);
        assertThat(prop.getRedirectFromPort()).isEqualTo(5678);
        verify(scipamatoPropMock).getRedirectFromPort();
    }

    @Test
    public void gettingPubmedApiKey_() {
        when(scipamatoPropMock.getPubmedApiKey()).thenReturn("ak");
        assertThat(prop.getPubmedApiKey()).isEqualTo("ak");
        verify(scipamatoPropMock).getPubmedApiKey();
    }

}
