package ch.difty.sipamato.web.panel.paper;

import org.apache.wicket.model.IModel;

import ch.difty.sipamato.entity.ComplexPaperFilter;

public abstract class SearchablePaperPanel extends PaperPanel<ComplexPaperFilter> {

    private static final long serialVersionUID = 1L;

    public SearchablePaperPanel(String id) {
        super(id, null, Mode.SEARCH);
    }

    public SearchablePaperPanel(String id, IModel<ComplexPaperFilter> model) {
        super(id, model, Mode.SEARCH);
    }

    public SearchablePaperPanel(String id, IModel<ComplexPaperFilter> model, Mode ignoredMode) {
        super(id, model, Mode.SEARCH);
    }

}
