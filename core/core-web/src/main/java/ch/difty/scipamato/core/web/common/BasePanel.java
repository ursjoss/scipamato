package ch.difty.scipamato.core.web.common;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.scipamato.common.navigator.ItemNavigator;
import ch.difty.scipamato.common.web.AbstractPanel;
import ch.difty.scipamato.common.web.Mode;
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade;
import ch.difty.scipamato.core.config.ApplicationCoreProperties;
import ch.difty.scipamato.core.entity.User;

@SuppressWarnings("unused")
public abstract class BasePanel<T> extends AbstractPanel<T> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ApplicationCoreProperties properties;
    @SpringBean
    private ScipamatoWebSessionFacade webSessionFacade;

    protected BasePanel(@NotNull final String id) {
        this(id, null, Mode.VIEW);
    }

    @SuppressWarnings("unused")
    BasePanel(@NotNull final String id, @Nullable IModel<T> model) {
        this(id, model, Mode.VIEW);
    }

    protected BasePanel(@NotNull final String id, @Nullable IModel<T> model, @NotNull Mode mode) {
        super(id, model, mode);
    }

    @NotNull
    protected ApplicationCoreProperties getProperties() {
        return properties;
    }

    @NotNull
    protected ItemNavigator<Long> getPaperIdManager() {
        return webSessionFacade.getPaperIdManager();
    }

    @NotNull
    protected String getLocalization() {
        return webSessionFacade.getLanguageCode();
    }

    /**
     * @return the currently active user
     */
    @NotNull
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
    @NotNull
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
    @NotNull
    protected String getShortLabelResourceFor(@NotNull final String componentId) {
        return getResourceFor(componentId);
    }

    private String getResourceFor(final String componentId) {
        return new StringResourceModel(componentId + AbstractPanel.LABEL_RESOURCE_TAG, this, null).getString();
    }
}
