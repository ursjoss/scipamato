package ch.difty.scipamato.common.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

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

}
