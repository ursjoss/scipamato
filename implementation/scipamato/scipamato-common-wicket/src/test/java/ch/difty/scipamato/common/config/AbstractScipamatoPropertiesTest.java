package ch.difty.scipamato.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
public class AbstractScipamatoPropertiesTest {

    private AbstractScipamatoProperties<ScipamatoBaseProperties> prop;

    @Mock
    private ScipamatoBaseProperties scipamatoPropMock;
    @Mock
    private MavenProperties         mavenPropMock;

    @Before
    public void setUp() {
        prop = new AbstractScipamatoProperties<>(scipamatoPropMock, mavenPropMock) {
            private static final long serialVersionUID = 1L;
        };

        when(scipamatoPropMock.getBrand()).thenReturn("brand");
        when(scipamatoPropMock.getDefaultLocalization()).thenReturn("dl");
        when(scipamatoPropMock.getPubmedBaseUrl()).thenReturn("pbUrl");
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
    public void gettingRedirectFromPort_delegatesToScipamatoProps() {
        assertThat(prop.getRedirectFromPort()).isEqualTo(5678);
        verify(scipamatoPropMock).getRedirectFromPort();
    }

    @Test
    public void gettingMultiSelectBoxActionBoxWithMoreEntriesThan_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getMultiSelectBoxActionBoxWithMoreEntriesThan()).thenReturn(4);
        assertThat(prop.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
        verify(scipamatoPropMock).getMultiSelectBoxActionBoxWithMoreEntriesThan();
    }
}
