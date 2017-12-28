package ch.difty.scipamato.common.web.test;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.web.AbstractPage;
import ch.difty.scipamato.common.web.pages.login.AbstractLoginPage;

public class TestLoginPage extends AbstractLoginPage {
    private static final long serialVersionUID = 1L;

    public TestLoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected AbstractPage<?> getResponsePage() {
        return new TestLoginPage(getPageParameters());
    }

}
