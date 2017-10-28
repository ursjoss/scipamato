package ch.difty.scipamato.web.pages;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.ScipamatoPublicSession;
import ch.difty.scipamato.web.AbstractPage;
import ch.difty.scipamato.web.pages.portal.PublicPage;
import ch.difty.scipamato.web.resources.MainCssResourceReference;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public abstract class BasePage<T> extends AbstractPage<T> {

    private static final long serialVersionUID = 1L;

    public BasePage(final PageParameters parameters) {
        super(parameters);
    }

    public BasePage(final IModel<T> model) {
        super(model);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(MainCssResourceReference.get()));
    }

    @Override
    protected Navbar newNavbar(String markupId) {
        Navbar nb = new Navbar(markupId);

        nb.setPosition(Navbar.Position.TOP);
        nb.setBrandName(new ResourceModel("brandname", "LUDOK"));
        nb.setInverted(true);

        addPageLink(nb, PublicPage.class, "menu.home", GlyphIconType.home);

        return nb;
    }

    protected boolean isSignedIn() {
        return AuthenticatedWebSession.get().isSignedIn();
    }

    protected boolean signIn(String username, String password) {
        return AuthenticatedWebSession.get().signIn(username, password);
    }

    protected void signOutAndInvalidate() {
        AuthenticatedWebSession.get().invalidate();
    }

    protected String getLanguageCode() {
        return ScipamatoPublicSession.get().getLocale().getLanguage();
    }
}
