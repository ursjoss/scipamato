package ch.difty.scipamato.common.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.head.filter.HeaderResponseContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.settings.DebugSettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.config.ApplicationProperties;

public class AbstractPageTest extends WicketBaseTest {

    private AbstractPage<TestRecord> page;

    @MockBean
    private DateTimeService dateTimeServiceMock;

    @Override
    protected void setUpHook() {
        page = new AbstractPage<>(Model.of(new TestRecord(1, "foo"))) {
            private static final long serialVersionUID = 1L;

            @Override
            protected ApplicationProperties getProperties() {
                return new TestApplicationProperties();
            }
        };
    }

    @AfterEach
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
        assertThat(page.getDateTimeService())
            .isNotNull()
            .isInstanceOf(DateTimeService.class);
    }

    @Test
    public void hasNavbarVisibleByDefault() {
        assertThat(page.isNavbarVisible()).isTrue();
    }

    @Test
    public void gettingNavbarAndFeedbackPanelBeforeInitialize_returnsNull() {
        assertThat(page.getNavBar()).isNull();
        assertThat(page.getFeedbackPanel()).isNull();
    }

    @Test
    public void test_withPageParametersConstructor() {
        AbstractPage<TestRecord> page = new AbstractPage<>(new PageParameters()) {
            private static final long serialVersionUID = 1L;

            @Override
            protected ApplicationProperties getProperties() {
                return new TestApplicationProperties();
            }

            @Override
            protected boolean isNavbarVisible() {
                return true;
            }
        };

        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractPage.class);
    }

    @Test
    public void assertPage() {
        getTester().startPage(new TestAbstractPage(Model.of(new TestRecord(1, "foo"))));
        getTester().assertRenderedPage(TestAbstractPage.class);
        assertComponents();
    }

    private void assertComponents() {
        getTester().assertComponent("_header_", HtmlHeaderContainer.class);
        getTester().assertLabel("_header_:pageTitle", "SciPaMaTo");

        getTester().assertComponent("navbar", Navbar.class);
        getTester().assertComponent("navbar:container:collapse:extraItems", RepeatingView.class);

        getTester().assertLabel("navbar:container:collapseButton:toggleNavigationLabel", "Toggle Navigation");
        getTester().assertLabel("navbar:container:brandName:brandLabel", "SciPaMaTo");

        getTester().assertComponent("feedback", NotificationPanel.class);
        getTester().assertComponent("footer-container", HeaderResponseContainer.class);

        getTester().assertInvisible("debug");

        getTester().assertComponent("form", Form.class);
        getTester().assertLabel("form:nameLabel", "Name");
        getTester().assertComponent("form:name", TextField.class);
        getTester().assertModelValue("form:name", "foo");
    }

    @Test
    public void gettingBrandName_withNullBrand_retrievesBrandFromProperties() {
        assertThat(page.getBrandName(null)).isEqualTo("SciPaMaTo");
    }

    @Test
    public void gettingBrandName_withEmptyBrand_retrievesBrandFromProperties() {
        assertThat(page.getBrandName("")).isEqualTo("SciPaMaTo");
    }

    @Test
    public void gettingBrandName_withNaBrand_retrievesBrandFromProperties() {
        assertThat(page.getBrandName("n.a.")).isEqualTo("SciPaMaTo");
    }

    @Test
    public void gettingBrandName_withExplicitBrand_usesThat() {
        assertThat(page.getBrandName("foo")).isEqualTo("foo");
    }

    @Test
    public void queueingFieldAndLabel_withPropertyValidator_addsItToField() {
        final TextField<String> field = mock(TextField.class);
        when(field.getId()).thenReturn("testField");
        final PropertyValidator<String> pv = mock(PropertyValidator.class);

        page.queueFieldAndLabel(field, pv);

        verify(field, times(8)).getId();
        verify(field).setLabel(isA(IModel.class));
        verify(field).add(pv);
        verify(field).isVisible();
        verifyNoMoreInteractions(field);
    }

    @Test
    public void queueingFieldAndLabel_withNoPropertyValidator_dowNotAddToField() {
        final TextField<String> field = mock(TextField.class);
        when(field.getId()).thenReturn("testField");

        page.queueFieldAndLabel(field, null);

        verify(field, times(8)).getId();
        verify(field).setLabel(isA(IModel.class));
        verify(field).isVisible();
        verifyNoMoreInteractions(field);
    }

    @Test
    public void submitting() {
        getTester().startPage(new TestAbstractPage(Model.of(new TestRecord(1, "foo"))));
        getTester().assertRenderedPage(TestAbstractPage.class);

        getTester().assertModelValue("form:name", "foo");

        getTester().executeAjaxEvent("form:respPageButton", "click");

        getTester().assertRenderedPage(TestAbstractPage.class);
        getTester().assertModelValue("form:name", "bar");
    }

    @Test
    public void withDebugEnabled() {
        final DebugSettings debugSettings = mock(DebugSettings.class);
        when(debugSettings.isDevelopmentUtilitiesEnabled()).thenReturn(true);

        page = new AbstractPage<>(Model.of(new TestRecord(1, "foo"))) {
            private static final long serialVersionUID = 1L;

            @Override
            protected ApplicationProperties getProperties() {
                return new TestApplicationProperties();
            }

            @Override
            DebugSettings getDebugSettings() {
                return debugSettings;
            }
        };

        getTester().startPage(page);
        getTester().assertRenderedPage(AbstractPage.class);

        verify(debugSettings).isDevelopmentUtilitiesEnabled();
    }
}
