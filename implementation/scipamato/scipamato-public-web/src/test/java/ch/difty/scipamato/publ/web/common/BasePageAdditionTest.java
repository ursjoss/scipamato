package ch.difty.scipamato.publ.web.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.config.ScipamatoPublicProperties;
import ch.difty.scipamato.publ.misc.LocaleExtractor;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.WicketTest;
import ch.difty.scipamato.publ.web.resources.MetaOTCssResourceReference;

public class BasePageAdditionTest extends WicketTest {

    @MockBean
    private LocaleExtractor localeExtractor;

    @MockBean(name = "metaOTFontResourceProvider")
    private CommercialFontResourceProvider metaOtFontResourceProvider;

    @Mock
    private ScipamatoPublicProperties applicationProperties;

    private Locale localeZh = Locale.CHINESE;

    private boolean additionalCommercialFontsRendered = false;

    private final PageParameters pp = new PageParameters();

    private BasePage newPageWithParameters(final PageParameters pp) {
        return new BasePage<>(pp) {
            @Override
            protected ApplicationPublicProperties getProperties() {
                return applicationProperties;
            }

            @Override
            protected void renderAdditionalCommercialFonts(final IHeaderResponse response) {
                additionalCommercialFontsRendered = true;
            }
        };
    }

    @Test
    public void callingPageWithoutLocaleIndicatingParentUrl_doesNotSetLocale() {
        assertThat(newPageWithParameters(pp)
            .getSession()
            .getLocale()).isNotEqualTo(localeZh);
    }

    @Test
    public void callingPageWithParentUrlIndicatingChinese_setsChineseLocale() {
        pp.set(PublicPageParameters.PARENT_URL.getName(), "someParentUrlIndicatingChineseLocal");
        when(localeExtractor.extractLocaleFrom("someParentUrlIndicatingChineseLocal")).thenReturn(localeZh);

        assertThat(newPageWithParameters(pp)
            .getSession()
            .getLocale()).isEqualTo(localeZh);
    }

    @Test
    public void page_withCommercialFontsPresent_rendersMetaFontsAndAdditionalCommercialFonts() {
        when(applicationProperties.isCommercialFontPresent()).thenReturn(true);

        when(metaOtFontResourceProvider.getCssResourceReference()).thenReturn(MetaOTCssResourceReference.get());

        getTester().startPage(newPageWithParameters(pp));

        assertThat(getTester()
            .getLastResponseAsString()
            .contains("css/MetaOT-ver")).isTrue();
        assertThat(additionalCommercialFontsRendered).isTrue();

        verify(applicationProperties).isCommercialFontPresent();
        verify(metaOtFontResourceProvider).getCssResourceReference();
    }

    @Test
    public void page_withoutCommercialFontsPresent_rendersNeitherMetaFontsNorAdditionalCommercialFonts() {
        when(applicationProperties.isCommercialFontPresent()).thenReturn(false);

        getTester().startPage(newPageWithParameters(pp));

        assertThat(getTester()
            .getLastResponseAsString()
            .contains("css/MetaOT-ver")).isFalse();
        assertThat(additionalCommercialFontsRendered).isFalse();

        verify(applicationProperties).isCommercialFontPresent();
    }

    @Test
    public void page_withResponsiveIframeSupportEnabled_rendersPym() {
        assertPym(true);
    }

    @Test
    public void page_withoutResponsiveIframeSupportEnabled_doesNotRenderPym() {
        assertPym(false);
    }

    private void assertPym(final boolean render) {
        when(applicationProperties.isResponsiveIframeSupportEnabled()).thenReturn(render);

        getTester().startPage(newPageWithParameters(pp));

        assertThat(getTester()
            .getLastResponseAsString()
            .contains("PymJavaScriptResourceReference/js/pym.v1")).isEqualTo(render);
        assertThat(getTester()
            .getLastResponseAsString()
            .contains("var pymChild = new pym.Child()")).isEqualTo(render);

        verify(applicationProperties).isResponsiveIframeSupportEnabled();
    }

}
