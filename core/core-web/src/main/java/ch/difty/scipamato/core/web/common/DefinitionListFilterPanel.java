package ch.difty.scipamato.core.web.common;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.core.web.DefinitionProvider;

@SuppressWarnings("SameParameterValue")
public abstract class DefinitionListFilterPanel<T extends DefinitionEntity<?, ?>, F extends ScipamatoFilter, S extends DefinitionProviderService<T, F>, P extends DefinitionProvider<T, F, S>>
    extends BasePanel<T> {

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private final P dataProvider;

    protected DefinitionListFilterPanel(@NotNull final String id, @NotNull final P provider) {
        super(id);
        this.dataProvider = provider;
    }

    @Nullable
    protected F getFilter() {
        return dataProvider.getFilterState();
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        makeAndQueueFilterForm("filterForm");
    }

    private void makeAndQueueFilterForm(@NotNull final String id) {
        queue(new FilterForm<>(id, dataProvider));
        queueFilterFormFields();
    }

    protected abstract void queueFilterFormFields();
}
