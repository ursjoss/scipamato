package ch.difty.scipamato.publ.web.common;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider;
import ch.difty.scipamato.publ.web.resources.MainCssResourceReference;

public abstract class BasePage<T> extends AbstractPage<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties          applicationProperties;
    @SpringBean
    private ScipamatoWebSessionFacade      sessionFacade;
    @SpringBean
    private CommercialFontResourceProvider fontResourceProvider;

    public BasePage(final PageParameters parameters) {
        super(parameters);
    }

    public BasePage(final IModel<T> model) {
        super(model);
    }

    @Override
    protected ApplicationProperties getProperties() {
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
