package ch.difty.scipamato.common.web.pages.login;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.AbstractPage;

/**
 * @param <R>
 *     type of the response page
 */
@Slf4j
@SuppressWarnings("ALL")
public abstract class AbstractLoginPage<R extends AbstractPage<?>> extends AbstractPage<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties scipamatoProperties;

    private String username;
    private String password;

    public AbstractLoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected ApplicationProperties getProperties() {
        return scipamatoProperties;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (isSignedIn()) {
            continueToOriginalDestination();
        }

        queue(newLoginForm("form"));
    }

    private StatelessForm<Void> newLoginForm(String id) {
        StatelessForm<Void> form = new StatelessForm<>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                if (signIn(username, password)) {
                    log.info("User '{}' logged in successfully", username);
                    setResponsePage(getResponsePage());
                } else {
                    log.warn("Unsuccessful login attempt by user '{}'.", username);
                    error(getString("msg.login.failure"));
                }
            }

        };
        form.setDefaultModel(new CompoundPropertyModel<>(this));

        queue(newHeader("header"));
        queueFieldAndLabel(new RequiredTextField<String>("username"));
        queueFieldAndLabel(new PasswordTextField("password"));
        queue(newButton("signin"));

        return form;
    }

    /**
     * Provide the response page
     *
     * @return the response page of type <R>
     */
    protected abstract R getResponsePage();

    private Label newHeader(String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null));
    }

    private BootstrapButton newButton(String id) {
        final BootstrapButton button = new BootstrapButton(id, new StringResourceModel(id + ".value", this, null), Buttons.Type.Default);
        button.add(new LoadingBehavior(new StringResourceModel(id + ".loading", this, null)));
        return button;
    }

    @Override
    protected boolean isNavbarVisible() {
        return false;
    }

}
