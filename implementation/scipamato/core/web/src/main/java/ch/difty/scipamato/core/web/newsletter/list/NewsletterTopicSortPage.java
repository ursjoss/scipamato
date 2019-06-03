package ch.difty.scipamato.core.web.newsletter.list;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterNewsletterTopic;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.BasePage;
import ch.difty.scipamato.core.web.paper.list.PaperListPage;

@SuppressWarnings("SameParameterValue")
public class NewsletterTopicSortPage extends BasePage<Newsletter> {

    private static final long serialVersionUID = 1L;

    private static final String LABEL = ".label";

    private       List<NewsletterNewsletterTopic> topics = new ArrayList<>();
    private final PageReference                   previousPageRef;

    @SpringBean
    private NewsletterTopicService service;

    NewsletterTopicSortPage(final IModel<Newsletter> model, final PageReference previous) {
        super(model);
        this.previousPageRef = previous;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        if (getModelObject() != null)
            topics = service.getSortedNewsletterTopicsForNewsletter(getModelObject().getId());

        queue(newHeader("header"));
        queue(new Form<Void>("form"));
        queue(newSortable("sortable", topics));
        queue(newSubmitButton("submit"));
        queue(newCancelButton("cancel"));
    }

    private Label newHeader(final String id) {
        final StringResourceModel model = new StringResourceModel(id + LABEL, this, getModel());
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
                return NewsletterTopicSortPage.newListView("items", model);
            }
        };
    }

    private static Sortable.HashListView<NewsletterNewsletterTopic> newListView(String id,
        IModel<List<NewsletterNewsletterTopic>> model) {
        return new Sortable.HashListView<>(id, model) {

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
        return new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL, this, null), Buttons.Type.Primary) {

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);
                alignSortToIndex(topics);

                try {
                    service.saveSortedNewsletterTopics(NewsletterTopicSortPage.this
                        .getModelObject()
                        .getId(), topics);
                    if (previousPageRef != null)
                        setResponsePage(previousPageRef.getPage());
                    else
                        setResponsePage(PaperListPage.class);
                } catch (Exception ex) {
                    error("Unexpected error: " + ex.getMessage());
                }

            }
        };
    }

    private BootstrapAjaxButton newCancelButton(final String id) {
        return new BootstrapAjaxButton(id, new StringResourceModel(id + LABEL, this, null), Buttons.Type.Default) {

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);
                if (previousPageRef != null)
                    setResponsePage(previousPageRef.getPage());
                else
                    setResponsePage(PaperListPage.class);
            }
        };
    }
}
