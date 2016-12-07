package ch.difty.sipamato.web.pages;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.sipamato.web.pages.home.SipamatoHomePage;
import ch.difty.sipamato.web.pages.login.LogoutPage;
import ch.difty.sipamato.web.pages.paper.list.PaperListPage;
import ch.difty.sipamato.web.pages.paper.search.PaperSearchPage;
import ch.difty.sipamato.web.resources.MainCssResourceReference;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;

public abstract class BasePage<T> extends GenericWebPage<T> {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_TAG = "Label";
    protected static final String LABEL_RECOURCE_TAG = ".label";
    protected static final String PANEL_HEADER_RESOURCE_TAG = ".header";

    private NotificationPanel feedbackPanel;
    private Navbar navBar;

    public BasePage(final PageParameters parameters) {
        super(parameters);
    }

    public BasePage(final IModel<T> model) {
        super(model);
    }

    protected Navbar getNavBar() {
        return navBar;
    }

    protected NotificationPanel getFeedbackPanel() {
        return feedbackPanel;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(MainCssResourceReference.get()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        createAndAddNavBar("navbar");
        createAndAddFeedbackPanel("feedback");
        createAndAddDebugBar("debug");
    }

    private void createAndAddNavBar(String id) {
        navBar = newNavbar(id);
        queue(navBar);
        extendNavBar();
    }

    /**
     * Override if you need to extend the {@link Navbar}
     */
    protected void extendNavBar() {
    }

    private void createAndAddFeedbackPanel(String label) {
        feedbackPanel = new NotificationPanel(label);
        feedbackPanel.setOutputMarkupId(true);
        queue(feedbackPanel);
    }

    private void createAndAddDebugBar(String label) {
        if (getApplication().getDebugSettings().isDevelopmentUtilitiesEnabled()) {
            queue(new DebugBar(label).positionBottom());
        } else {
            queue(new EmptyPanel(label).setVisible(false));
        }
    }

    private Navbar newNavbar(String markupId) {
        Navbar navbar = new Navbar(markupId);

        navbar.setPosition(Navbar.Position.TOP);
        navbar.setBrandName(new ResourceModel("brandname", "SiPaMaTo"));
        navbar.setInverted(true);

        addPageLink(navbar, SipamatoHomePage.class, new StringResourceModel("menu.home", this, null).getString(), GlyphIconType.home);
        addPageLink(navbar, PaperListPage.class, new StringResourceModel("menu.papers", this, null).getString(), GlyphIconType.list);
        addPageLink(navbar, PaperSearchPage.class, new StringResourceModel("menu.search", this, null).getString(), GlyphIconType.search);
        addPageLink(navbar, LogoutPage.class, new StringResourceModel("menu.logout", this, null).getString(), GlyphIconType.edit);

        return navbar;
    }

    private <P extends BasePage<?>> void addPageLink(Navbar navbar, Class<P> pageClass, String label, IconType iconType) {
        NavbarButton<Void> button = new NavbarButton<Void>(pageClass, Model.of(label)).setIconType(iconType);
        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT, button));
    }

    protected boolean isSignedIn() {
        return AuthenticatedWebSession.get().isSignedIn();
    }

    protected boolean signIn(String username, String password) {
        return AuthenticatedWebSession.get().signIn(username, password);
    }

    protected void queueFieldAndLabel(FormComponent<?> field, Optional<PropertyValidator<?>> pv) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        queue(field);
        if (pv.isPresent()) {
            field.add(pv.get());
        }
    }

    protected void queueResponsePageButton(final String id, BasePage<?> responsePage) {
        BootstrapAjaxButton newButton = new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null), Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                setResponsePage(responsePage);
            }
        };
        queue(newButton);
    }

    protected void queuePanelHeadingFor(String id) {
        queue(new Label(id + LABEL_TAG, new StringResourceModel(id + PANEL_HEADER_RESOURCE_TAG, this, null)));
    }

}
