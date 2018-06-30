package ch.difty.scipamato.common.web.pages.login;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.AbstractPage;

@MountPath("logout")
public abstract class AbstractLogoutPage extends AbstractPage<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties scipamatoProperties;

    public AbstractLogoutPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected ApplicationProperties getProperties() {
        return scipamatoProperties;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        queue(newForm("form"));
    }

    private StatelessForm<Void> newForm(String id) {
        return new StatelessForm<Void>(id) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                signOutAndInvalidate();
                AbstractPage<?> responsePage = getResponsePage();
                if (responsePage != null)
                    setResponsePage(responsePage);
            }
        };
    }

    /**
     * @return the response page
     */
    protected abstract AbstractPage<?> getResponsePage();

    @Override
    protected boolean isNavbarVisible() {
        return false;
    }
}
