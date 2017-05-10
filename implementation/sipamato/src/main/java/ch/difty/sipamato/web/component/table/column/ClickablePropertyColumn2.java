package ch.difty.sipamato.web.component.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import ch.difty.sipamato.web.component.SerializableBiConsumer;
import ch.difty.sipamato.web.component.SerializableSupplier;

/**
 * ClickablePropertyColumn, a lambdified version of the one provided in
 * {@code Apache Wicket Cookbook} - thanks to Igor Vaynberg.
 *
 * Adjusted to accept a BiConsumer instead of a Consumer, thus accepting two arguments in the constructor
 * of the referring page. In addition accepting a supplier to provide the additional argument for the biconsumer.
 *
 * @author u.joss
 *
 * @param <T> the type of the object that will be rendered in this column's cells
 * @param <S> the type of the sort property. It will be passed into the constructor of this class and will be final.
 * @param <U> the type of an additional argument passed as supplier to the (bi)consumer
 */
public class ClickablePropertyColumn2<T, S, U> extends AbstractColumn<T, S> {
    private static final long serialVersionUID = 1L;

    private final String property;
    private final SerializableBiConsumer<IModel<T>, U> biConsumer;
    private final SerializableSupplier<U> supplier;

    public ClickablePropertyColumn2(IModel<String> displayModel, String property, SerializableBiConsumer<IModel<T>, U> biConsumer, SerializableSupplier<U> supplier) {
        this(displayModel, null, property, biConsumer, supplier);
    }

    public ClickablePropertyColumn2(IModel<String> displayModel, S sort, String property, SerializableBiConsumer<IModel<T>, U> biConsumer, SerializableSupplier<U> supplier) {
        super(displayModel, sort);
        this.property = property;
        this.biConsumer = biConsumer;
        this.supplier = supplier;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
        cellItem.add(new LinkPanel(componentId, rowModel, new PropertyModel<Object>(rowModel, property)));
    }

    protected void onClick(IModel<T> clicked) {
        biConsumer.accept(clicked, supplier.get());
    }

    private class LinkPanel extends Panel {
        private static final long serialVersionUID = 1L;

        public LinkPanel(String id, IModel<T> rowModel, IModel<?> labelModel) {
            super(id);
            Link<T> link = new Link<T>("link", rowModel) {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick() {
                    ClickablePropertyColumn2.this.onClick(getModel());
                }
            };
            add(link);
            link.add(new Label("label", labelModel));
        }
    }
}
