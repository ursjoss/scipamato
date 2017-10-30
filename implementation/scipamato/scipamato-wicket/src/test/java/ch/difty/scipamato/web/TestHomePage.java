package ch.difty.scipamato.web;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

@WicketHomePage
public class TestHomePage extends AbstractPage<Void> {

    private static final long serialVersionUID = 1L;

    public TestHomePage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected Navbar newNavbar(String markupId) {
        return new Navbar(markupId);
    }

}
