package ch.difty.scipamato.public_.web.common;

import org.apache.wicket.model.IModel;

import ch.difty.scipamato.common.web.AbstractPanel;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.public_.ScipamatoPublicSession;

public abstract class BasePanel<T> extends AbstractPanel<T> {

    private static final long serialVersionUID = 1L;

    public BasePanel(final String id) {
        this(id, null, Mode.VIEW);
    }

    public BasePanel(final String id, IModel<T> model) {
        this(id, model, Mode.VIEW);
    }

    public BasePanel(final String id, IModel<T> model, Mode mode) {
        super(id, model, mode);
    }

    protected String getLocalization() {
        return ScipamatoPublicSession.get()
            .getLocale()
            .getLanguage();
    }

}
