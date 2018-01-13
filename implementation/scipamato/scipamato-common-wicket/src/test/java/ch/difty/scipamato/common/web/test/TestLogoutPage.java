package ch.difty.scipamato.common.web.test;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.pages.login.AbstractLogoutPage;

public class TestLogoutPage extends AbstractLogoutPage {
    private static final long serialVersionUID = 1L;

    public TestLogoutPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected AbstractPage<?> getResponsePage() {
        return null;
    }

}
