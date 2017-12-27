package ch.difty.scipamato.core.web.panel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.config.ApplicationProperties;
import ch.difty.scipamato.common.web.AbstractPanel;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.core.ScipamatoSession;
import ch.difty.scipamato.core.entity.User;

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
        return ScipamatoSession.get()
            .getLocale()
            .getLanguage();
    }

    /**
     * Get the currently active user
     */
    protected User getActiveUser() {
        Object principal = SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        return (User) principal;
    }

    /**
     * Retrieves the labels resource string.
     *
     * @param componentId
     *            the wicket id of the component the label is assigned to.
     * @return the label string, taken from the resource.
     */
    protected String getLabelResourceFor(final String componentId) {
        return getResourceFor(componentId, LABEL_RESOURCE_TAG);
    }

    /**
     * Retrieves the labels resource string (short form).
     *
     * @param componentId
     *            the wicket id of the component the label is assigned to.
     * @return the label string, taken from the resource.
     */
    protected String getShortLabelResourceFor(final String componentId) {
        return getResourceFor(componentId, LABEL_RESOURCE_TAG);
    }

    private String getResourceFor(final String componentId, final String tag) {
        return new StringResourceModel(componentId + tag, this, null).getString();
    }

}
