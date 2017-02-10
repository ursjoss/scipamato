package ch.difty.sipamato.web.panel.paper;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.web.jasper.summary.PaperSummaryDataSource;
import ch.difty.sipamato.web.pages.Mode;

/**
 * The {@link SearchablePaperPanel} does not implement specific behavior and therefore
 * simply implements the abstract methods of the {@link PaperPanel} as no-ops.
 *
 * @author u.joss
 */
public abstract class SearchablePaperPanel extends PaperPanel<SearchCondition> {

    private static final long serialVersionUID = 1L;

    public SearchablePaperPanel(String id, IModel<SearchCondition> model) {
        super(id, model, Mode.SEARCH);
    }

    @Override
    protected PaperSummaryDataSource getSummaryDataSource() {
        return null;
    }

    @Override
    protected void reflectPersistedChangesViaTimer(TextField<Integer> id, TextField<String> created, TextField<String> modified) {
        // don't add the behavior
    }

}
