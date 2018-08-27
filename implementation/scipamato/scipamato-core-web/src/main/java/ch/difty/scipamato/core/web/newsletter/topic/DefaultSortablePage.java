package ch.difty.scipamato.core.web.newsletter.topic;

import java.time.LocalDate;
import java.util.List;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.BasePage;

public class DefaultSortablePage extends BasePage<Newsletter> {
    private static final long serialVersionUID = 1L;

    private List<NewsletterNewsletterTopic> topics;
    private int                             newsletterId = 1;

    @SpringBean
    private NewsletterTopicService service;

    public DefaultSortablePage(final PageParameters parameters) {
        super(parameters);
        setModel(newDefaultModel());
    }

    private Model<Newsletter> newDefaultModel() {
        final Newsletter nl = new Newsletter("1806", LocalDate.now(), PublicationStatus.WIP);
        nl.setId(1);
        return Model.of(nl);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        topics = service.getSortableNewsletterTopicsForNewsletter(newsletterId);

        queue(newHeader("header"));
        queue(new Form<Void>("form"));
        queue(newSortable("sortable", topics));
        queue(newSubmitButton("submit"));
    }

    private Label newHeader(final String id) {
        final StringResourceModel model = new StringResourceModel(id + ".label", this, getModel());
        return new Label(id, model);
    }

    private void alignSortToIndex(final List<NewsletterNewsletterTopic> topics) {
        int index = 0;
        for (final NewsletterNewsletterTopic t : topics)
            t.setSort(index++);
    }

    private Sortable<NewsletterNewsletterTopic> newSortable(final String id,
        final List<NewsletterNewsletterTopic> list) {
        return new Sortable<>(id, list) {

            private static final long serialVersionUID = 1L;

            @Override
            protected HashListView<NewsletterNewsletterTopic> newListView(
                IModel<List<NewsletterNewsletterTopic>> model) {
                return DefaultSortablePage.newListView("items", model);
            }

            @Override
            public void onUpdate(AjaxRequestTarget target, NewsletterNewsletterTopic item, int index) {
                // Will update the model object with the new order
                // Remove the call to super if you do not want your model to be updated (or you use a LDM)
                super.onUpdate(target, item, index);
                //                target.add(getFeedbackPanel());
            }
        };
    }

    protected static Sortable.HashListView<NewsletterNewsletterTopic> newListView(String id,
        IModel<List<NewsletterNewsletterTopic>> model) {
        return new Sortable.HashListView<NewsletterNewsletterTopic>(id, model) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<NewsletterNewsletterTopic> item) {
                item.add(
                    new EmptyPanel("icon").add(AttributeModifier.append("class", "ui-icon " + JQueryIcon.ARROW_2_N_S)));
                item.add(new Label("item", item
                    .getModelObject()
                    .getDisplayValue()));
                item.add(AttributeModifier.append("class", "ui-state-default"));
            }

        };
    }

    private BootstrapAjaxButton newSubmitButton(final String id) {
        return new BootstrapAjaxButton(id, new StringResourceModel(id + ".label", this, null), Buttons.Type.Primary) {

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);
                alignSortToIndex(topics);
                this.info("The list order is now: " + topics);
                target.add(getFeedbackPanel());
            }
        };
    }
}
