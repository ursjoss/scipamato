package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties();

    @Test
    void brand_hasDefaultValue() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo-Public");
    }

    @Test
    void defaultLocalization_hasDefaultEnglish() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("en");
    }

    @Test
    void pubmedBaseUrl_hasDefaultValue() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    void gettingRedirectPort_hasNoDefaultValue() {
        assertThat(sp.getRedirectFromPort()).isNull();
    }

    @Test
    void isCommercialFontPresent() {
        assertThat(sp.isCommercialFontPresent()).isFalse();
    }

    @Test
    void isLessUsedOverCss() {
        assertThat(sp.isLessUsedOverCss()).isFalse();
    }

    @Test
    void isNavbarVisibleByDefault() {
        assertThat(sp.isNavbarVisibleByDefault()).isFalse();
    }

    @Test
    void cmsUrlSearchPage() {
        assertThat(sp.getCmsUrlSearchPage()).isNull();
    }

    @Test
    void cmsUrlNewStudyPage() {
        assertThat(sp.getCmsUrlNewStudyPage()).isNull();
    }

    @Test
    void authorsAbbreviatedMaxLength() {
        assertThat(sp.getAuthorsAbbreviatedMaxLength()).isEqualTo(0);
    }

    @Test
    void responsiveIFrameSupport_isDisabledByDefault() {
        assertThat(sp.isResponsiveIframeSupportEnabled()).isFalse();
    }

    @Test
    void managementUserName_hasDefaultValue() {
        assertThat(sp.getManagementUserName()).isEqualTo("admin");
    }

    @Test
    void managementUserPassword_isPresent() {
        assertThat(sp.getManagementUserPassword()).isNull();
    }

    @Test
    void multiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue() {
        assertThat(sp.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
    }

}
