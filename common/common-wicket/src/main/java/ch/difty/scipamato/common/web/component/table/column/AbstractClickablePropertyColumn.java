package ch.difty.scipamato.common.web.component.table.column;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract BaseClass for ClickablePropertyColumn implementations
 * {@code Apache Wicket Cookbook} - thanks to Igor Vaynberg.
 *
 * @param <T>
 *     the type of the object that will be rendered in this column's
 *     cells
 * @param <S>
 *     the type of the sort property
 * @author u.joss
 */
@SuppressWarnings({ "SameParameterValue" })
public abstract class AbstractClickablePropertyColumn<T, S> extends AbstractColumn<T, S> {
    private static final long serialVersionUID = 1L;

    private final String  property;
    private final boolean inNewTab;

    AbstractClickablePropertyColumn(@NotNull IModel<String> displayModel, @Nullable S sort, @NotNull String property,
        boolean inNewTab) {
        super(displayModel, sort);
        this.property = property;
        this.inNewTab = inNewTab;
    }

    @NotNull
    protected String getProperty() {
        return property;
    }

    @Override
    public void populateItem(@NotNull Item<ICellPopulator<T>> cellItem, @NotNull String componentId,
        @Nullable IModel<T> rowModel) {
        cellItem.add(new LinkPanel(componentId, rowModel, new PropertyModel<>(rowModel, getProperty())));
    }

    protected abstract void onClick(@Nullable IModel<T> clicked);

    private class LinkPanel extends Panel {
        private static final long serialVersionUID = 1L;

        LinkPanel(@NotNull String id, @Nullable IModel<T> rowModel, @Nullable IModel<?> labelModel) {
            super(id);
            Link<T> link = new Link<>("link", rowModel) {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick() {
                    AbstractClickablePropertyColumn.this.onClick(getModel());
                }

                @Override
                protected void onComponentTag(@NotNull final ComponentTag tag) {
                    super.onComponentTag(tag);
                    if (inNewTab)
                        tag.put("target", "_blank");
                }
            };
            add(link);
            link.add(new Label("label", labelModel));
        }
    }
}
