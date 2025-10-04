package ch.difty.scipamato.publ.web.newstudies

import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.publ.entity.NewStudy
import ch.difty.scipamato.publ.entity.NewStudyPageLink
import ch.difty.scipamato.publ.entity.NewStudyTopic
import ch.difty.scipamato.publ.entity.Newsletter
import ch.difty.scipamato.publ.persistence.api.NewStudyTopicService
import ch.difty.scipamato.publ.web.CommercialFontResourceProvider
import ch.difty.scipamato.publ.web.PublicPageParameters
import ch.difty.scipamato.publ.web.common.BasePage
import ch.difty.scipamato.publ.web.paper.browse.PublicPaperDetailPage
import ch.difty.scipamato.publ.web.resources.IcoMoonIconType
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapExternalLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapLink
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconType
import org.apache.wicket.markup.ComponentTag
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.link.ExternalLink
import org.apache.wicket.markup.html.link.Link
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean
import org.wicketstuff.annotation.mount.MountPath

/**
 * The page lists 'new studies', i.e. studies that were collected and flagged by the SciPaMaTo-team
 * as eligible for this page. By default, the newest collection of new studies is presented.
 *
 * With the use of page-parameters, an older collection of new studies can be selected instead.
 *
 * The page is typically shown in an iframe of a CMS.
 */
@MountPath("new-studies")
@Suppress("SameParameterValue", "TooManyFunctions")
open class NewStudyListPage(parameters: PageParameters) : BasePage<Unit>(parameters) {

    @SpringBean
    private lateinit var newStudyTopicService: NewStudyTopicService

    @SpringBean(name = "simplonFontResourceProvider")
    private lateinit var simplonFontResourceProvider: CommercialFontResourceProvider

    @SpringBean(name = "icoMoonFontResourceProvider")
    private lateinit var icoMoonFontResourceProvider: CommercialFontResourceProvider

    override fun renderAdditionalCommercialFonts(response: IHeaderResponse) {
        response.render(CssHeaderItem.forReference(simplonFontResourceProvider.cssResourceReference))
        response.render(CssHeaderItem.forReference(icoMoonFontResourceProvider.cssResourceReference))
    }

    override fun renderHead(response: IHeaderResponse) {
        super.renderHead(response)
        response.render(
            CssHeaderItem.forReference(CssResourceReference(NewStudyListPage::class.java, "NewStudyListPage.css"))
        )
    }

    override fun onInitialize() {
        super.onInitialize()
        newIntroSection()
        newNewsletterSection()
        newExternalLinkSection()
        newArchiveSectionWithPreviousNewsletters()
    }

    /**
     * Introductory paragraph
     */
    private fun newIntroSection() {
        queue(newLabel("h1Title"))
        queue(newLabel("introParagraph"))
        queue(newDbSearchLink("dbLink", properties.cmsUrlSearchPage))
    }

    private fun newDbSearchLink(id: String, href: String?) = object : ExternalLink(
        id,
        href,
        StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null).string
    ) {
        private val serialVersionUID: Long = 1L
        override fun onComponentTag(tag: ComponentTag) {
            super.onComponentTag(tag)
            tag.put(TARGET, BLANK)
        }
    }

    /**
     * The actual newsletter/new study list part with topics and nested studies
     */
    private fun newNewsletterSection() {
        queue(newNewStudyCollection("topics"))
    }

    private fun newNewStudyCollection(id: String): ListView<NewStudyTopic> {
        val topics = retrieveStudyCollection()
        paperIdManager.initialize(extractPaperNumbersFrom(topics))
        return object : ListView<NewStudyTopic>(id, topics) {
            private val serialVersionUID: Long = 1L
            override fun populateItem(topic: ListItem<NewStudyTopic>) {
                topic.add(Label("topicTitle", PropertyModel<Any>(topic.model, "title")))
                topic.add(object : ListView<NewStudy>("topicStudies", topic.modelObject.studies) {
                    private val serialVersionUID: Long = 1L
                    override fun populateItem(study: ListItem<NewStudy>) {
                        study.add(Label("headline", PropertyModel<Any>(study.model, "headline")))
                        study.add(Label("description", PropertyModel<Any>(study.model, "description")))
                        study.add(newLinkToStudy("reference", study))
                    }
                })
            }
        }
    }

    private fun retrieveStudyCollection(): List<NewStudyTopic> {
        val issue = pageParameters[PublicPageParameters.ISSUE.parameterName]
        return if (issue.isNull || issue.isEmpty)
            newStudyTopicService.findMostRecentNewStudyTopics(languageCode)
        else newStudyTopicService.findNewStudyTopicsForNewsletterIssue(issue.toString(), languageCode)
    }

    private fun extractPaperNumbersFrom(topics: List<NewStudyTopic>): List<Long> =
        topics.flatMap { it.studies }.map { it.number }

    /**
     * Link pointing to the study detail page with the current [study] (with [id])
     */
    private fun newLinkToStudy(id: String, study: ListItem<NewStudy>): Link<NewStudy> {
        val pp = PageParameters()
        pp[PublicPageParameters.NUMBER.parameterName] = study.modelObject.number
        return object : Link<NewStudy>(id) {
            private val serialVersionUID: Long = 1L
            override fun onClick() {
                paperIdManager.setFocusToItem(study.modelObject.number)
                setResponsePage(PublicPaperDetailPage(pp, pageReference))
            }
        }.apply {
            add(Label("$id$LABEL_TAG", PropertyModel<String>(study.model, "reference")))
        }
    }

    /**
     * Any links configured in database table new_study_page_links will be published in this section.
     */
    private fun newExternalLinkSection() {
        queue(newLinkList("links"))
    }

    private fun newLinkList(id: String): ListView<NewStudyPageLink> {
        val links = newStudyTopicService.findNewStudyPageLinks(languageCode)
        return object : ListView<NewStudyPageLink>(id, links) {
            private val serialVersionUID: Long = 1L
            override fun populateItem(link: ListItem<NewStudyPageLink>) {
                link.add(newExternalLink("link", link))
            }
        }
    }

    private fun newExternalLink(id: String, linkItem: ListItem<NewStudyPageLink>) = object : BootstrapExternalLink(
        id,
        Model.of(linkItem.modelObject.url)
    ) {
        private val serialVersionUID: Long = 1
    }.apply {
        setTarget(BootstrapExternalLink.Target.blank)
        setIconType(chooseIcon(FontAwesome7IconType.right_long_s, IcoMoonIconType.arrow_right))
        setLabel(Model.of(linkItem.modelObject.title))
    }

    fun chooseIcon(free: IconType, commercial: IconType): IconType =
        if (properties.isCommercialFontPresent) commercial else free

    /** The archive section lists links pointing to previous newsletters with their studies. */
    private fun newArchiveSectionWithPreviousNewsletters() {
        queue(newLabel("h2ArchiveTitle"))
        queue(newNewsletterArchive("archive"))
    }

    private fun newLabel(id: String) = Label(
        id,
        StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null)
    )

    private fun newNewsletterArchive(id: String): ListView<Newsletter> {
        val newsletterCount = properties.numberOfPreviousNewslettersInArchive
        val newsletters = newStudyTopicService.findArchivedNewsletters(newsletterCount, languageCode)
        return object : ListView<Newsletter>(id, newsletters) {
            private val serialVersionUID: Long = 1L
            override fun populateItem(nl: ListItem<Newsletter>) {
                nl.add(newLinkToArchivedNewsletter("monthName", nl))
            }
        }
    }

    private fun newLinkToArchivedNewsletter(id: String, newsletter: ListItem<Newsletter>): Link<Newsletter> {
        val pp = PageParameters()
        pp[PublicPageParameters.ISSUE.parameterName] = newsletter.modelObject.issue
        val monthName = newsletter.modelObject.getMonthName(languageCode)
        return object : BootstrapLink<Newsletter>(id, Buttons.Type.Link) {
            private val serialVersionUID: Long = 1L
            override fun onClick() = setResponsePage(NewStudyListPage(pp))
        }.apply {
            setLabel(Model.of(monthName))
            setIconType(chooseIcon(FontAwesome7IconType.link_s, IcoMoonIconType.link))
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val TARGET = "target"
        private const val BLANK = "_blank"
    }
}
