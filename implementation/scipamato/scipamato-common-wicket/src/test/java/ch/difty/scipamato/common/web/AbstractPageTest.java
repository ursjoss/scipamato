package ch.difty.scipamato.common.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;
import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;

public class AbstractPageTest extends WicketBaseTest {

    private AbstractPage<TestRecord> page;

    @MockBean
    private DateTimeService dateTimeServiceMock;

    @Override
    protected void setUpHook() {
        page = new AbstractPage<TestRecord>(Model.of(new TestRecord(1, "foo"))) {
            private static final long serialVersionUID = 1L;

            @Override
            protected ApplicationProperties getProperties() {
                return new TestApplicationProperties();
            }
        };
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(dateTimeServiceMock);
    }

    @Test
    public void test_withModelConstructor() {
        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractPage.class);
    }

    @Test
    public void canGetDateTimeService() {
        assertThat(page.getDateTimeService()).isNotNull()
            .isInstanceOf(DateTimeService.class);
    }

    @Test
    public void gettingNavbarAndFeedbackPanelBeforeInitialize_returnsNull() {
        assertThat(page.getNavBar()).isNull();
        assertThat(page.getFeedbackPanel()).isNull();
    }

    @Test
    public void test_withPageParametersConstructor() {
        AbstractPage<TestRecord> page = new AbstractPage<TestRecord>(new PageParameters()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected ApplicationProperties getProperties() {
                return new TestApplicationProperties();
            }
        };

        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractPage.class);
    }

    @Test
    public void assertPage() {
        getTester().startPage(new TestAbstractPage(Model.of(new TestRecord(1, "foo"))));
        getTester().assertRenderedPage(TestAbstractPage.class);
        getTester().debugComponentTrees();
        assertComponents();
    }

    private void assertComponents() {
        getTester().assertComponent("_header_", HtmlHeaderContainer.class);

        getTester().assertComponent("navbar", Navbar.class);
        getTester().assertComponent("navbar:container:collapse:extraItems", RepeatingView.class);

        getTester().assertLabel("navbar:container:collapseButton:toggleNavigationLabel", "Toggle Navigation");
        getTester().assertLabel("navbar:container:brandName:brandLabel", "SciPaMaTo");

        getTester().assertComponent("feedback", NotificationPanel.class);

        getTester().assertInvisible("debug");

        getTester().assertComponent("form", Form.class);
        getTester().assertLabel("form:fooLabel", "Foo");
        getTester().assertComponent("form:foo", TextField.class);
    }
}
