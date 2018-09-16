package ch.difty.scipamato.core.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.difty.scipamato.common.config.MavenProperties;
import ch.difty.scipamato.core.logic.parsing.AuthorParserStrategy;

@SuppressWarnings("ResultOfMethodCallIgnored")
@RunWith(MockitoJUnitRunner.class)
public class ScipamatoCorePropertiesTest {

    private ScipamatoCoreProperties prop;

    @Mock
    private ScipamatoProperties scipamatoPropMock;
    @Mock
    private MavenProperties     mavenPropMock;

    @Before
    public void setUp() {
        prop = new ScipamatoCoreProperties(scipamatoPropMock, mavenPropMock);

        when(scipamatoPropMock.getBrand()).thenReturn("brand");
        when(scipamatoPropMock.getDefaultLocalization()).thenReturn("dl");
        when(scipamatoPropMock.getPubmedBaseUrl()).thenReturn("pbUrl");
        when(scipamatoPropMock.getAuthorParserStrategy()).thenReturn(AuthorParserStrategy.PUBMED);
        when(scipamatoPropMock.getPaperNumberMinimumToBeRecycled()).thenReturn(100);
        when(scipamatoPropMock.getRedirectFromPort()).thenReturn(5678);

        when(mavenPropMock.getVersion()).thenReturn("0.0.1-SNAPSHOT");
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(scipamatoPropMock, mavenPropMock);
    }

    @Test
    public void gettingBrand_delegatesToScipamatoProps() {
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
    public void gettingDefaultLocalization_delegatesToScipamatoProps() {
        assertThat(prop.getDefaultLocalization()).isEqualTo("dl");
        verify(scipamatoPropMock).getDefaultLocalization();
    }

    @Test
    public void gettingPubmedBaseUrl_delegatesToScipamatoProps() {
        assertThat(prop.getPubmedBaseUrl()).isEqualTo("pbUrl");
        verify(scipamatoPropMock).getPubmedBaseUrl();
    }

    @Test
    public void gettingBuildVersion_delegatesToMavenProp() {
        assertThat(prop.getBuildVersion()).isEqualTo("0.0.1-SNAPSHOT");
        verify(mavenPropMock).getVersion();
    }

    @Test
    public void gettingAuthorParserStrategy_delegatesToScipamatoProps() {
        assertThat(prop.getAuthorParserStrategy()).isEqualTo(AuthorParserStrategy.PUBMED);
        verify(scipamatoPropMock).getAuthorParserStrategy();
    }

    @Test
    public void gettingPaperNumberMin2BeRecycled_delegatesToScipamatoProps() {
        assertThat(prop.getMinimumPaperNumberToBeRecycled()).isEqualTo(100);
        verify(scipamatoPropMock).getPaperNumberMinimumToBeRecycled();
    }

    @Test
    public void gettingRedirectFromPort_delegatesToScipamatoProps() {
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
