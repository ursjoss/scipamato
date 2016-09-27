package ch.difty.sipamato.web.component.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ch.difty.sipamato.web.component.SerializableConsumer;

/**
 * ClickablePropertyColumn, a lambdified version of the one provided in
 * <literal>Apache Wicket Cookbook</literal> - thanks to Igor Vaynberg.
 *
 * @author u.joss
 *
 * @param <T> the type of the object that will be rendered in this column's cells
 * @param <S> the type of the sort property
 */
public class ClickablePropertyColumn<T, S> extends AbstractColumn<T, S> {
    private static final long serialVersionUID = 1L;

    private final String property;
    private final SerializableConsumer<IModel<T>> consumer;

    public ClickablePropertyColumn(IModel<String> displayModel, String property, SerializableConsumer<IModel<T>> consumer) {
        this(displayModel, property, null, consumer);
    }

    public ClickablePropertyColumn(IModel<String> displayModel, String property, S sort, SerializableConsumer<IModel<T>> consumer) {
        super(displayModel, sort);
        this.property = property;
        this.consumer = consumer;
    }

    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new LinkPanel(componentId, rowModel, new PropertyModel<Object>(rowModel, property)));
    }

    protected void onClick(IModel<T> clicked) {
        consumer.accept(clicked);
    };

    private class LinkPanel extends Panel {
        private static final long serialVersionUID = 1L;

        public LinkPanel(String id, IModel<T> rowModel, IModel<?> labelModel) {
            super(id);
            Link<T> link = new Link<T>("link", rowModel) {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick() {
                    ClickablePropertyColumn.this.onClick(getModel());
                }
            };
            add(link);
            link.add(new Label("label", labelModel));
        }
    }
}
