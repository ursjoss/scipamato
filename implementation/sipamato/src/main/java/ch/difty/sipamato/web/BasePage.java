package ch.difty.sipamato.web;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;

public abstract class BasePage extends GenericWebPage<Void> {

    private static final long serialVersionUID = 1L;

    private NotificationPanel feedbackPanel;

    public BasePage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(newNavbar("navbar"));
        createAndAddFeedbackPanel("feedback");
    }

    private void createAndAddFeedbackPanel(String label) {
        feedbackPanel = new NotificationPanel(label);
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
    }

    private Navbar newNavbar(String markupId) {
        Navbar navbar = new Navbar(markupId);

        navbar.setPosition(Navbar.Position.TOP);
        navbar.setBrandName(new ResourceModel("brandname", "SiPaMaTo"));
        navbar.setInverted(true);

        addPageLink(navbar, SipamatoHomePage.class, "Home", GlyphIconType.home);
        addPageLink(navbar, PaperEntryPage.class, "Edit", GlyphIconType.edit);
        addPageLink(navbar, LogoutPage.class, "Logout", GlyphIconType.edit);

        return navbar;
    }

    private <P extends BasePage> void addPageLink(Navbar navbar, Class<P> pageClass, String label, IconType iconType) {
        NavbarButton<Void> button = new NavbarButton<Void>(pageClass, Model.of(label)).setIconType(iconType);
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT, button));
    }

    protected boolean isSignedIn() {
        return AuthenticatedWebSession.get().isSignedIn();
    }

    protected boolean signIn(String username, String password) {
        return AuthenticatedWebSession.get().signIn(username, password);
    }

}
