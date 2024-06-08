package ch.difty.scipamato.core.web.newsletter.list

import ch.difty.scipamato.core.entity.newsletter.Newsletter
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic
import ch.difty.scipamato.core.persistence.NewsletterTopicService
import ch.difty.scipamato.core.web.common.BasePage
import ch.difty.scipamato.core.web.paper.list.PaperListPage
import com.googlecode.wicket.jquery.ui.JQueryIcon
import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable
import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable.HashListView
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons
import org.apache.wicket.AttributeModifier
import org.apache.wicket.PageReference
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.panel.EmptyPanel
import org.apache.wicket.model.IModel
import org.apache.wicket.model.StringResourceModel
import org.apache.wicket.spring.injection.annot.SpringBean

@Suppress("SameParameterValue")
class NewsletterTopicSortPage(
    model: IModel<Newsletter>,
    private val previousPageRef: PageReference?
) : BasePage<Newsletter>(model) {

    @SpringBean
    private lateinit var service: NewsletterTopicService

    private lateinit var topics: List<NewsletterNewsletterTopic>

    override fun onInitialize() {
        super.onInitialize()

        check(modelObject?.id != null) { "Cannot start page w/o non-null newsletter topic id in model" }
        val newsletterTopicId = modelObject!!.id!!
        service.removeObsoleteNewsletterTopicsFromSort(newsletterTopicId)
        topics = service.getSortedNewsletterTopicsForNewsletter(newsletterTopicId)

        queue(newHeader("header"))
        queue(Form<Unit>("form"))
        queue(newSortable("sortable", topics))
        queue(newSubmitButton("submit"))
        queue(newCancelButton("cancel"))
    }

    private fun newHeader(id: String): Label =
        Label(id, StringResourceModel("$id$LABEL", this, this.model))

    private fun newSortable(
        id: String,
        topics: List<NewsletterNewsletterTopic>
    ): Sortable<NewsletterNewsletterTopic> =
        object : Sortable<NewsletterNewsletterTopic>(id, topics) {
            private val serialVersionUID: Long = 1L
            override fun newListView(
                model: IModel<List<NewsletterNewsletterTopic>>?
            ): HashListView<NewsletterNewsletterTopic> =
                newListView("items", model)
        }

    private fun newSubmitButton(id: String): BootstrapAjaxButton =
        object : BootstrapAjaxButton(
            id,
            StringResourceModel("$id$LABEL", this, null),
            Buttons.Type.Primary
        ) {
            private val serialVersionUID: Long = 1L
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                alignSortToIndex(topics)
                @Suppress("TooGenericExceptionCaught")
                try {
                    service.saveSortedNewsletterTopics(
                        this@NewsletterTopicSortPage.modelObject!!.id!!, topics
                    )
                    previousPageRef?.let { pageRef ->
                        setResponsePage(pageRef.page)
                    } ?: setResponsePage(PaperListPage::class.java)
                } catch (ex: Exception) {
                    error("Unexpected error: ${ex.message}")
                }
            }
        }

    private fun alignSortToIndex(topics: List<NewsletterNewsletterTopic>) {
        var index = 0
        topics.forEach { t ->
            t.sort = index++
        }
    }

    private fun newCancelButton(id: String): BootstrapAjaxButton {
        return object : BootstrapAjaxButton(
            id,
            StringResourceModel(id + LABEL, this, null),
            Buttons.Type.Default
        ) {
            private val serialVersionUID: Long = 1L
            override fun onSubmit(target: AjaxRequestTarget) {
                super.onSubmit(target)
                if (previousPageRef != null)
                    setResponsePage(previousPageRef.page)
                else setResponsePage(PaperListPage::class.java)
            }
        }
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val LABEL = ".label"

        private fun newListView(
            id: String,
            model: IModel<List<NewsletterNewsletterTopic>>?
        ): HashListView<NewsletterNewsletterTopic> =
            object : HashListView<NewsletterNewsletterTopic>(id, model) {
                private val serialVersionUID: Long = 1L
                override fun populateItem(item: ListItem<NewsletterNewsletterTopic?>?) {
                    item?.apply {
                        add(
                            EmptyPanel("icon").add(
                                AttributeModifier.append("class", "ui-icon ${JQueryIcon.ARROW_2_N_S}")
                            )
                        )
                        add(Label("item", item.modelObject!!.displayValue ?: "n.a."))
                        add(AttributeModifier.append("class", "ui-state-default"))
                    }
                }
            }
    }
}
