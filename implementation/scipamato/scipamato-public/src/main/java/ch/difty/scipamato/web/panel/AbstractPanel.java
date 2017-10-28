package ch.difty.scipamato.web.panel;

import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.ScipamatoPublicSession;
import ch.difty.scipamato.web.WicketUtils;
import ch.difty.scipamato.web.pages.Mode;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX;

public abstract class AbstractPanel<T> extends GenericPanel<T> {

    private static final long serialVersionUID = 1L;

    protected static final String LABEL_TAG = WicketUtils.LABEL_TAG;
    protected static final String LABEL_RECOURCE_TAG = WicketUtils.LABEL_RECOURCE_TAG;
    protected static final String SHORT_LABEL_RECOURCE_TAG = WicketUtils.SHORT_LABEL_RECOURCE_TAG;

    private final Mode mode;
    private final String submitLinkResourceLabel;

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

    protected String getLocalization() {
        return ScipamatoPublicSession.get().getLocale().getLanguage();
    }

    protected void queueFieldAndLabel(FormComponent<?> field) {
        queueFieldAndLabel(field, null);
    }

    protected void queueFieldAndLabel(FormComponent<?> field, PropertyValidator<?> pv) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        field.setOutputMarkupId(true);
        queue(field);
        if (pv != null && isEditMode()) {
            field.add(pv);
        }
    }

    protected void queueCheckBoxAndLabel(CheckBoxX field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        queue(field);
    }

}
