package ch.difty.scipamato.publ.web.paper.browse;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
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

/**
 * The SimpleFilterPanel is added to the PublicPage twice and should be kept
 * synchronized. The fields are therefore change aware, i.e. send an event upon
 * changes that let's the other fields instances
 *
 * @author Urs Joss
 */
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
        TextField<String> field = new TextField<String>(id, PropertyModel.of(getModel(), filterField.getName())) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(IEvent<?> event) {
                if (event.getPayload()
                    .getClass() == SimpleFilterPanelChangeEvent.class) {
                    ((SimpleFilterPanelChangeEvent) event.getPayload()).considerAddingToTarget(this);
                    event.dontBroadcastDeeper();
                }
            }
        };
        field.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                final String id = field.getId();
                final String markupId = field.getMarkupId();
                send(getPage(), Broadcast.BREADTH, new SimpleFilterPanelChangeEvent(target).withId(id)
                    .withMarkupId(markupId));
            }
        });
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
        final BootstrapMultiSelect<C> multiSelect = new BootstrapMultiSelect<C>(id, model, choices, choiceRenderer) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onEvent(IEvent<?> event) {
                if (event.getPayload()
                    .getClass() == SimpleFilterPanelChangeEvent.class) {
                    ((SimpleFilterPanelChangeEvent) event.getPayload()).considerAddingToTarget(this);
                    event.dontBroadcastDeeper();
                }
            }
        };
        multiSelect.with(config);
        multiSelect.add(new AjaxFormComponentUpdatingBehavior("change") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                final String id = multiSelect.getId();
                final String markupId = multiSelect.getMarkupId();
                send(getPage(), Broadcast.BREADTH, new SimpleFilterPanelChangeEvent(target).withId(id)
                    .withMarkupId(markupId));
            }
        });
        multiSelect.add(new AttributeModifier(AM_DATA_WIDTH, "fit"));
        queue(multiSelect);
    }

}
