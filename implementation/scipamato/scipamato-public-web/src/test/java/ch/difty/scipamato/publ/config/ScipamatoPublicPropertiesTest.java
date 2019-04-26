package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.difty.scipamato.common.config.MavenProperties;

@ExtendWith(MockitoExtension.class)
class ScipamatoPublicPropertiesTest {

    private ScipamatoPublicProperties prop;

    @Mock
    private ScipamatoProperties scipamatoPropMock;
    @Mock
    private MavenProperties     mavenPropMock;

    @BeforeEach
    void setUp() {
        prop = new ScipamatoPublicProperties(scipamatoPropMock, mavenPropMock);
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
    void checkingCommercialFontPresence_ifPresent_delegatesToMavenProp() {
        when(scipamatoPropMock.isCommercialFontPresent()).thenReturn(true);
        assertThat(prop.isCommercialFontPresent()).isEqualTo(true);
        verify(scipamatoPropMock).isCommercialFontPresent();
    }

    @Test
    void checkingCommercialFontPresence_ifNotPresent_delegatesToMavenProp() {
        when(scipamatoPropMock.isCommercialFontPresent()).thenReturn(false);
        assertThat(prop.isCommercialFontPresent()).isEqualTo(false);
        verify(scipamatoPropMock).isCommercialFontPresent();
    }

    @Test
    void gettingRedirectFromPort_delegatesToScipamatoProp() {
        when(scipamatoPropMock.getRedirectFromPort()).thenReturn(5678);
        assertThat(prop.getRedirectFromPort()).isEqualTo(5678);
        verify(scipamatoPropMock).getRedirectFromPort();
    }

    @Test
    void gettingBuildVersion_delegatesToMavenProp() {
        when(mavenPropMock.getVersion()).thenReturn("0.0.1-SNAPSHOT");
        assertThat(prop.getBuildVersion()).isEqualTo("0.0.1-SNAPSHOT");
        verify(mavenPropMock).getVersion();
    }

    @Test
    void checkingLessOverCSS_ifTrue_delegatesToScipamatoProp() {
        when(scipamatoPropMock.isLessUsedOverCss()).thenReturn(true);
        assertThat(prop.isLessUsedOverCss()).isEqualTo(true);
        verify(scipamatoPropMock).isLessUsedOverCss();
    }

    @Test
    void checkingLessOverCSS_ifNotPresent_delegatesToScipamatoProp() {
        when(scipamatoPropMock.isLessUsedOverCss()).thenReturn(false);
        assertThat(prop.isLessUsedOverCss()).isEqualTo(false);
        verify(scipamatoPropMock).isLessUsedOverCss();
    }

    @Test
    void checkingNavbarDefaultVisibility_delegatesToScipamatoProp() {
        when(scipamatoPropMock.isNavbarVisibleByDefault()).thenReturn(true);
        assertThat(prop.isNavbarVisibleByDefault()).isEqualTo(true);
        verify(scipamatoPropMock).isNavbarVisibleByDefault();
    }

    @Test
    void checkingCssUrlSearchPage_delegatesToScipamatoProp() {
        when(scipamatoPropMock.getCmsUrlSearchPage()).thenReturn("http://u.sp");
        assertThat(prop.getCmsUrlSearchPage()).isEqualTo("http://u.sp");
        verify(scipamatoPropMock).getCmsUrlSearchPage();
    }

    @Test
    void checkingCssUrlNewStudyPage_delegatesToScipamatoProp() {
        when(scipamatoPropMock.getCmsUrlNewStudyPage()).thenReturn("http://u.nsp");
        assertThat(prop.getCmsUrlNewStudyPage()).isEqualTo("http://u.nsp");
        verify(scipamatoPropMock).getCmsUrlNewStudyPage();
    }

    @Test
    void checkingAuthorsAbbreviatedMaxLength() {
        when(scipamatoPropMock.getAuthorsAbbreviatedMaxLength()).thenReturn(70);
        assertThat(prop.getAuthorsAbbreviatedMaxLength()).isEqualTo(70);
        verify(scipamatoPropMock).getAuthorsAbbreviatedMaxLength();
    }

    @Test
    void checkingManagementUserName_delegatesToScipamatoProp() {
        when(scipamatoPropMock.getManagementUserName()).thenReturn("un");
        assertThat(prop.getManagementUserName()).isEqualTo("un");
        verify(scipamatoPropMock).getManagementUserName();
    }

    @Test
    void checkingManagementPassword_delegatesToScipamatoProp() {
        when(scipamatoPropMock.getManagementUserPassword()).thenReturn("pw");
        assertThat(prop.getManagementUserPassword()).isEqualTo("pw");
        verify(scipamatoPropMock).getManagementUserPassword();
    }

    @Test
    void checkingNumberOfPreviousNewslettersInArchive() {
        when(scipamatoPropMock.getNumberOfPreviousNewslettersInArchive()).thenReturn(14);
        assertThat(prop.getNumberOfPreviousNewslettersInArchive()).isEqualTo(14);
        verify(scipamatoPropMock).getNumberOfPreviousNewslettersInArchive();
    }

}
