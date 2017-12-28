package ch.difty.scipamato.public_.web.pages;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.public_.ScipamatoPublicSession;
import ch.difty.scipamato.public_.web.pages.portal.PublicPage;
import ch.difty.scipamato.public_.web.resources.MainCssResourceReference;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public abstract class BasePage<T> extends AbstractPage<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties applicationProperties;

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
    }

    @Override
    protected void addLinksTo(Navbar nb) {
        super.addLinksTo(nb);
        addPageLink(nb, PublicPage.class, "menu.home", GlyphIconType.home, Navbar.ComponentPosition.LEFT);
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext()
            .getAuthentication();
    }

    protected String getLanguageCode() {
        return ScipamatoPublicSession.get()
            .getLocale()
            .getLanguage();
    }

}
