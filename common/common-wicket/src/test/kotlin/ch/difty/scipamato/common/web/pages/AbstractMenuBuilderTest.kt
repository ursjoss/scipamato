package ch.difty.scipamato.common.web.pages

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.config.ApplicationProperties
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.common.web.TestHomePage
import ch.difty.scipamato.common.web.pages.login.TestLoginPage
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.image.Icon
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarExternalLink
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5IconType
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldHaveSize
import org.apache.wicket.Page
import org.apache.wicket.markup.html.link.AbstractLink
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Suppress("SpellCheckingInspection")
@SpringBootTest
internal class AbstractMenuBuilderTest {

    private lateinit var menuBuilder: AbstractMenuBuilder

    @Autowired
    private lateinit var application: WebApplication

    @Suppress("unused")
    @MockkBean
    private lateinit var dateTimeService: DateTimeService

    @MockkBean(relaxed = true)
    private lateinit var applicationProperties: ApplicationProperties

    @MockkBean
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
        menuBuilder.applicationProperties shouldBeEqualTo applicationProperties
    }

    private fun getPageLinksFrom(navbar: Navbar): List<NavbarButton<Void>> {
        val navLeftList = navbar.get("container:collapse:navLeftListEnclosure:navLeftList")
        @Suppress("UNCHECKED_CAST")
        return navLeftList.defaultModelObject as List<NavbarButton<Void>>
    }

    @Test
    fun pageLinks_withNoneAdded_isEmpty() {
        getPageLinksFrom(navbar).shouldBeEmpty()
    }

    @Test
    fun assertPageLink() {
        menuBuilder.addPageLink(
            navbar,
            TestHomePage(),
            TestHomePage::class.java,
            "test",
            FontAwesome5IconType.volume_down_s,
            Navbar.ComponentPosition.LEFT
        )

        val links = getPageLinksFrom(navbar)

        links shouldHaveSize 1
        val theLink = links[0]

        theLink.defaultModelObjectAsString shouldBeEqualTo ""
        theLink.get("label").defaultModelObject shouldBeEqualTo "foobar"
        (theLink.get("icon") as Icon).type shouldBeEqualTo FontAwesome5IconType.volume_down_s
    }

    private fun getExternalLinksFrom(navbar: Navbar): List<NavbarExternalLink> {
        val navLeftList = navbar.get("container:collapse:navLeftListEnclosure:navLeftList")
        @Suppress("UNCHECKED_CAST")
        return navLeftList.defaultModelObject as List<NavbarExternalLink>
    }

    @Test
    fun externalLinks_withNoneAdded_isEmpty() {
        getExternalLinksFrom(navbar).shouldBeEmpty()
    }

    @Test
    fun assertExternalLinkWithIcon() {
        menuBuilder.addExternalLink(
            navbar,
            "https://test.com",
            "mylabel",
            FontAwesome5IconType.adjust_s,
            Navbar.ComponentPosition.LEFT
        )

        val links = getExternalLinksFrom(navbar)

        links shouldHaveSize 1
        val theLink = links[0]

        theLink.defaultModelObjectAsString shouldBeEqualTo "https://test.com"
        theLink.get("label").defaultModelObject shouldBeEqualTo "mylabel"
        (theLink.get("icon") as Icon).type shouldBeEqualTo FontAwesome5IconType.adjust_s
    }

    @Test
    fun assertExternalLinkWithoutIcon() {
        menuBuilder.addExternalLink(navbar, "https://foo.com", "otherlabel", null, Navbar.ComponentPosition.LEFT)

        val links = getExternalLinksFrom(navbar)

        links shouldHaveSize 1
        val theLink = links[0]

        theLink.defaultModelObjectAsString shouldBeEqualTo "https://foo.com"
        theLink.get("label").defaultModelObject shouldBeEqualTo "otherlabel"
        val icon = theLink.get("icon") as Icon
        icon.type.shouldBeNull()
    }

    private class TestMenuBuilder constructor(
        applicationProperties: ApplicationProperties,
        webSessionFacade: ScipamatoWebSessionFacade,
    ) : AbstractMenuBuilder(applicationProperties, webSessionFacade) {
        override fun addMenuLinksTo(navbar: Navbar, page: Page) {
            // override if needed
        }
    }

    @Test
    fun addingMenu() {
        val action = { _: List<AbstractLink> -> called = true }
        menuBuilder.newMenu(navbar, TestLoginPage(PageParameters()), "foo", FontAwesome5IconType.adjust_s, action)
        called.shouldBeTrue()
    }

    @Test
    fun gettingVersionStuff_withNoVersionInApplicationProperties() {
        menuBuilder.versionAnker shouldBeEqualTo ""
        menuBuilder.versionLink shouldBeEqualTo "version "
    }

    @Test
    fun gettingVersionStuff_withNullVersionInApplicationProperties() {
        every { applicationProperties.buildVersion } returns null
        menuBuilder.versionAnker shouldBeEqualTo ""
        menuBuilder.versionLink shouldBeEqualTo "version null"
    }

    @Test
    fun gettingVersionStuff_withBlankVersionInApplicationProperties() {
        every { applicationProperties.buildVersion } returns ""
        menuBuilder.versionAnker shouldBeEqualTo ""
        menuBuilder.versionLink shouldBeEqualTo "version "
    }

    @Test
    fun gettingVersionStuff_withSnapshotVersionInApplicationProperties() {
        every { applicationProperties.buildVersion } returns "1.2.3-SNAPSHOT"
        menuBuilder.versionAnker shouldBeEqualTo "#unreleased"
        menuBuilder.versionLink shouldBeEqualTo "version 1.2.3-SNAPSHOT"
        verify(exactly = 2) { applicationProperties.buildVersion }
    }

    @Test
    fun gettingVersionStuff_withReleasedVersionInApplicationProperties() {
        every { applicationProperties.buildVersion } returns "1.2.3"
        menuBuilder.versionAnker shouldBeEqualTo "#v1.2.3"
        menuBuilder.versionLink shouldBeEqualTo "version 1.2.3"
        verify(exactly = 2) { applicationProperties.buildVersion }
    }

    @Test
    fun addingEntryToMenu_withIcon() {
        val links = mutableListOf<AbstractLink>()
        links.shouldBeEmpty()

        menuBuilder.addEntryToMenu("label.link", TestHomePage(), TestHomePage::class.java, FontAwesome5IconType.adjust_s, links)

        links shouldHaveSize 1
        val link = links[0]
        link.get("label").defaultModelObject shouldBeEqualTo "somelink"
        (link.get("icon") as Icon).type shouldBeEqualTo FontAwesome5IconType.adjust_s
    }

    @Test
    fun addingEntryToMenu_withoutIcon() {
        val links = ArrayList<AbstractLink>()
        links.shouldBeEmpty()

        menuBuilder.addEntryToMenu("label.link", TestHomePage(), TestHomePage::class.java, null, links)

        links shouldHaveSize 1
        val link = links[0]
        link.get("label").defaultModelObject shouldBeEqualTo "somelink"
        (link.get("icon") as Icon).type.shouldBeNull()
    }

    @Test
    fun hasOneOfRoles() {
        every { webSessionFacade.hasAtLeastOneRoleOutOf("foo", "bar") } returns true
        menuBuilder.hasOneOfRoles("foo", "bar").shouldBeTrue()
        verify { webSessionFacade.hasAtLeastOneRoleOutOf("foo", "bar") }
    }
}
