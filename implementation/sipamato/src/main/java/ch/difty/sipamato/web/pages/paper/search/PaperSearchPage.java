package ch.difty.sipamato.web.pages.paper.search;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.sipamato.entity.CompositeComplexPaperFilter;
import ch.difty.sipamato.web.pages.BasePage;
import ch.difty.sipamato.web.pages.paper.provider.ComplexSortablePaperSlimProvider;
import ch.difty.sipamato.web.panel.result.ResultPanel;

/**
 * The PaperSearchPage allows to add and combine individual search terms by instantiating the {@link PaperSearchCriteriaPage}
 * with the currently combined {@link CompositeComplexPaperFilter}. That page will instantiate a new instance of this page
 * after adding the new search term.
 *
 * @author u.joss
 */
@MountPath("search")
@AuthorizeInstantiation({ "ROLE_USER" })
public class PaperSearchPage extends BasePage<CompositeComplexPaperFilter> {

    private static final long serialVersionUID = 1L;

    public PaperSearchPage(PageParameters parameters) {
        super(parameters);
        setDefaultModel(Model.of(new CompositeComplexPaperFilter(null)));
    }

    public PaperSearchPage(IModel<CompositeComplexPaperFilter> filterModel) {
        super(filterModel);
    }

    protected void onInitialize() {
        super.onInitialize();

        final ComplexSortablePaperSlimProvider dataProvider = new ComplexSortablePaperSlimProvider(getModelObject());
        queue(new FilterForm<>("form", dataProvider));
        queue(new ResultPanel("resultPanel", dataProvider));
        queueResponsePageButton("addSearch", new PaperSearchCriteriaPage(getModelObject()));
    }

}
