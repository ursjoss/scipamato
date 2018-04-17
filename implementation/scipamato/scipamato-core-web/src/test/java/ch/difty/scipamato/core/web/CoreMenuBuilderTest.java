package ch.difty.scipamato.core.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.junit.Test;
import org.mockito.Mock;

import ch.difty.scipamato.common.NullArgumentException;
import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.pages.MenuBuilder;
import ch.difty.scipamato.core.web.common.BasePage;

public class CoreMenuBuilderTest extends WicketTest {

    @Mock
    private ApplicationProperties applicationProperties;
    @Mock
    private Navbar                navbar;
    @Mock
    private BasePage<?>           basePage;

    private MenuBuilder menuBuilder;

    @Override
    public void setUpHook() {
        menuBuilder = new CoreMenuBuilder(applicationProperties);
    }

    @Test
    public void degenerateConstruction_withNullApplicationProperties() {
        TestUtils.assertDegenerateSupplierParameter(() -> new CoreMenuBuilder(null), "applicationProperties");
    }

    @Test
    public void degenerateMethodCall_withNullNavbar() {
        try {
            menuBuilder.addMenuLinksTo(null, basePage);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("navbar must not be null.");
        }
    }

    @Test
    public void degenerateMethodCall_withNullPage() {
        try {
            menuBuilder.addMenuLinksTo(navbar, null);
            fail("should have thrown exception");
        } catch (Exception ex) {
            assertThat(ex)
                .isInstanceOf(NullArgumentException.class)
                .hasMessage("page must not be null.");
        }
    }

    // TODO complete the test to verify the actual menu creation. From the original
    // TestAbstractPage

}
