package ch.difty.scipamato.common.web.pages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import ch.difty.scipamato.common.DateTimeService;
import ch.difty.scipamato.common.TestUtils;
import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.TestHomePage;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AbstractMenuBuilderTest {

    private AbstractMenuBuilder menuBuilder;

    @Autowired
    private WebApplication application;

    @MockBean
    private DateTimeService dateTimeService;

    @Mock
    private ApplicationProperties applicationProperties;

    private Navbar navbar;

    @Before
    public void setUp() {
        // instantiate the application, but we don't need the wicket tester
        new WicketTester(application);

        menuBuilder = new TestMenuBuilder(applicationProperties);
        navbar = new Navbar("navbar");
    }

    @Test
    public void degenerateConstruction() {
        TestUtils.assertDegenerateSupplierParameter(() -> new TestMenuBuilder(null), "applicationProperties");
    }

    @Test
    public void canGetApplicationProperties() {
        assertThat(menuBuilder.getApplicationProperties()).isEqualTo(applicationProperties);
    }

    @SuppressWarnings("unchecked")
    private List<NavbarButton<Void>> getPageLinksFrom(final Navbar navbar) {
        Component navLeftList = navbar.get("container:collapse:navLeftListEnclosure:navLeftList");
        return (List<NavbarButton<Void>>) navLeftList.getDefaultModelObject();
    }

    @Test
    public void pageLinks_withNoneAdded_isEmpty() {
        assertThat(getPageLinksFrom(navbar)).isEmpty();
    }

    @Test
    public void assertPageLink() {
        menuBuilder.addPageLink(navbar, new TestHomePage(), TestHomePage.class, "test", GlyphIconType.volumedown,
            Navbar.ComponentPosition.LEFT);

        List<NavbarButton<Void>> links = getPageLinksFrom(navbar);

        assertThat(links).hasSize(1);
        NavbarButton<Void> theLink = links.get(0);

        assertThat(theLink.getDefaultModelObjectAsString()).isEqualTo("");
        assertThat(theLink.get("label").getDefaultModelObject()).isEqualTo("foobar");
        assertThat(((Icon) theLink.get("icon")).getType()).isEqualTo(GlyphIconType.volumedown);
    }

    @SuppressWarnings("unchecked")
    private List<NavbarExternalLink> getExternalLinksFrom(Navbar navbar) {
        Component navLeftList = navbar.get("container:collapse:navLeftListEnclosure:navLeftList");
        return (List<NavbarExternalLink>) navLeftList.getDefaultModelObject();
    }

    @Test
    public void externalLinks_withNoneAdded_isEmpty() {
        assertThat(getExternalLinksFrom(navbar)).isEmpty();
    }

    @Test
    public void assertExternalLinkWithIcon() {
        menuBuilder.addExternalLink(navbar, "http://test.com", "mylabel", GlyphIconType.adjust,
            Navbar.ComponentPosition.LEFT);

        List<NavbarExternalLink> links = getExternalLinksFrom(navbar);

        assertThat(links).hasSize(1);
        NavbarExternalLink theLink = links.get(0);

        assertThat(theLink.getDefaultModelObjectAsString()).isEqualTo("http://test.com");
        assertThat(theLink.get("label").getDefaultModelObject()).isEqualTo("mylabel");
        assertThat(((Icon) theLink.get("icon")).getType()).isEqualTo(GlyphIconType.adjust);
    }

    @Test
    public void assertExternalLinkWithoutIcon() {
        menuBuilder.addExternalLink(navbar, "http://foo.com", "otherlabel", null, Navbar.ComponentPosition.LEFT);

        List<NavbarExternalLink> links = getExternalLinksFrom(navbar);

        assertThat(links).hasSize(1);
        NavbarExternalLink theLink = links.get(0);

        assertThat(theLink.getDefaultModelObjectAsString()).isEqualTo("http://foo.com");
        assertThat(theLink.get("label").getDefaultModelObject()).isEqualTo("otherlabel");
        Icon icon = (Icon) theLink.get("icon");
        assertThat(icon).isNotNull();
        assertThat(icon.getType()).isNull();
    }

    private static class TestMenuBuilder extends AbstractMenuBuilder {
        TestMenuBuilder(ApplicationProperties applicationProperties) {
            super(applicationProperties);
        }

        @Override
        public void addMenuLinksTo(Navbar navbar, Page page) {
        }
    }

    @Test
    public void gettingVersionStuff_withNoVersionInApplicationProperties() {
        assertThat(menuBuilder.getVersionAnker()).isEqualTo("");
        assertThat(menuBuilder.getVersionLink()).isEqualTo("version null");
    }

    @Test
    public void gettingVersionStuff_withSnapshotVersionInApplicationProperties() {
        when(applicationProperties.getBuildVersion()).thenReturn("1.2.3-SNAPSHOT");
        assertThat(menuBuilder.getVersionAnker()).isEqualTo("#unreleased");
        assertThat(menuBuilder.getVersionLink()).isEqualTo("version 1.2.3-SNAPSHOT");
        verify(applicationProperties, times(2)).getBuildVersion();
    }

    @Test
    public void gettingVersionStuff_withReleasedVersionInApplicationProperties() {
        when(applicationProperties.getBuildVersion()).thenReturn("1.2.3");
        assertThat(menuBuilder.getVersionAnker()).isEqualTo("#v1.2.3");
        assertThat(menuBuilder.getVersionLink()).isEqualTo("version 1.2.3");
        verify(applicationProperties, times(2)).getBuildVersion();
    }
}
