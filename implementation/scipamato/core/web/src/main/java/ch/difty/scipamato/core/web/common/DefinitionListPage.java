package ch.difty.scipamato.core.web.common;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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

    protected DefinitionListPage(final PageParameters parameters) {
        super(parameters);
    }

    protected F getFilter() {
        return filter;
    }

    protected P getProvider() {
        return provider;
    }

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

    protected abstract F newFilter();

    protected abstract P newProvider(F filter);

    protected abstract Panel newFilterPanel(final String id);

    protected abstract Panel newResultPanel(final String id);

}
