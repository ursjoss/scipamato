package ch.difty.scipamato.web.pages.login;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.web.AbstractPage;

@MountPath("logout")
@AuthorizeInstantiation({ "ROLE_USER", "ROLE_ADMIN" })
public abstract class AbstractLogoutPage extends AbstractPage<Void> {

    private static final long serialVersionUID = 1L;

    public AbstractLogoutPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newForm("form"));
    }

    private Form<Void> newForm(String id) {
        return new Form<Void>(id) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                signOutAndInvalidate();
                setResponsePage(getResponsePage());
            }
        };
    }

    /**
     * Provide the response page
     */
    protected abstract AbstractPage<?> getResponsePage();
}
