package ch.difty.scipamato.common.web.pages.login;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.web.AbstractPage;

public class TestLogoutPage extends AbstractLogoutPage<AbstractPage<?>> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("WeakerAccess")
    public TestLogoutPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected AbstractPage<?> getResponsePage() {
        return null;
    }

}
