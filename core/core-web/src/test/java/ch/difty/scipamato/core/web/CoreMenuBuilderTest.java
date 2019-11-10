package ch.difty.scipamato.core.web;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.mockito.Mock;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.common.web.pages.MenuBuilder;
import ch.difty.scipamato.core.web.common.BasePage;

class CoreMenuBuilderTest extends WicketTest {

    @Mock
    private ApplicationProperties     applicationProperties;
    @Mock
    private ScipamatoWebSessionFacade webSessionFacade;
    @Mock
    private Navbar                    navbar;
    @Mock
    private BasePage<?>               basePage;

    private MenuBuilder menuBuilder;

    @Override
    public void setUpHook() {
        menuBuilder = new CoreMenuBuilder(applicationProperties, webSessionFacade);
    }

    // TODO complete the test to verify the actual menu creation. From the original
    // TestAbstractPage
}
