package ch.difty.scipamato.publ.web.common;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.config.ApplicationPublicProperties;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.PageParameters;
import ch.difty.scipamato.publ.web.resources.MainCssResourceReference;

public abstract class BasePage<T> extends AbstractPage<T> {

    private static final long serialVersionUID = 1L;

    private final boolean showNavbar;

    @SpringBean
    private ApplicationPublicProperties    applicationProperties;
    @SpringBean
    private ScipamatoWebSessionFacade      sessionFacade;
    @SpringBean
    private CommercialFontResourceProvider fontResourceProvider;

    public BasePage(final org.apache.wicket.request.mapper.parameter.PageParameters parameters) {
        super(parameters);
        final StringValue showNavbarValue = parameters.get(PageParameters.SHOW_NAVBAR.getName());
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

        if (fontResourceProvider.isCommercialFontPresent())
            response.render(CssHeaderItem.forReference(fontResourceProvider.getCssResourceReference()));
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext()
            .getAuthentication();
    }

    protected String getLanguageCode() {
        return sessionFacade.getLanguageCode();
    }

    protected ItemNavigator<Long> getPaperIdManager() {
        return sessionFacade.getPaperIdManager();
    }

}
