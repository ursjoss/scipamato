package ch.difty.scipamato.common.web.pages.login;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;

import ch.difty.scipamato.common.web.AbstractPage;

@WicketSignInPage
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
