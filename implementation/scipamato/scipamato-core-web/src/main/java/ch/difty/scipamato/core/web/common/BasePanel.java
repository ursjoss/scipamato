package ch.difty.scipamato.core.web.common;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.AbstractPanel;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.entity.User;

public abstract class BasePanel<T> extends AbstractPanel<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationCoreProperties properties;
    @SpringBean
    private ScipamatoWebSessionFacade webSessionFacade;

    protected BasePanel(final String id) {
        this(id, null, Mode.VIEW);
    }

    protected BasePanel(final String id, IModel<T> model) {
        this(id, model, Mode.VIEW);
    }

    protected BasePanel(final String id, IModel<T> model, Mode mode) {
        super(id, model, mode);
    }

    protected ApplicationCoreProperties getProperties() {
        return properties;
    }

    protected ItemNavigator<Long> getPaperIdManager() {
        return webSessionFacade.getPaperIdManager();
    }

    protected String getLocalization() {
        return webSessionFacade.getLanguageCode();
    }

    /**
     * @return the currently active user
     */
    protected User getActiveUser() {
        Object principal = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
        return (User) principal;
    }

    /**
     * Retrieves the labels resource string.
     *
     * @param componentId
     *     the wicket id of the component the label is assigned to.
     * @return the label string, taken from the resource.
     */
    protected String getLabelResourceFor(final String componentId) {
        return getResourceFor(componentId);
    }

    /**
     * Retrieves the labels resource string (short form).
     *
     * @param componentId
     *     the wicket id of the component the label is assigned to.
     * @return the label string, taken from the resource.
     */
    protected String getShortLabelResourceFor(final String componentId) {
        return getResourceFor(componentId);
    }

    private String getResourceFor(final String componentId) {
        return new StringResourceModel(componentId + AbstractPanel.LABEL_RESOURCE_TAG, this, null).getString();
    }

}
