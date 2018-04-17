package ch.difty.scipamato.common.web.component.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Abstract BaseClass for ClickablePropertyColumn implementations
 * {@code Apache Wicket Cookbook} - thanks to Igor Vaynberg.
 *
 * @author u.joss
 *
 * @param <T>
 *            the type of the object that will be rendered in this column's
 *            cells
 * @param <S>
 *            the type of the sort property
 */
public abstract class AbstractClickablePropertyColumn<T, S> extends AbstractColumn<T, S> {
    private static final long serialVersionUID = 1L;

    private final String property;

    AbstractClickablePropertyColumn(IModel<String> displayModel, S sort, String property) {
        super(displayModel, sort);
        this.property = property;
    }

    protected String getProperty() {
        return property;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new LinkPanel(componentId, rowModel, new PropertyModel<>(rowModel, getProperty())));
    }

    protected abstract void onClick(IModel<T> clicked);

    private class LinkPanel extends Panel {
        private static final long serialVersionUID = 1L;

        LinkPanel(String id, IModel<T> rowModel, IModel<?> labelModel) {
            super(id);
            Link<T> link = new Link<T>("link", rowModel) {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick() {
                    AbstractClickablePropertyColumn.this.onClick(getModel());
                }
            };
            add(link);
            link.add(new Label("label", labelModel));
        }
    }
}
