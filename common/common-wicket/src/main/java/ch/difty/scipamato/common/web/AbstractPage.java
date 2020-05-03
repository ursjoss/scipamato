package ch.difty.scipamato.common.web;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons.Type;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.MetaDataHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.GenericWebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.settings.DebugSettings;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.component.SerializableSupplier;

@SuppressWarnings({ "SameParameterValue", "WeakerAccess", "unused", "SpellCheckingInspection" })
public abstract class AbstractPage<T> extends GenericWebPage<T> {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_TAG                 = WicketUtils.LABEL_TAG;
    protected static final String LABEL_RESOURCE_TAG        = WicketUtils.LABEL_RESOURCE_TAG;
    protected static final String LOADING_RESOURCE_TAG      = WicketUtils.LOADING_RESOURCE_TAG;
    protected static final String PANEL_HEADER_RESOURCE_TAG = WicketUtils.PANEL_HEADER_RESOURCE_TAG;
    protected static final String TITLE_RESOURCE_TAG        = WicketUtils.TITLE_RESOURCE_TAG;

    @SpringBean
    private DateTimeService dateTimeService;

    private NotificationPanel feedbackPanel;
    private Navbar            navbar;

    public AbstractPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    public AbstractPage(@Nullable final IModel<T> model) {
        super(model);
    }

    @NotNull
    protected DateTimeService getDateTimeService() {
        return dateTimeService;
    }

    @Nullable
    protected Navbar getNavBar() {
        return navbar;
    }

    @Nullable
    public NotificationPanel getFeedbackPanel() {
        return feedbackPanel;
    }

    @Override
    public void renderHead(@NotNull IHeaderResponse response) {
        super.renderHead(response);

        response.render(new PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("charset", "utf-8")));
        response.render(new PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("X-UA-Compatible", "IE=edge")));
        response.render(
            new PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("viewport", "width=device-width, initial-scale=1")));
        response.render(
            new PriorityHeaderItem(MetaDataHeaderItem.forMetaTag("Content-Type", "text/html; charset=UTF-8")));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        createAndAddTitle("pageTitle");
        createAndAddNavBar("navbar");
        createAndAddFeedbackPanel("feedback");
        createAndAddDebugBar("debug");
        createAndAddFooterContainer("footer-container");
    }

    private void createAndAddTitle(String id) {
        queue(new Label(id, getTitle()));
    }

    private IModel<String> getTitle() {
        return Model.of(getProperties().getTitleOrBrand());
    }

    private void createAndAddNavBar(String id) {
        navbar = newNavbar(id);
        queue(navbar);
        extendNavBar();
    }

    /**
     * Override if you need to extend the {@link Navbar}
     */
    @SuppressWarnings("EmptyMethod")
    protected void extendNavBar() {
    }

    private void createAndAddFeedbackPanel(String label) {
        feedbackPanel = new NotificationPanel(label);
        feedbackPanel.setOutputMarkupId(true);
        queue(feedbackPanel);
    }

    private void createAndAddDebugBar(String label) {
        if (getDebugSettings().isDevelopmentUtilitiesEnabled())
            queue(new DebugBar(label).positionBottom());
        else
            queue(new EmptyPanel(label).setVisible(false));
    }

    DebugSettings getDebugSettings() {
        return getApplication().getDebugSettings();
    }

    private void createAndAddFooterContainer(String id) {
        queue(new HeaderResponseContainer(id, id));
    }

    private Navbar newNavbar(String markupId) {
        Navbar nb = new Navbar(markupId) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(isNavbarVisible());
            }
        };
        nb.fluid();
        nb.setPosition(Navbar.Position.STATIC_TOP);
        nb.setBrandName(Model.of(getBrandName(getProperties().getBrand())));
        nb.setInverted(true);

        addLinksTo(nb);

        return nb;
    }

    /**
     * Override if you do not want to show the navbar or only conditionally.
     *
     * @return whether to show the Navbar or not.
     */
    protected boolean isNavbarVisible() {
        return true;
    }

    @NotNull
    String getBrandName(@Nullable final String brand) {
        if (brand == null || brand.isBlank() || "n.a.".equals(brand))
            return new StringResourceModel("brandname", this, null).getString();
        return brand;
    }

    @NotNull
    protected abstract ApplicationProperties getProperties();

    protected void addLinksTo(@NotNull Navbar nb) {
    }

    protected void queueFieldAndLabel(@NotNull FormComponent<?> field) {
        queueFieldAndLabel(field, null);
    }

    protected void queueFieldAndLabel(@NotNull FormComponent<?> field, @Nullable PropertyValidator<?> pv) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        final Label label = new Label(id + LABEL_TAG, labelModel);
        queue(label);
        field.setLabel(labelModel);
        queue(field);
        if (pv != null) {
            field.add(pv);
        }
        label.setVisible(field.isVisible());
    }

    protected BootstrapAjaxButton newResponsePageButton(@NotNull final String id,
        @NotNull final SerializableSupplier<AbstractPage<?>> responsePage) {
        return new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, AbstractPage.this, null),
            Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(@NotNull final AjaxRequestTarget target) {
                super.onSubmit(target);
                setResponsePage(responsePage.get());
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setEnabled(AbstractPage.this.setResponsePageButtonEnabled());
            }
        };
    }

    /**
     * Controls the enabled status of the response page button. Override if needed.
     *
     * @return true if response page button shall be enabled (default). false otherwise.
     */
    protected boolean setResponsePageButtonEnabled() {
        return true;
    }

    protected void queuePanelHeadingFor(@NotNull String id) {
        queue(new Label(id + LABEL_TAG, new StringResourceModel(id + PANEL_HEADER_RESOURCE_TAG, this, null)));
    }

    protected boolean isSignedIn() {
        return AuthenticatedWebSession
            .get()
            .isSignedIn();
    }

    protected boolean signIn(@Nullable String username, @Nullable String password) {
        return AuthenticatedWebSession
            .get()
            .signIn(username, password);
    }

    protected void signOutAndInvalidate() {
        AuthenticatedWebSession
            .get()
            .invalidate();
    }

}
