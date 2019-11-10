package ch.difty.scipamato.common.web.pages.login;

import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.AbstractPage;

@MountPath("logout")
@SuppressWarnings({ "SameParameterValue", "unused" })
public abstract class AbstractLogoutPage<R extends AbstractPage<?>> extends AbstractPage<Void> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties scipamatoProperties;

    public AbstractLogoutPage(@Nullable PageParameters parameters) {
        super(parameters);
    }

    @NotNull
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
        return new StatelessForm<>(id) {

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
    @Nullable
    protected abstract R getResponsePage();

    @Override
    protected boolean isNavbarVisible() {
        return false;
    }
}
