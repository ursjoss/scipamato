package ch.difty.scipamato.publ.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ScipamatoPropertiesTest {

    private final ScipamatoProperties sp = new ScipamatoProperties();

    @Test
    public void brand_hasDefaultValue() {
        assertThat(sp.getBrand()).isEqualTo("SciPaMaTo-Public");
    }

    @Test
    public void defaultLocalization_hasDefaultEnglish() {
        assertThat(sp.getDefaultLocalization()).isEqualTo("en");
    }

    @Test
    public void pubmedBaseUrl_hasDefaultValue() {
        assertThat(sp.getPubmedBaseUrl()).isEqualTo("https://www.ncbi.nlm.nih.gov/pubmed/");
    }

    @Test
    public void gettingRedirectPort_hasNoDefaultValue() {
        assertThat(sp.getRedirectFromPort()).isNull();
    }

    @Test
    public void isCommercialFontPresent() {
        assertThat(sp.isCommercialFontPresent()).isFalse();
    }

    @Test
    public void isLessUsedOverCss() {
        assertThat(sp.isLessUsedOverCss()).isFalse();
    }

    @Test
    public void isNavbarVisibleByDefault() {
        assertThat(sp.isNavbarVisibleByDefault()).isFalse();
    }

    @Test
    public void cmsUrlSearchPage() {
        assertThat(sp.getCmsUrlSearchPage()).isNull();
    }

    @Test
    public void cmsUrlNewStudyPage() {
        assertThat(sp.getCmsUrlNewStudyPage()).isNull();
    }

    @Test
    public void authorsAbbreviatedMaxLength() {
        assertThat(sp.getAuthorsAbbreviatedMaxLength()).isEqualTo(0);
    }

    @Test
    public void responsiveIFrameSupport_isDisabledByDefault() {
        assertThat(sp.isResponsiveIframeSupportEnabled()).isFalse();
    }

    @Test
    public void managementUserName_hasDefaultValue() {
        assertThat(sp.getManagementUserName()).isEqualTo("admin");
    }

    @Test
    public void managementUserPassword_isPresent() {
        assertThat(sp.getManagementUserPassword()).isNull();
    }

    @Test
    public void multiSelectBoxActionBoxWithMoreEntriesThan_hasDefaultValue() {
        assertThat(sp.getMultiSelectBoxActionBoxWithMoreEntriesThan()).isEqualTo(4);
    }

}
