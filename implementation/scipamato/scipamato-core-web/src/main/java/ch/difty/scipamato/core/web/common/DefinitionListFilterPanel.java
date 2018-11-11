package ch.difty.scipamato.core.web.common;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.core.web.DefinitionProvider;

@SuppressWarnings("SameParameterValue")
public abstract class DefinitionListFilterPanel<T extends DefinitionEntity, F extends ScipamatoFilter, S extends DefinitionProviderService<T, F>, P extends DefinitionProvider<T, F, S>>
    extends BasePanel<T> {

    private final P dataProvider;

    protected DefinitionListFilterPanel(final String id, final P provider) {
        super(id);
        this.dataProvider = provider;
    }

    protected F getFilter() {
        return dataProvider.getFilterState();
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        makeAndQueueFilterForm("filterForm");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<>(id, dataProvider));
        queueFilterFormFields();
    }

    protected abstract void queueFilterFormFields();

}
