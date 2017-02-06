package ch.difty.sipamato.web.panel;

import java.util.Optional;

import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.core.context.SecurityContextHolder;

import ch.difty.sipamato.config.ApplicationProperties;
import ch.difty.sipamato.entity.User;
import ch.difty.sipamato.service.Localization;
import ch.difty.sipamato.web.pages.Mode;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;

public abstract class AbstractPanel<T> extends GenericPanel<T> {

    private static final long serialVersionUID = 1L;

    // TODO duplicated from BasePage
    protected static final String LABEL_RECOURCE_TAG = ".label";
    protected static final String SHORT_LABEL_RECOURCE_TAG = ".short.label";
    protected static final String LABEL_TAG = "Label";

    private final Mode mode;
    private final String submitLinkResourceLabel;

    @SpringBean
    private Localization localization;

    @SpringBean
    private ApplicationProperties properties;

    protected ApplicationProperties getProperties() {
        return properties;
    }

    protected String getSubmitLinkResourceLabel() {
        return submitLinkResourceLabel;
    }

    protected Mode getMode() {
        return mode;
    }

    protected boolean isSearchMode() {
        return mode == Mode.SEARCH;
    }

    protected boolean isEditMode() {
        return mode == Mode.EDIT;
    }

    protected boolean isViewMode() {
        return mode == Mode.VIEW;
    }

    protected Localization getLocalization() {
        return localization;
    }

    public AbstractPanel(final String id) {
        this(id, null, Mode.VIEW);
    }

    public AbstractPanel(final String id, IModel<T> model) {
        this(id, model, Mode.VIEW);
    }

    public AbstractPanel(final String id, IModel<T> model, Mode mode) {
        super(id, model);
        this.mode = mode;
        switch (mode) {
        case EDIT:
            this.submitLinkResourceLabel = "button.save.label";
            break;
        case SEARCH:
            this.submitLinkResourceLabel = "button.search.label";
            break;
        case VIEW:
        default:
            this.submitLinkResourceLabel = "button.disabled.label";
        }
    }

    // TODO duplicated from BasePage
    protected void queueFieldAndLabel(FormComponent<?> field, Optional<PropertyValidator<?>> pv) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        queue(field);
        if (pv.isPresent() && isEditMode()) {
            field.add(pv.get());
        }
    }

    protected void queueFieldAndLabel(FormComponent<?> field, PropertyValidator<?> pv) {
        queueFieldAndLabel(field, Optional.ofNullable(pv));
    }

    protected void queueCheckBoxAndLabel(CheckBoxX field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        queue(field);
    }

    /**
     * Get the currently active user
     */
    protected User getActiveUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (User) principal;
    }
}
