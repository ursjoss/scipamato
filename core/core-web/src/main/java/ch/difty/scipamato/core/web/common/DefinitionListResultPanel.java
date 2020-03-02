package ch.difty.scipamato.core.web.common;

import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.jetbrains.annotations.NotNull;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.SerializableFunction;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.core.web.DefinitionProvider;

@SuppressWarnings("SameParameterValue")
public abstract class DefinitionListResultPanel<T extends DefinitionEntity, F extends ScipamatoFilter, S extends DefinitionProviderService<T, F>, P extends DefinitionProvider<T, F, S>>
    extends BasePanel<T> {

    private static final int    ROWS_PER_PAGE = 10;
    private static final String COLUMN_HEADER = "column.header.";

    private final P dataProvider;

    protected DefinitionListResultPanel(@NotNull final String id, @NotNull final P provider) {
        super(id);
        this.dataProvider = provider;
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        makeAndQueueTable("results");
    }

    private void makeAndQueueTable(final String id) {
        final DataTable<T, String> results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider,
            ROWS_PER_PAGE);
        results.setOutputMarkupId(true);
        results.add(new TableBehavior()
            .striped()
            .hover());
        queue(results);
    }

    @NotNull
    protected abstract List<IColumn<T, String>> makeTableColumns();

    @NotNull
    protected PropertyColumn<T, String> makePropertyColumn(@NotNull final String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression);
    }

    @NotNull
    protected ClickablePropertyColumn<T, String> makeClickableColumn(@NotNull final String propExpression,
        @NotNull final SerializableConsumer<IModel<T>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
            propExpression, propExpression, consumer);
    }

    @NotNull
    protected PropertyColumn<T, String> makeBooleanPropertyColumn(@NotNull final String propExpression,
        @NotNull final SerializableFunction<T, Boolean> predicate) {
        final String trueLabel = new StringResourceModel(propExpression + ".true", this, null).getString();
        final String falseLabel = new StringResourceModel(propExpression + ".false", this, null).getString();
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression) {

            @Override
            public IModel<?> getDataModel(final IModel<T> rowModel) {
                return (Model.of(Boolean.TRUE.equals(predicate.apply(rowModel.getObject())) ? trueLabel : falseLabel));
            }
        };
    }
}
