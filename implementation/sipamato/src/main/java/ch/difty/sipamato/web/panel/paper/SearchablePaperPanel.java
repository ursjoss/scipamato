package ch.difty.sipamato.web.panel.paper;

import org.apache.wicket.model.IModel;

import ch.difty.sipamato.entity.filter.SearchCondition;
import ch.difty.sipamato.web.pages.Mode;

public abstract class SearchablePaperPanel extends PaperPanel<SearchCondition> {

    private static final long serialVersionUID = 1L;

    public SearchablePaperPanel(String id) {
        super(id, null, Mode.SEARCH);
    }

    public SearchablePaperPanel(String id, IModel<SearchCondition> model) {
        super(id, model, Mode.SEARCH);
    }

    public SearchablePaperPanel(String id, IModel<SearchCondition> model, Mode ignoredMode) {
        super(id, model, Mode.SEARCH);
    }

}
