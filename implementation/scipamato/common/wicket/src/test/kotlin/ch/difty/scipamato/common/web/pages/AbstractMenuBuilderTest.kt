package ch.difty.scipamato.common.web.pages

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.common.web.TestHomePage
import ch.difty.scipamato.common.web.pages.login.TestLoginPage
import com.nhaarman.mockitokotlin2.whenever
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink
import org.apache.wicket.Page
import org.apache.wicket.markup.html.link.AbstractLink
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.util.tester.WicketTester
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@Suppress("SpellCheckingInspection")
@SpringBootTest
internal class AbstractMenuBuilderTest {

    private lateinit var menuBuilder: AbstractMenuBuilder

    @Autowired
    private lateinit var application: WebApplication

    @Suppress("unused")
    @MockBean
    private lateinit var dateTimeService: DateTimeService

    @MockBean
    private lateinit var applicationProperties: ApplicationProperties

    @MockBean
    private lateinit var webSessionFacade: ScipamatoWebSessionFacade

    private lateinit var navbar: Navbar

    private var called = false

    @BeforeEach
    fun setUp() {
        // instantiate the application, but we don't need the wicket tester
        WicketTester(application)

        menuBuilder = TestMenuBuilder(applicationProperties, webSessionFacade)
        navbar = Navbar("navbar")
    }

    @Test
    fun canGetApplicationProperties() {
        assertThat(menuBuilder.applicationProperties).isEqualTo(applicationProperties)
    }

    private fun getPageLinksFrom(navbar: Navbar): List<NavbarButton<Void>> {
        val navLeftList = navbar.get("container:collapse:navLeftListEnclosure:navLeftList")
        @Suppress("UNCHECKED_CAST")
        return navLeftList.defaultModelObject as List<NavbarButton<Void>>
    }

    @Test
    fun pageLinks_withNoneAdded_isEmpty() {
        assertThat(getPageLinksFrom(navbar)).isEmpty()
    }

    @Test
    fun assertPageLink() {
        menuBuilder.addPageLink(
            navbar,
            TestHomePage(),
            TestHomePage::class.java,
            "test",
            GlyphIconType.volumedown,
            Navbar.ComponentPosition.LEFT
        )

        val links = getPageLinksFrom(navbar)

        assertThat(links).hasSize(1)
        val theLink = links[0]

        assertThat(theLink.defaultModelObjectAsString).isEqualTo("")
        assertThat(theLink.get("label").defaultModelObject).isEqualTo("foobar")
        assertThat((theLink.get("icon") as Icon).type).isEqualTo(GlyphIconType.volumedown)
    }

    private fun getExternalLinksFrom(navbar: Navbar): List<NavbarExternalLink> {
        val navLeftList = navbar.get("container:collapse:navLeftListEnclosure:navLeftList")
        @Suppress("UNCHECKED_CAST")
        return navLeftList.defaultModelObject as List<NavbarExternalLink>
    }

    @Test
    fun externalLinks_withNoneAdded_isEmpty() {
        assertThat(getExternalLinksFrom(navbar)).isEmpty()
    }

    @Test
    fun assertExternalLinkWithIcon() {
        menuBuilder.addExternalLink(
            navbar,
            "http://test.com",
            "mylabel",
            GlyphIconType.adjust,
            Navbar.ComponentPosition.LEFT
        )

        val links = getExternalLinksFrom(navbar)

        assertThat(links).hasSize(1)
        val theLink = links[0]

        assertThat(theLink.defaultModelObjectAsString).isEqualTo("http://test.com")
        assertThat(theLink.get("label").defaultModelObject).isEqualTo("mylabel")
        assertThat((theLink.get("icon") as Icon).type).isEqualTo(GlyphIconType.adjust)
    }

    @Test
    fun assertExternalLinkWithoutIcon() {
        menuBuilder.addExternalLink(navbar, "http://foo.com", "otherlabel", null, Navbar.ComponentPosition.LEFT)

        val links = getExternalLinksFrom(navbar)

        assertThat(links).hasSize(1)
        val theLink = links[0]

        assertThat(theLink.defaultModelObjectAsString).isEqualTo("http://foo.com")
        assertThat(theLink.get("label").defaultModelObject).isEqualTo("otherlabel")
        val icon = theLink.get("icon") as Icon
        assertThat(icon.type).isNull()
    }

    private class TestMenuBuilder internal constructor(
        applicationProperties: ApplicationProperties,
        webSessionFacade: ScipamatoWebSessionFacade
    ) : AbstractMenuBuilder(applicationProperties, webSessionFacade) {
        override fun addMenuLinksTo(navbar: Navbar, page: Page) {
            // override if needed
        }
    }

    @Test
    fun addingMenu() {
        val consumer = { _: List<AbstractLink> -> called = true }
        menuBuilder.newMenu(navbar, TestLoginPage(PageParameters()), "foo", GlyphIconType.adjust, consumer)
        assertThat(called).isTrue()
    }

    @Test
    fun gettingVersionStuff_withNoVersionInApplicationProperties() {
        assertThat(menuBuilder.versionAnker).isEqualTo("")
        assertThat(menuBuilder.versionLink).isEqualTo("version null")
    }

    @Test
    fun gettingVersionStuff_withSnapshotVersionInApplicationProperties() {
        whenever(applicationProperties.buildVersion).thenReturn("1.2.3-SNAPSHOT")
        assertThat(menuBuilder.versionAnker).isEqualTo("#unreleased")
        assertThat(menuBuilder.versionLink).isEqualTo("version 1.2.3-SNAPSHOT")
        verify(applicationProperties, times(2)).buildVersion
    }

    @Test
    fun gettingVersionStuff_withReleasedVersionInApplicationProperties() {
        whenever(applicationProperties.buildVersion).thenReturn("1.2.3")
        assertThat(menuBuilder.versionAnker).isEqualTo("#v1.2.3")
        assertThat(menuBuilder.versionLink).isEqualTo("version 1.2.3")
        verify(applicationProperties, times(2)).buildVersion
    }

    @Test
    fun addingEntryToMenu_withIcon() {
        val links = ArrayList<AbstractLink>()
        assertThat(links).isEmpty()

        menuBuilder.addEntryToMenu("label.link", TestHomePage(), TestHomePage::class.java, GlyphIconType.adjust, links)

        assertThat(links).hasSize(1)
        val link = links[0]
        assertThat(link.get("label").defaultModelObject).isEqualTo("somelink")
        assertThat((link.get("icon") as Icon).type).isEqualTo(GlyphIconType.adjust)
    }

    @Test
    fun addingEntryToMenu_withoutIcon() {
        val links = ArrayList<AbstractLink>()
        assertThat(links).isEmpty()

        menuBuilder.addEntryToMenu("label.link", TestHomePage(), TestHomePage::class.java, null, links)

        assertThat(links).hasSize(1)
        val link = links[0]
        assertThat(link.get("label").defaultModelObject).isEqualTo("somelink")
        assertThat((link.get("icon") as Icon).type).isNull()
    }

    @Test
    fun hasOneOfRoles() {
        whenever(webSessionFacade.hasAtLeastOneRoleOutOf("foo", "bar")).thenReturn(true)
        assertThat(menuBuilder.hasOneOfRoles("foo", "bar")).isTrue()
        verify(webSessionFacade).hasAtLeastOneRoleOutOf("foo", "bar")
    }
}
