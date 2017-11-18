package ch.difty.scipamato.web.pages.login;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.web.AbstractPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

public abstract class AbstractLoginPage extends AbstractPage<Void> {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public AbstractLoginPage(PageParameters parameters) {
        super(parameters);
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
        StatelessForm<Void> form = new StatelessForm<Void>(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                if (signIn(username, password)) {
                    setResponsePage(getResponsePage());
                } else {
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
     */
    protected abstract AbstractPage<?> getResponsePage();

    private Label newHeader(String id) {
        return new Label(id, new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null));
    }

    private BootstrapButton newButton(String id) {
        return new BootstrapButton(id, new StringResourceModel(id + ".value", this, null), Buttons.Type.Default);
    }

}
