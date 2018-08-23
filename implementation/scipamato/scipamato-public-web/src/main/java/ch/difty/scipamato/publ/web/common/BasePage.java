package ch.difty.scipamato.publ.web.common;

import org.apache.wicket.markup.head.*;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.PublicPageParameters;
import ch.difty.scipamato.publ.web.pym.PymScripts;
import ch.difty.scipamato.publ.web.resources.MainCssResourceReference;
import ch.difty.scipamato.publ.web.resources.PymJavaScriptResourceReference;

public abstract class BasePage<T> extends AbstractPage<T> {

    private static final long serialVersionUID = 1L;

    private final boolean showNavbar;

    @SpringBean
    private ApplicationPublicProperties applicationProperties;
    @SpringBean
    private ScipamatoWebSessionFacade   sessionFacade;

    @SpringBean(name = "metaOTFontResourceProvider")
    private CommercialFontResourceProvider metaOtFontResourceProvider;

    public BasePage(final PageParameters parameters) {
        super(parameters);
        final StringValue showNavbarValue = parameters.get(PublicPageParameters.SHOW_NAVBAR.getName());
        this.showNavbar = showNavbarValue.toBoolean(applicationProperties.isNavbarVisibleByDefault());
    }

    public BasePage(final IModel<T> model) {
        super(model);
        this.showNavbar = applicationProperties.isNavbarVisibleByDefault();
    }

    @Override
    protected boolean isNavbarVisible() {
        return showNavbar;
    }

    @Override
    protected ApplicationPublicProperties getProperties() {
        return applicationProperties;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(MainCssResourceReference.get()));

        if (applicationProperties.isCommercialFontPresent())
            renderCommercialFonts(response);

        if (applicationProperties.isResponsiveIframeSupportEnabled())
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
    protected void renderAdditionalCommercialFonts(final IHeaderResponse response) {
    }

    /**
     * Adds pym.js to the page and instantiates the <code>pymChild</code>.
     *
     * @param response
     *     the response to render the javascript for
     */
    private void renderPymForResponsiveIframe(final IHeaderResponse response) {
        response.render(JavaScriptHeaderItem.forReference(PymJavaScriptResourceReference.get()));
//        response.render(CssHeaderItem.forCSS("html, body { width:auto; height:auto; }", "pymCSS"));
        response.render(
            new JavaScriptContentHeaderItem(PymScripts.INSTANTIATE.script, PymScripts.INSTANTIATE.id, null));
//        response.render(OnLoadHeaderItem.forScript(PymScripts.RESIZE.script));
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder
            .getContext()
            .getAuthentication();
    }

    protected String getLanguageCode() {
        return sessionFacade.getLanguageCode();
    }

    protected ItemNavigator<Long> getPaperIdManager() {
        return sessionFacade.getPaperIdManager();
    }

}
