package ch.difty.scipamato.common.web;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import ch.difty.scipamato.common.config.ApplicationProperties;

@WicketHomePage
public class TestHomePage extends AbstractPage<Void> {

    private static final long serialVersionUID = 1L;

    public TestHomePage() {
        super(new PageParameters());
    }

    public TestHomePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected ApplicationProperties getProperties() {
        return new TestApplicationProperties();
    }

    @Override
    protected boolean isNavbarVisible() {
        return true;
    }

}
