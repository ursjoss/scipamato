package ch.difty.scipamato.publ.web.newstudies

import ch.difty.scipamato.publ.config.ApplicationPublicProperties
import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import ch.difty.scipamato.publ.entity.NewStudy
import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider
import ch.difty.scipamato.publ.web.PublicPageParameters
import ch.difty.scipamato.publ.web.clickLinkSameSite
import ch.difty.scipamato.publ.web.common.BasePageTest
import ch.difty.scipamato.publ.web.newFormTesterSameSite
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage
import ch.difty.scipamato.publ.web.resources.IcoMoonIconType
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconType
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.ExternalLink
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.junit.jupiter.api.Test
import java.time.LocalDate

@Suppress("unused")
class NewStudyListPageTest : BasePageTest<NewStudyListPage>() {

    @MockkBean
    private lateinit var serviceMock: NewStudyTopicService

    @MockkBean(name = "simplonFontResourceProvider", relaxed = true)
    private lateinit var simplonFontResourceProvider: CommercialFontResourceProvider

    @MockkBean(name = "icoMoonFontResourceProvider", relaxed = true)
    private lateinit var icoMoonFontResourceProvider: CommercialFontResourceProvider

    private val topics: MutableList<NewStudyTopic> = ArrayList()
    private val links: MutableList<NewStudyPageLink> = ArrayList()
    private val archived: MutableList<Newsletter> = ArrayList()

    override fun setUpHook() {
        super.setUpHook()
        var topicIndex = 0
        var studyIndex = 0
        val newStudies1: MutableList<NewStudy> = ArrayList()
        newStudies1.add(NewStudy(studyIndex++, 8924, 2017, "Foo et al.", "hl1", "descr1"))
        newStudies1.add(NewStudy(studyIndex, 8993, 2017, "Bar et al.", "hl2", "descr2"))
        topics.add(NewStudyTopic(topicIndex++, "Topic1", newStudies1))
        val newStudies2: MutableList<NewStudy> = ArrayList()
        studyIndex = 0
        newStudies2.add(NewStudy(studyIndex, 8973, 2017, "Baz et al.", "hl3", "descr3"))
        topics.add(NewStudyTopic(topicIndex, "Topic2", newStudies2))
        every { serviceMock.findMostRecentNewStudyTopics(any()) } returns topics

        links.add(NewStudyPageLink("en", 1, "linkTitle1", "linkUrl1"))
        links.add(NewStudyPageLink("en", 2, "linkTitle2", "linkUrl2"))
        every { serviceMock.findNewStudyPageLinks(any()) } returns links
        archived.add(Newsletter(10, "2018/02", LocalDate.of(2018, 2, 10)))
        archived.add(Newsletter(9, "2017/12", LocalDate.of(2017, 12, 12)))
        every { serviceMock.findArchivedNewsletters(any(), any()) } returns archived

        every { serviceMock.findNewStudyTopicsForNewsletterIssue(any(), any()) } returns emptyList()
    }

    override fun makePage(): NewStudyListPage = NewStudyListPage(PageParameters())

    override val pageClass: Class<NewStudyListPage>
        get() = NewStudyListPage::class.java

    override fun assertSpecificComponents() {
        super.assertSpecificComponents()
        assertStudySection()
        assertLinkSection()
        assertArchiveSection()
    }

    private fun assertStudySection() {
        tester.assertLabel("h1Title", "New Studies")
        tester.assertComponent("introParagraph", Label::class.java)
        tester.assertComponent("dbLink", ExternalLink::class.java)
        tester.assertComponent("topics", ListView::class.java)
        var topic = "topics:0:"
        tester.assertLabel(topic + "topicTitle", "Topic1")
        assertNewStudy(topic, 0, "hl1", "descr1", "(Foo et al.; 2017)")
        assertNewStudy(topic, 1, "hl2", "descr2", "(Bar et al.; 2017)")
        topic = "topics:1:"
        tester.assertLabel(topic + "topicTitle", "Topic2")
        assertNewStudy(topic, 0, "hl3", "descr3", "(Baz et al.; 2017)")
    }

    private fun assertLinkSection() {
        tester.assertComponent("links", ListView::class.java)
        var index = 0
        assertLink(index++, "linkTitle1", "linkUrl1")
        assertLink(index, "linkTitle2", "linkUrl2")
    }

    private fun assertLink(index: Int, title: String, url: String) {
        val path = "links:$index:link"
        tester.assertComponent(path, BootstrapExternalLink::class.java)
        tester.assertModelValue(path, url)
        tester.assertLabel("$path:label", title)
    }

    private fun assertArchiveSection() {
        tester.assertLabel("h2ArchiveTitle", "Archive")
        tester.assertComponent("archive", ListView::class.java)
        tester.assertLabel("archive:0:monthName:label", "Feb 2018")
        tester.assertLabel("archive:1:monthName:label", "Dec 2017")
    }

    private fun assertNewStudy(
        base: String, studyIndex: Int, headline: String, description: String, reference: String
    ) {
        val path = base + "topicStudies:" + studyIndex + ":"
        tester.assertLabel(path + "headline", headline)
        tester.assertLabel(path + "description", description)
        tester.assertLabel(path + "reference:referenceLabel", reference)
        tester.assertComponent(path + "reference", Link::class.java)
    }

    override fun doVerify() {
        super.doVerify()
        // loading the page initially
        verify(exactly = 2) { serviceMock.findMostRecentNewStudyTopics("en_us") }
        verify(exactly = 2) { serviceMock.findNewStudyPageLinks("en_us") }
        verify(exactly = 2) { serviceMock.findArchivedNewsletters(14, "en_us") }
        confirmVerified(serviceMock)
    }

    @Test
    fun canAccessPublicPaperDetailPageForSpecificPaper_andReturnToNewStudyListPageFromThere() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.clickLinkSameSite("topics:1:topicStudies:0:reference")
        tester.assertRenderedPage(PublicPaperDetailPage::class.java)
        tester.newFormTesterSameSite("form").submit("back")
        tester.assertRenderedPage(NewStudyListPage::class.java)
    }

    @Test
    fun renderingCommercialFonts() {
        val page = makePage()
        val hr = mockk<IHeaderResponse>(relaxUnitFun = true)
        page.renderAdditionalCommercialFonts(hr)

        // null, as commercial font is configured to not be used
        verify(exactly = 2) { hr.render(any()) }
    }

    @Test
    fun withIssueMissing() {
        val pp = PageParameters()
        pageWithIssue(pp)
    }

    @Test
    fun withIssueNull() {
        val pp = PageParameters()
        pp[PublicPageParameters.ISSUE.parameterName] = null
        pageWithIssue(pp)
    }

    @Test
    fun withIssueBlank() {
        val pp = PageParameters()
        pp[PublicPageParameters.ISSUE.parameterName] = ""
        pageWithIssue(pp)
    }

    private fun pageWithIssue(pp: PageParameters) {
        val page = NewStudyListPage(pp)
        tester.startPage(page)
        tester.assertRenderedPage(pageClass)
        verify(exactly = 2) { serviceMock.findMostRecentNewStudyTopics("en_us") }
        verify(exactly = 0) { serviceMock.findNewStudyTopicsForNewsletterIssue(any(), any()) }
    }

    @Test
    fun withIssuePresent() {
        val pp = PageParameters()
        pp[PublicPageParameters.ISSUE.parameterName] = "1806"
        val page = NewStudyListPage(pp)
        tester.startPage(page)
        tester.assertRenderedPage(pageClass)
        verify(exactly = 2) { serviceMock.findNewStudyTopicsForNewsletterIssue("1806", "en_us") }
        verify(exactly = 0) { serviceMock.findMostRecentNewStudyTopics(any()) }
    }

    @Test
    fun icon_withFreeFont() {
        val page = makePage()
        val icon = page.chooseIcon(FontAwesome7IconType.right_long_s, IcoMoonIconType.arrow_right)
        icon shouldBeEqualTo FontAwesome7IconType.right_long_s
    }

    @Test
    fun icon_withCommercialFont() {
        val applicationProperties = mockk<ScipamatoPublicProperties> {
            every { isCommercialFontPresent } returns true
            every { isNavbarVisibleByDefault } returns false
        }
        val page: NewStudyListPage = object : NewStudyListPage(PageParameters()) {
            private val serialVersionUID: Long = 1L
            override val properties: ApplicationPublicProperties
                get() = applicationProperties
        }
        val icon = page.chooseIcon(FontAwesome7IconType.right_long_s, IcoMoonIconType.arrow_right)
        icon shouldBeEqualTo IcoMoonIconType.arrow_right
    }

    @Test
    fun clickingLinkToArchivedNewsletter() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        tester.clickLinkSameSite("archive:1:monthName")
        tester.assertRenderedPage(NewStudyListPage::class.java)
        tester.debugComponentTrees()

        // loading the page initially
        verify(exactly = 2) { serviceMock.findMostRecentNewStudyTopics("en_us") }
        // redirecting after having clicked the link
        verify { serviceMock.findNewStudyTopicsForNewsletterIssue("2017/12", "en_us") }
        // other service calls (twice at initial page load, one each after redirect
        verify(exactly = 2 + 1) { serviceMock.findNewStudyPageLinks("en_us") }
        verify(exactly = 2 + 1) { serviceMock.findArchivedNewsletters(14, "en_us") }
        confirmVerified(serviceMock)
    }
}
