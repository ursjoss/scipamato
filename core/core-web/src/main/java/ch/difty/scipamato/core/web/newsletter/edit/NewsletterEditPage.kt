package ch.difty.scipamato.core.web.newsletter.edit

import ch.difty.scipamato.common.entity.newsletter.PublicationStatus
import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.web.LABEL_RESOURCE_TAG
import ch.difty.scipamato.common.web.LABEL_TAG
import ch.difty.scipamato.common.web.Mode
import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.search.PaperFilter
import ch.difty.scipamato.core.persistence.NewsletterService
import ch.difty.scipamato.core.persistence.OptimisticLockingException
import ch.difty.scipamato.core.web.common.BasePage
import ch.difty.scipamato.core.web.paper.NewsletterChangeEvent
import ch.difty.scipamato.core.web.paper.PaperSlimByPaperFilterProvider
import ch.difty.scipamato.core.web.paper.result.ResultPanel
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.LocalDateTextField
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome5CDNCSSReference
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.bean.validation.PropertyValidator
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.head.CssHeaderItem
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.EnumChoiceRenderer
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.model.CompoundPropertyModel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.Model
import org.apache.wicket.model.PropertyModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.spring.injection.annot.SpringBean
import org.wicketstuff.annotation.mount.MountPath

private val log = logger()

@Suppress("SameParameterValue")
@MountPath("newsletters/entry")
@AuthorizeInstantiation(Roles.USER, Roles.ADMIN)
class NewsletterEditPage(model: IModel<Newsletter>?) : BasePage<Newsletter>(getModelOrDefaultModelFrom(model)) {

    @SpringBean
    private lateinit var service: NewsletterService

    private var dataProvider: PaperSlimByPaperFilterProvider? = null
    private var newNewsletter = false

    constructor() : this(null) {
        newNewsletter = true
    }

    init {
        val filter = PaperFilter().apply {
            newsletterId = relevantNewsletterId
        }
        dataProvider = PaperSlimByPaperFilterProvider(filter, RESULT_PAGE_SIZE)
        updateNavigateable()
    }

    /**
     * Have the provider provide a list of all paper ids matching the current
     * filter. Construct a navigateable with this list and set it into the session
     */
    private fun updateNavigateable() {
        paperIdManager.initialize(dataProvider!!.findAllPaperIdsByFilter())
    }

    /**
     * If we have a persisted newsletter, we use its id to filter the assigned papers.
     * A new newsletter however does not yet have an id. If we'd use the null id as filter
     * criterion, we'd get all papers back. So we need to apply a dummy newsletter id that
     * does not exist and therefore will not return any papers as assignments.
     */
    private val relevantNewsletterId: Int?
        get() = if (modelObject!!.id != null) modelObject!!.id else -1

    override fun renderHead(response: IHeaderResponse) {
        super.renderHead(response)
        response.render(CssHeaderItem.forReference(FontAwesome5CDNCSSReference.instance()))
    }

    override fun onEvent(event: IEvent<*>) {
        super.onEvent(event)
        if (event.payload.javaClass == NewsletterChangeEvent::class.java) {
            val target = (event.payload as NewsletterChangeEvent).target
            target.add(feedbackPanel)
            event.dontBroadcastDeeper()
        }
    }

    override fun onInitialize() {
        super.onInitialize()
        queueForm("form")
        queueFieldAndLabel(newIssueField(Newsletter.NewsletterFields.ISSUE.fieldName), PropertyValidator<Any>())
        queueFieldAndLabel(newIssueDateField(Newsletter.NewsletterFields.ISSUE_DATE.fieldName), PropertyValidator<Any>())
        makeAndQueuePublicationStatusSelectBox(Newsletter.NewsletterFields.PUBLICATION_STATUS.fieldName)
        queueSubmitButton("submit")
        makeAndQueueResultPanel("resultPanel")
    }

    private fun newIssueField(id: String): TextField<String> = object :
        TextField<String>(id) {
        override fun onConfigure() {
            super.onConfigure()
            isEnabled = isInStatusInProgress
        }
    }

    private val isInStatusInProgress: Boolean
        get() = modelObject != null && modelObject.publicationStatus.isInProgress

    private fun newIssueDateField(id: String): LocalDateTextField =
        object : LocalDateTextField(id, StringResourceModel("date.format", this@NewsletterEditPage, null).string) {
            override fun onConfigure() {
                super.onConfigure()
                isEnabled = isInStatusInProgress
            }
        }

    private fun queueSubmitButton(id: String) {
        queue(BootstrapButton(id, StringResourceModel("submit.label"), Buttons.Type.Default))
    }

    private fun queueForm(id: String) {
        val form: Form<Newsletter> = object : Form<Newsletter>(id, CompoundPropertyModel(model)) {
            override fun onSubmit() {
                super.onSubmit()
                doUpdate()
            }
        }
        queue(form)
    }

    private fun makeAndQueuePublicationStatusSelectBox(id: String) {
        val publicationStatus = object : BootstrapSelect<PublicationStatus>(
            id,
            PropertyModel(model, Newsletter.NewsletterFields.PUBLICATION_STATUS.fieldName),
            listOf(*PublicationStatus.values()),
            EnumChoiceRenderer(this)
        ) {
            override fun onConfigure() {
                super.onConfigure()
                this.isEnabled = !newNewsletter
            }
        }
        queue(publicationStatus)
        queue(Label("$id$LABEL_TAG", StringResourceModel("$id$LABEL_RESOURCE_TAG", this, null)))
    }

    private fun doUpdate() {
        try {
            val persisted = service.saveOrUpdate(modelObject!!)
            if (persisted != null) {
                modelObject = persisted
                info(StringResourceModel("save.successful.hint", this, null)
                    .setParameters(nullSafeId, modelObject!!.issue).string)
            } else {
                error(StringResourceModel("save.error.hint", this, null)
                    .setParameters(nullSafeId, "").string)
            }
        } catch (ole: OptimisticLockingException) {
            val msg = StringResourceModel("save.optimisticlockexception.hint", this, null)
                .setParameters(ole.tableName, nullSafeId).string
            log.error(msg)
            error(msg)
        } catch (iae: IllegalArgumentException) {
            val msg = StringResourceModel(iae.message, this, null).string
            error(msg)
        } catch (ex: Exception) {
            val msg = StringResourceModel("save.error.hint", this, null)
                .setParameters(nullSafeId, ex.message).string
            log.error(msg)
            error(msg)
        }
    }

    private val nullSafeId: Long
        get() = modelObject!!.id?.toLong() ?: 0L

    private fun makeAndQueueResultPanel(id: String) {
        val resultPanel = object : ResultPanel(id, dataProvider!!, Mode.EDIT) {
            override val isOfferingSearchComposition: Boolean
                get() = false
        }
        resultPanel.outputMarkupId = true
        queue(resultPanel)
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val RESULT_PAGE_SIZE = 12

        private fun getModelOrDefaultModelFrom(model: IModel<Newsletter>?): IModel<Newsletter> = model
            ?: newDefaultModel()

        private fun newDefaultModel(): IModel<Newsletter> =
            Model.of(Newsletter().apply {
                publicationStatus = PublicationStatus.WIP
            })
    }

}
