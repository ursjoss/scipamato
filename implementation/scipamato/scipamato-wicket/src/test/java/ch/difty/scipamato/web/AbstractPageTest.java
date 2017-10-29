package ch.difty.scipamato.web;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.DateTimeService;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public class AbstractPageTest extends WicketTest {

    @MockBean
    private DateTimeService dateTimeService;

    @Test
    public void test() {
        AbstractPage<TestRecord> page = new AbstractPage<TestRecord>(Model.of(new TestRecord(1, "foo"))) {
            private static final long serialVersionUID = 1L;

            @Override
            protected Navbar newNavbar(String markupId) {
                Navbar nb = new Navbar(markupId);
                nb.setPosition(Navbar.Position.TOP);
                nb.setBrandName(new ResourceModel("brandname", "LUDOK"));
                nb.setInverted(true);
                return nb;
            }
        };

        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractPage.class);
    }
}
