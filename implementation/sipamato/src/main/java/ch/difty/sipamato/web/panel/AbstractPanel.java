package ch.difty.sipamato.web.panel;

import java.util.Optional;

import org.apache.wicket.bean.validation.PropertyValidator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ch.difty.sipamato.entity.SipamatoEntity;
import ch.difty.sipamato.service.Localization;

public abstract class AbstractPanel<T extends SipamatoEntity> extends GenericPanel<T> {

    private static final long serialVersionUID = 1L;

    // TODO duplicated from BasePage
    protected static final String LABEL_RECOURCE_TAG = ".label";
    protected static final String LABEL_TAG = "Label";

    @SpringBean
    private Localization localization;

    protected Localization getLocalization() {
        return localization;
    }

    public AbstractPanel(final String id) {
        super(id);
    }

    public AbstractPanel(final String id, IModel<T> model) {
        super(id, model);
    }

    // TODO duplicated from BasePage
    protected void queueFieldAndLabel(FormComponent<?> field, Optional<PropertyValidator<?>> pv) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        queue(field);
        if (pv.isPresent()) {
            field.add(pv.get());
        }
    }

    protected void queueFieldAndLabel(FormComponent<?> field, PropertyValidator<?> pv) {
        queueFieldAndLabel(field, Optional.ofNullable(pv));
    }

    protected void queueCheckBoxAndLabel(CheckBox field) {
        String id = field.getId();
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RECOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        queue(field);
    }

}
