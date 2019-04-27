package ch.difty.scipamato.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ResultOfMethodCallIgnored")
class AbstractScipamatoPropertiesTest {

    private AbstractScipamatoProperties<ScipamatoBaseProperties> prop;

    @Mock
    private ScipamatoBaseProperties scipamatoPropMock;
    @Mock
    private MavenProperties         mavenPropMock;

    @BeforeEach
    void setUp() {
        prop = new AbstractScipamatoProperties<>(scipamatoPropMock, mavenPropMock) {
            private static final long serialVersionUID = 1L;
        };
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(scipamatoPropMock, mavenPropMock);
    }

    @Test
    void gettingBrand_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getBrand()).thenReturn("brand");
        assertThat(prop.getBrand()).isEqualTo("brand");
        verify(scipamatoPropMock).getBrand();
    }

    @Test
    void gettingTitleOrBrand_withPageTitleDefined_delegatesToScipamatoProps_andReturnsPageTitle() {
        when(scipamatoPropMock.getPageTitle()).thenReturn("pt");
        assertThat(prop.getTitleOrBrand()).isEqualTo("pt");
        verify(scipamatoPropMock).getPageTitle();
        verify(scipamatoPropMock, never()).getBrand();
    }

    @Test
    void gettingTitleOrBrand_withPageTitleNotDefined_delegatesToScipamatoProps_andReturnsBrand() {
        when(scipamatoPropMock.getBrand()).thenReturn("brand");
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
    void gettingRedirectFromPort_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getRedirectFromPort()).thenReturn(5678);
        assertThat(prop.getRedirectFromPort()).isEqualTo(5678);
        verify(scipamatoPropMock).getRedirectFromPort();
    }

    @Test
    void gettingMultiSelectBoxActionBoxWithMoreEntriesThan_delegatesToScipamatoProps() {
        when(scipamatoPropMock.getMultiSelectBoxActionBoxWithMoreEntriesThan()).thenReturn(4);
        assertThat(prop.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
        verify(scipamatoPropMock).getMultiSelectBoxActionBoxWithMoreEntriesThan();
    }
}
