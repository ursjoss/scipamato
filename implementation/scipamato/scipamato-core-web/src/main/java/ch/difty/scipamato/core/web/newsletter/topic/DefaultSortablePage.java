package ch.difty.scipamato.core.web.newsletter.topic;

import java.util.List;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.interaction.sortable.Sortable;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Generics;

import ch.difty.scipamato.core.web.common.BasePage;

public class DefaultSortablePage extends BasePage<Void> {
    private static final long serialVersionUID = 1L;

    public DefaultSortablePage(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final List<String> list = newList("item #1", "item #2", "item #3", "item #4", "item #5", "item #6");

        // Sortable //
        final Sortable<String> sortable = new Sortable<String>("sortable", list) {

            private static final long serialVersionUID = 1L;

            @Override
            protected HashListView<String> newListView(IModel<List<String>> model) {
                return DefaultSortablePage.newListView("items", model);
            }

            @Override
            public void onUpdate(AjaxRequestTarget target, String item, int index) {
                // Will update the model object with the new order
                // Remove the call to super if you do not want your model to be updated (or you use a LDM)
                super.onUpdate(target, item, index);

                this.info(String.format("'%s' has moved to position %d", item, index + 1));
                this.info("The list order is now: " + this.getModelObject());

                target.add(getFeedbackPanel());
            }
        };

        this.add(sortable);
    }

    protected static Sortable.HashListView<String> newListView(String id, IModel<List<String>> model) {
        return new Sortable.HashListView<String>(id, model) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<String> item) {
                item.add(
                    new EmptyPanel("icon").add(AttributeModifier.append("class", "ui-icon " + JQueryIcon.ARROW_2_N_S)));
                item.add(new Label("item", item.getModelObject()));
                item.add(AttributeModifier.append("class", "ui-state-default"));
            }
        };
    }

    /**
     * Gets a new <i>modifiable</i> list
     */
    private static List<String> newList(String... items) {
        List<String> list = Generics.newArrayList();

        for (String item : items) {
            list.add(item);
        }

        return list;
    }
}
