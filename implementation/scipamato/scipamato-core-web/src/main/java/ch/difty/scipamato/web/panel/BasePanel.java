package ch.difty.scipamato.web.panel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.ScipamatoSession;
import ch.difty.scipamato.config.core.ApplicationProperties;
import ch.difty.scipamato.entity.User;
import ch.difty.scipamato.web.AbstractPanel;
import ch.difty.scipamato.web.Mode;

public abstract class BasePanel<T> extends AbstractPanel<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationProperties properties;

    public BasePanel(final String id) {
        this(id, null, Mode.VIEW);
    }

    public BasePanel(final String id, IModel<T> model) {
        this(id, model, Mode.VIEW);
    }

    public BasePanel(final String id, IModel<T> model, Mode mode) {
        super(id, model, mode);
    }

    protected ApplicationProperties getProperties() {
        return properties;
    }

    protected String getLocalization() {
        return ScipamatoSession.get().getLocale().getLanguage();
    }

    /**
     * Get the currently active user
     */
    protected User getActiveUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (User) principal;
    }
}
