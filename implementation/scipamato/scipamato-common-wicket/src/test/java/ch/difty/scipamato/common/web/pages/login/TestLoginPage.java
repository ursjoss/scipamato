package ch.difty.scipamato.common.web.pages.login;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@WicketSignInPage
public class TestLoginPage extends AbstractLoginPage<TestLoginPage> {
    private static final long serialVersionUID = 1L;

    public TestLoginPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected TestLoginPage getResponsePage() {
        return new TestLoginPage(getPageParameters());
    }

}
