package ch.difty.scipamato.web.test;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.scipamato.web.AbstractPage;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

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
    protected Navbar newNavbar(String markupId) {
        return new Navbar(markupId);
    }

}
