package ch.difty.scipamato.publ.web.paper.browse;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import ch.difty.scipamato.common.web.AbstractPanel;
import ch.difty.scipamato.publ.entity.PopulationCode;
import ch.difty.scipamato.publ.entity.StudyDesignCode;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter.PublicPaperFilterFields;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;

public class SimpleFilterPanel extends AbstractPanel<PublicPaperFilter> {

    private static final long serialVersionUID = 1L;

    private static final String CODES_NONE_SELECT_RESOURCE_TAG = "codes.noneSelected";
    private static final String AM_DATA_WIDTH                  = "data-width";

    public SimpleFilterPanel(String id, IModel<PublicPaperFilter> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addTextFieldTo("methodsSearch", PublicPaperFilter.PublicPaperFilterFields.METHODS_MASK);
        addTextFieldTo("authorsSearch", PublicPaperFilter.PublicPaperFilterFields.AUTHOR_MASK);
        addTextFieldTo("pubYearFrom", PublicPaperFilter.PublicPaperFilterFields.PUB_YEAR_FROM);
        addTextFieldTo("pubYearUntil", PublicPaperFilter.PublicPaperFilterFields.PUB_YEAR_UNTIL);
        addCodesComplex("populationCodes", PublicPaperFilter.PublicPaperFilterFields.POPULATION_CODES,
            PopulationCode.values());
        addCodesComplex("studyDesignCodes", PublicPaperFilter.PublicPaperFilterFields.STUDY_DESIGN_CODES,
            StudyDesignCode.values());
    }

    private void addTextFieldTo(String id, PublicPaperFilterFields filterField) {
        TextField<String> field = new TextField<String>(id, PropertyModel.of(getModel(), filterField.getName()));
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));
        field.setLabel(labelModel);
        queue(field);
    }

    private <C extends Enum<C>> void addCodesComplex(String id, PublicPaperFilterFields filterField, C[] values) {
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));

        IModel<Collection<C>> model = PropertyModel.of(getModel(), filterField.getName());
        List<? extends C> choices = Arrays.asList(values);
        final IChoiceRenderer<C> choiceRenderer = new EnumChoiceRenderer<C>(this);
        final StringResourceModel noneSelectedModel = new StringResourceModel(CODES_NONE_SELECT_RESOURCE_TAG, this,
                null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withMultiple(true)
            .withLiveSearch(true)
            .withNoneSelectedText(noneSelectedModel.getObject());
        final BootstrapMultiSelect<C> multiSelect = new BootstrapMultiSelect<>(id, model, choices, choiceRenderer)
            .with(config);
        multiSelect.add(new AttributeModifier(AM_DATA_WIDTH, "fit"));
        queue(multiSelect);
    }

}
