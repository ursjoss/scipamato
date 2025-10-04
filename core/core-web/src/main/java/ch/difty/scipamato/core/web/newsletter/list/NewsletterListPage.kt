package ch.difty.scipamato.core.web.newsletter.list

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.common.web.component.SerializableConsumer
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn
import ch.difty.scipamato.common.web.component.table.column.LinkIconColumn
import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter.NewsletterFilterFields
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic
import ch.difty.scipamato.core.persistence.NewsletterService
import ch.difty.scipamato.core.web.common.BasePage
import ch.difty.scipamato.core.web.fixed
import ch.difty.scipamato.core.web.model.NewsletterTopicModel
import ch.difty.scipamato.core.web.newsletter.NewsletterProvider
import ch.difty.scipamato.core.web.newsletter.edit.NewsletterEditPage
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome7IconTypeBuilder
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior
import org.apache.wicket.ajax.markup.html.AjaxLink
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.ChoiceRenderer
import org.apache.wicket.markup.html.form.EnumChoiceRenderer
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.apache.wicket.spring.injection.annot.SpringBean
import org.wicketstuff.annotation.mount.MountPath

/**
 * Page to list all newsletters and apply simple filters to limit the results.
 *
 * Offers the option to create a new newsletter.
 */
@MountPath("/newsletters")
@AuthorizeInstantiation(Roles.USER, Roles.ADMIN)
@Suppress("SameParameterValue", "TooManyFunctions")
class NewsletterListPage(parameters: PageParameters?) : BasePage<Unit>(parameters) {

    @SpringBean
    private lateinit var service: NewsletterService

    private val filter: NewsletterFilter = NewsletterFilter()
    private val dataProvider: NewsletterProvider = NewsletterProvider(filter)

    private var results: DataTable<Newsletter, String>? = null
    private var newNewsletterButton: BootstrapAjaxButton? = null

    override fun onInitialize() {
        super.onInitialize()
        makeAndQueueFilterForm("filterForm")
        makeAndQueueTable("results")
    }

    private fun makeAndQueueFilterForm(id: String) {
        queue(FilterForm(id, dataProvider))
        queueFieldAndLabel(
            TextField(
                Newsletter.NewsletterFields.ISSUE.fieldName,
                PropertyModel.of<Any>(filter, NewsletterFilterFields.ISSUE_MASK.fieldName)
            )
        )
        queueStatusSelectAndLabel(Newsletter.NewsletterFields.PUBLICATION_STATUS.fieldName)
        queueTopicsSelectAndLabel(Newsletter.NewsletterFields.TOPICS.fieldName)
        queueNewButton("newNewsletter")
    }

    private fun queueStatusSelectAndLabel(id: String) {
        StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null).also {
            queue(Label("$id$LABEL_TAG", it))
        }
        val selectionModel =
            PropertyModel.of<PublicationStatus>(filter, NewsletterFilterFields.PUBLICATION_STATUS.fieldName)

        @Suppress("SpreadOperator")
        val choicesModel = Model.ofList(PublicationStatus.entries)
        BootstrapSelect(id, selectionModel, choicesModel, EnumChoiceRenderer(this)).apply {
            isNullValid = true
            add(filterFormFieldUpdatingBehavior())
        }.also { queue(it) }
    }

    private fun filterFormFieldUpdatingBehavior() = object :
        AjaxFormComponentUpdatingBehavior("change") {
        private val serialVersionUID: Long = 1L
        override fun onUpdate(target: AjaxRequestTarget) {
            target.add(results)
        }
    }

    private fun queueTopicsSelectAndLabel(id: String) {
        StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null).also {
            queue(Label(id + LABEL_TAG, it))
        }
        val selectionModel =
            PropertyModel.of<NewsletterTopic>(filter, NewsletterFilterFields.NEWSLETTER_TOPIC_ID.fieldName)
        val choicesModel = NewsletterTopicModel(languageCode)
        val choiceRenderer = ChoiceRenderer<NewsletterTopic>(
            NewsletterTopic.NewsletterTopicFields.TITLE.fieldName,
            NewsletterTopic.NewsletterTopicFields.ID.fieldName
        )
        val noneSelectedModel = StringResourceModel("$id.noneSelected", this, null)
        val config = BootstrapSelectConfig()
            .withNoneSelectedText(noneSelectedModel.getObject())
            .withLiveSearch(true)
        BootstrapSelect(id, selectionModel, choicesModel, choiceRenderer).with(config).apply {
            isNullValid = true
            add(filterFormFieldUpdatingBehavior())
        }.also { queue(it) }
    }

    override fun setResponsePageButtonEnabled(): Boolean = service.canCreateNewsletterInProgress()

    private fun makeAndQueueTable(id: String) {
        results = BootstrapDefaultDataTable(id, makeTableColumns(), dataProvider, ROWS_PER_PAGE.toLong()).apply {
            outputMarkupId = true
            add(TableBehavior().striped().hover())
        }
        queue(results)
    }

    private fun makeTableColumns(): List<IColumn<Newsletter, String>> =
        mutableListOf<IColumn<Newsletter, String>>().apply {
            add(makeClickableColumn(Newsletter.NewsletterFields.ISSUE.fieldName) { newsletterModel ->
                onTitleClick(newsletterModel)
            })
            add(makePropertyColumn(Newsletter.NewsletterFields.ISSUE_DATE.fieldName))
            add(makeEnumPropertyColumn(Newsletter.NewsletterFields.PUBLICATION_STATUS.fieldName))
            add(makeSortTopicLinkColumn("sortTopics"))
            add(makeRemoveLinkColumn("remove"))
        }

    private fun makeClickableColumn(
        propExpression: String,
        action: SerializableConsumer<IModel<Newsletter>>,
    ): ClickablePropertyColumn<Newsletter, String> =
        ClickablePropertyColumn(StringResourceModel(
            "$COLUMN_HEADER$propExpression",
            this,
            null
        ), propExpression, action, propExpression)

    private fun onTitleClick(newsletterModel: IModel<Newsletter>) {
        setResponsePage(NewsletterEditPage(newsletterModel))
    }

    private fun makePropertyColumn(propExpression: String): PropertyColumn<Newsletter, String> =
        PropertyColumn(StringResourceModel("$COLUMN_HEADER$propExpression", this, null), propExpression, propExpression)

    /**
     * provides the localized values for the publication status as defined in the properties files.
     */
    private fun makeEnumPropertyColumn(propExpression: String): PropertyColumn<Newsletter, String> = object :
        PropertyColumn<Newsletter, String>(
            StringResourceModel(COLUMN_HEADER + propExpression, this, null),
            propExpression,
            propExpression
        ) {
        private val serialVersionUID: Long = 1L
        override fun getDataModel(rowModel: IModel<Newsletter?>): IModel<String> {
            val dataModel = super.getDataModel(rowModel)
            val ps = dataModel.getObject() as PublicationStatus
            return StringResourceModel("PublicationStatus.${ps.name}", this@NewsletterListPage, null)
        }
    }

    private fun makeSortTopicLinkColumn(id: String): IColumn<Newsletter, String> {
        val shuffle = FontAwesome7IconTypeBuilder.FontAwesome7Solid.shuffle.fixed()
        return object : LinkIconColumn<Newsletter>(
            StringResourceModel("$COLUMN_HEADER$id", this@NewsletterListPage, null)
        ) {
            private val serialVersionUID: Long = 1L
            override fun createIconModel(rowModel: IModel<Newsletter>): IModel<String> =
                Model.of(shuffle.cssClassName())

            override fun createTitleModel(rowModel: IModel<Newsletter>): IModel<String> =
                StringResourceModel("column.title.$id", this@NewsletterListPage, rowModel)

            override fun onClickPerformed(
                target: AjaxRequestTarget,
                rowModel: IModel<Newsletter>,
                link: AjaxLink<Void>,
            ) {
                setResponsePage(NewsletterTopicSortPage(rowModel, pageReference))
            }
        }
    }

    private fun makeRemoveLinkColumn(id: String): IColumn<Newsletter, String> {
        val trash = FontAwesome7IconTypeBuilder.FontAwesome7Solid.trash_can.fixed()
        return object : LinkIconColumn<Newsletter>(
            StringResourceModel("$COLUMN_HEADER$id", this@NewsletterListPage, null)
        ) {
            private val serialVersionUID: Long = 1L
            override fun createIconModel(rowModel: IModel<Newsletter>): IModel<String> =
                Model.of(if (rowModel.getObject().isDeletable) trash.cssClassName() else "")

            override fun createTitleModel(rowModel: IModel<Newsletter>): IModel<String> =
                StringResourceModel("column.title.$id", this@NewsletterListPage, rowModel)

            override fun onClickPerformed(
                target: AjaxRequestTarget,
                rowModel: IModel<Newsletter>,
                link: AjaxLink<Void>,
            ) {
                val nl = rowModel.getObject()
                if (nl.isDeletable) {
                    service.remove(nl)
                    info(StringResourceModel(
                        "newsletter.deleted.success", this@NewsletterListPage, rowModel
                    ).string)
                    target.add(results)
                    target.add(newNewsletterButton)
                    target.add(feedbackPanel)
                }
            }
        }
    }

    private fun queueNewButton(id: String) {
        newNewsletterButton = newResponsePageButton(id) { NewsletterEditPage() }.also { queue(it) }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val COLUMN_HEADER = "column.header."
        private const val ROWS_PER_PAGE = 10
    }
}
