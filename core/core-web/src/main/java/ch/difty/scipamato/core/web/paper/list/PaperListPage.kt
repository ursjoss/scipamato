package ch.difty.scipamato.core.web.paper.list

import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.entity.search.PaperFilter.PaperFilterFields
import ch.difty.scipamato.core.web.common.BasePage
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent
import ch.difty.scipamato.core.web.paper.PaperSlimByPaperFilterProvider
import ch.difty.scipamato.core.web.paper.entry.PaperEntryPage
import ch.difty.scipamato.core.web.paper.result.ResultPanel
import com.giffing.wicket.spring.boot.context.scan.WicketHomePage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5CDNCSSReference
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.event.IEvent
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import org.wicketstuff.annotation.mount.MountPath

/**
 * Page to list all papers and apply simple filters to limit the results.
 *
 * Offers the option to create new papers.
 */
@WicketHomePage
@MountPath("/")
@AuthorizeInstantiation(Roles.USER, Roles.ADMIN, Roles.VIEWER)
@Suppress("SameParameterValue")
class PaperListPage(parameters: PageParameters?) : BasePage<Void?>(parameters) {

    private val filter = PaperFilter()
    private val dataProvider = PaperSlimByPaperFilterProvider(filter, RESULT_PAGE_SIZE)

    private val mode: Mode = if (hasOneOfRoles(Roles.USER, Roles.ADMIN)) Mode.EDIT else Mode.VIEW

    private lateinit var resultPanel: ResultPanel

    init {
        updateNavigateable()
    }

    override fun onInitialize() {
        super.onInitialize()
        makeAndQueueFilterForm("searchForm")
        makeAndQueueResultPanel("resultPanel")
    }

    /**
     * Have the provider provide a list of all paper ids matching the current
     * filter. Construct a navigateable with this list and set it into the session
     */
    private fun updateNavigateable() {
        paperIdManager.initialize(dataProvider.findAllPaperIdsByFilter())
    }

    override fun renderHead(response: IHeaderResponse) {
        super.renderHead(response)
        response.render(CssHeaderItem.forReference(FontAwesome5CDNCSSReference.instance()))
    }

    override fun onEvent(event: IEvent<*>) {
        if (event.payload.javaClass == NewsletterChangeEvent::class.java) {
            (event.payload as NewsletterChangeEvent).target.run {
                add(feedbackPanel)
            }
            event.dontBroadcastDeeper()
        }
    }

    private fun makeAndQueueFilterForm(id: String) {
        queue(object : FilterForm<PaperFilter>(id, dataProvider) {
            override fun onSubmit() {
                super.onSubmit()
                updateNavigateable()
            }
        })
        queueFieldAndLabel(TextField("number", PropertyModel.of<Any>(filter, PaperFilterFields.NUMBER.fieldName)))
        queueFieldAndLabel(TextField("authorsSearch", PropertyModel.of<Any>(filter, PaperFilterFields.AUTHOR_MASK.fieldName)))
        queueFieldAndLabel(TextField("methodsSearch", PropertyModel.of<Any>(filter, PaperFilterFields.METHODS_MASK.fieldName)))
        queueFieldAndLabel(TextField("populationSearch", PropertyModel.of<Any>(filter, PaperFilterFields.POPULATION_MASK.fieldName)))
        queueFieldAndLabel(TextField("fieldSearch", PropertyModel.of<Any>(filter, PaperFilterFields.SEARCH_MASK.fieldName)))
        queueFieldAndLabel(TextField("pubYearFrom", PropertyModel.of<Any>(filter, PaperFilterFields.PUB_YEAR_FROM.fieldName)))
        queueFieldAndLabel(TextField("pubYearUntil", PropertyModel.of<Any>(filter, PaperFilterFields.PUB_YEAR_UNTIL.fieldName)))
        queueNewPaperButton("newPaper")
    }


    private fun makeAndQueueResultPanel(id: String) {
        resultPanel = object : ResultPanel(id, dataProvider, mode) {
            override val isOfferingSearchComposition: Boolean
                get() = false
        }.apply {
            outputMarkupId = true
        }.also {
            queue(it)
        }
    }

    private fun queueNewPaperButton(id: String) {
        newResponsePageButton(id) { PaperEntryPage(pageParameters, page.pageReference) }
            .apply {
                setType(Buttons.Type.Primary)
                isVisible = mode !== Mode.VIEW
            }.also { queue(it) }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val RESULT_PAGE_SIZE = 12
    }
}
