package ch.difty.scipamato.publ.web.common;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.head.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.misc.LocaleExtractor;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.pym.PymScripts;
import ch.difty.scipamato.publ.web.resources.MainCssResourceReference;
import ch.difty.scipamato.publ.web.resources.PymJavaScriptResourceReference;

@Slf4j
public abstract class BasePage<T> extends AbstractPage<T> {

    private static final long serialVersionUID = 1L;

    protected static final String SELECT_ALL_RESOURCE_TAG   = "multiselect.selectAll";
    protected static final String DESELECT_ALL_RESOURCE_TAG = "multiselect.deselectAll";

    private final boolean showNavbar;

    @SpringBean
    private ApplicationPublicProperties applicationProperties;
    @SpringBean
    private ScipamatoWebSessionFacade   sessionFacade;
    @SpringBean(name = "parentUrlLocaleExtractor")
    private LocaleExtractor             localeExtractor;

    @SpringBean(name = "metaOTFontResourceProvider")
    private CommercialFontResourceProvider metaOtFontResourceProvider;

    protected BasePage(@NotNull final PageParameters parameters) {
        super(parameters);
        final StringValue showNavbarValue = parameters.get(PublicPageParameters.SHOW_NAVBAR.getName());
        this.showNavbar = showNavbarValue.toBoolean(getProperties().isNavbarVisibleByDefault());
        considerSettingLocaleFromParentUrl(parameters);
    }

    protected BasePage(@Nullable final IModel<T> model) {
        super(model);
        this.showNavbar = getProperties().isNavbarVisibleByDefault();
    }

    private void considerSettingLocaleFromParentUrl(final PageParameters parameters) {
        final StringValue puValue = parameters.get(PublicPageParameters.PARENT_URL.getName());
        if (!puValue.isNull()) {
            final Locale locale = localeExtractor.extractLocaleFrom(puValue.toString());
            log.info("Switching Locale to {}", locale.getLanguage());
            getSession().setLocale(locale);
        }
    }

    @Override
    protected boolean isNavbarVisible() {
        return showNavbar;
    }

    @NotNull
    @Override
    protected ApplicationPublicProperties getProperties() {
        return applicationProperties;
    }

    @Override
    public void renderHead(@NotNull IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(MainCssResourceReference.get()));

        if (getProperties().isCommercialFontPresent())
            renderCommercialFonts(response);

        if (getProperties().isResponsiveIframeSupportEnabled())
            renderPymForResponsiveIframe(response);
    }

    private void renderCommercialFonts(final IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(metaOtFontResourceProvider.getCssResourceReference()));
        renderAdditionalCommercialFonts(response);
    }

    /**
     * Override to render page specific additional commercial fonts. Note: This code
     * is only called if the property {@code commercial-font-present} is set to
     * {@literal true}.
     *
     * @param response
     *     the response to render the css header item references on
     */
    protected void renderAdditionalCommercialFonts(@NotNull final IHeaderResponse response) {
    }

    /**
     * Adds pym.js to the page and instantiates the <code>pymChild</code>.
     *
     * @param response
     *     the response to render the javascript for
     */
    private void renderPymForResponsiveIframe(final IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(PymJavaScriptResourceReference.get()));
        response.render(
            new JavaScriptContentHeaderItem(PymScripts.INSTANTIATE.script, PymScripts.INSTANTIATE.id, null));
        response.render(new OnLoadHeaderItem(PymScripts.RESIZE.script));
    }

    @NotNull
    protected String getLanguageCode() {
        return sessionFacade.getLanguageCode();
    }

    @NotNull
    protected ItemNavigator<Long> getPaperIdManager() {
        return sessionFacade.getPaperIdManager();
    }
}
