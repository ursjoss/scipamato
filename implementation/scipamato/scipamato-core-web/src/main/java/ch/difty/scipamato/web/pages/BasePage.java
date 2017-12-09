package ch.difty.scipamato.web.pages;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.ScipamatoSession;
import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.web.AbstractPage;
import ch.difty.scipamato.web.pages.login.LogoutPage;
import ch.difty.scipamato.web.pages.paper.list.PaperListPage;
import ch.difty.scipamato.web.pages.paper.search.PaperSearchPage;
import ch.difty.scipamato.web.pages.sync.RefDataSyncPage;
import ch.difty.scipamato.web.resources.MainCssResourceReference;
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
        addPageLink(nb, PaperListPage.class, "menu.papers", GlyphIconType.list, Navbar.ComponentPosition.LEFT);
        addPageLink(nb, PaperSearchPage.class, "menu.search", GlyphIconType.search, Navbar.ComponentPosition.LEFT);
        addPageLink(nb, RefDataSyncPage.class, "menu.sync", GlyphIconType.export, Navbar.ComponentPosition.LEFT);

        addExternalLink(nb, new StringResourceModel("menu.help.url", this, null).getString(), new StringResourceModel("menu.help", this, null).getString(), GlyphIconType.questionsign,
                Navbar.ComponentPosition.RIGHT);
        addExternalLink(nb, new StringResourceModel("menu.changelog.url", this, null).getString(), getVersionLink(), GlyphIconType.briefcase, Navbar.ComponentPosition.RIGHT);
        addPageLink(nb, LogoutPage.class, "menu.logout", GlyphIconType.edit, Navbar.ComponentPosition.RIGHT);
    }

    private String getVersionLink() {
        return "version " + getProperties().getBuildVersion();
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    protected User getActiveUser() {
        return (User) getAuthentication().getPrincipal();
    }

    protected String getLanguageCode() {
        return ScipamatoSession.get().getLocale().getLanguage();
    }

}
