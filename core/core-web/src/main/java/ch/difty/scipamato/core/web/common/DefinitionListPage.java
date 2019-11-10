package ch.difty.scipamato.core.web.common;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.entity.DefinitionEntity;
import ch.difty.scipamato.common.entity.filter.ScipamatoFilter;
import ch.difty.scipamato.common.persistence.DefinitionProviderService;
import ch.difty.scipamato.core.web.DefinitionProvider;

@SuppressWarnings("SameParameterValue")
public abstract class DefinitionListPage<T extends DefinitionEntity, F extends ScipamatoFilter, S extends DefinitionProviderService<T, F>, P extends DefinitionProvider<T, F, S>>
    extends BasePage<T> {

    private F     filter;
    private P     provider;
    private Panel resultPanel;

    protected DefinitionListPage(@Nullable final PageParameters parameters) {
        super(parameters);
    }

    @NotNull
    private F getFilter() {
        return filter;
    }

    @NotNull
    protected P getProvider() {
        return provider;
    }

    @NotNull
    protected Panel getResultPanel() {
        return resultPanel;
    }

    @Override
    protected final void onInitialize() {
        super.onInitialize();
        filter = newFilter();
        provider = newProvider(getFilter());
        queue(newFilterPanel("filterPanel"));
        queue(resultPanel = newResultPanel("resultPanel"));
    }

    @NotNull
    protected abstract F newFilter();

    @NotNull
    protected abstract P newProvider(F filter);

    @NotNull
    protected abstract Panel newFilterPanel(final String id);

    @NotNull
    protected abstract Panel newResultPanel(final String id);
}
