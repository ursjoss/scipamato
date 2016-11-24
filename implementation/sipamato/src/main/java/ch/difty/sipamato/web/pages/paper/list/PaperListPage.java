package ch.difty.sipamato.web.pages.paper.list;

import java.util.Optional;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.SimplePaperFilter;
import ch.difty.sipamato.entity.projection.PaperSlim;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.entry.PaperEntryPage;
import ch.difty.sipamato.web.pages.paper.provider.SimpleSortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.result.ResultPanel;

@MountPath("list")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperListPage extends BasePage<PaperSlim> {

    private static final long serialVersionUID = 1L;

    private SimplePaperFilter filter;

    public PaperListPage(PageParameters parameters) {
        super(parameters);
        initDefaultModel();
    }

    private void initDefaultModel() {
        filter = new SimplePaperFilter();
    }

    protected void onInitialize() {
        super.onInitialize();

        final SimpleSortablePaperSlimProvider dataProvider = new SimpleSortablePaperSlimProvider(filter);

        queueFilterForm("searchForm", dataProvider);
        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(filter, SimplePaperFilter.AUTHOR_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(filter, SimplePaperFilter.METHODS_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("fieldSearch", PropertyModel.of(filter, SimplePaperFilter.SEARCH_MASK)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(filter, SimplePaperFilter.PUB_YEAR_FROM)), Optional.empty());
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(filter, SimplePaperFilter.PUB_YEAR_UNTIL)), Optional.empty());
        queueResponsePageButton("newPaper", new PaperEntryPage(getPageParameters()));

        queue(new ResultPanel("resultPanel", dataProvider));

    }

    private void queueFilterForm(final String id, final SimpleSortablePaperSlimProvider dataProvider) {
        queue(new FilterForm<SimplePaperFilter>(id, dataProvider));
    }
}
