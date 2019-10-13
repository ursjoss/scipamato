package ch.difty.scipamato.publ.web.common;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.difty.scipamato.common.web.AbstractPanel;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;

@SuppressWarnings("WeakerAccess")
public abstract class BasePanel<T> extends AbstractPanel<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ScipamatoWebSessionFacade sessionFacade;

    public BasePanel(@NotNull final String id) {
        this(id, null, Mode.VIEW);
    }

    public BasePanel(@NotNull final String id, @Nullable IModel<T> model) {
        this(id, model, Mode.VIEW);
    }

    public BasePanel(@NotNull final String id, @Nullable IModel<T> model, @NotNull Mode mode) {
        super(id, model, mode);
    }

    @NotNull
    protected String getLocalization() {
        return sessionFacade.getLanguageCode();
    }
}
