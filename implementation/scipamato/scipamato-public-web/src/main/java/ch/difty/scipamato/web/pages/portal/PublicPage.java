package ch.difty.scipamato.web.pages.portal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.scipamato.ScipamatoPublicSession;
import ch.difty.scipamato.entity.PopulationCode;
import ch.difty.scipamato.entity.PublicPaper;
import ch.difty.scipamato.entity.StudyDesignCode;
import ch.difty.scipamato.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.web.component.SerializableConsumer;
import ch.difty.scipamato.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.web.pages.BasePage;
import ch.difty.scipamato.web.provider.PublicPaperProvider;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

@MountPath("/")
@WicketHomePage
public class PublicPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER = "column.header.";

    private static final int RESULT_PAGE_SIZE = 20;

    private PublicPaperFilter filter;
    private PublicPaperProvider dataProvider;

    public PublicPage(PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new PublicPaperFilter();
        dataProvider = new PublicPaperProvider(filter, RESULT_PAGE_SIZE);
        updateNavigateable();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("searchForm");
        makeAndQueueResultTable("results");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<PublicPaperFilter>(id, dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                updateNavigateable();
            }
        });

        queueFieldAndLabel(new TextField<String>("methodsSearch", PropertyModel.of(filter, PublicPaperFilter.METHODS_MASK)));
        queueFieldAndLabel(new TextField<String>("authorsSearch", PropertyModel.of(filter, PublicPaperFilter.AUTHOR_MASK)));
        queueFieldAndLabel(new TextField<String>("pubYearFrom", PropertyModel.of(filter, PublicPaperFilter.PUB_YEAR_FROM)));
        queueFieldAndLabel(new TextField<String>("pubYearUntil", PropertyModel.of(filter, PublicPaperFilter.PUB_YEAR_UNTIL)));
        queueFieldAndLabel(new TextField<String>("number", PropertyModel.of(filter, PublicPaperFilter.NUMBER)));

        makePopulationCodesComplex("populationCodes");
        makeStudyDesignCodesComplex("studyDesignCodes");
    }

    private void makeAndQueueResultTable(String id) {
        DataTable<PublicPaper, String> results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, dataProvider.getRowsPerPage());
        results.setOutputMarkupId(true);
        results.add(new TableBehavior().striped().hover());
        queue(results);
    }

    private List<IColumn<PublicPaper, String>> makeTableColumns() {
        final List<IColumn<PublicPaper, String>> columns = new ArrayList<>();
        columns.add(makePropertyColumn(PublicPaper.AUTHORS));
        columns.add(makeClickableColumn(PublicPaper.TITLE, this::onTitleClick));
        columns.add(makePropertyColumn(PublicPaper.LOCATION));
        columns.add(makePropertyColumn(PublicPaper.PUBL_YEAR));
        return columns;
    }

    private PropertyColumn<PublicPaper, String> makePropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, propExpression);
    }

    private ClickablePropertyColumn<PublicPaper, String> makeClickableColumn(String propExpression, SerializableConsumer<IModel<PublicPaper>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, propExpression, consumer);
    }

    private void onTitleClick(IModel<PublicPaper> m) {
        ScipamatoPublicSession.get().getPaperIdManager().setFocusToItem(m.getObject().getId());
        setResponsePage(new PublicPaperDetailPage(m, getPage().getPageReference()));
    }

    private void makePopulationCodesComplex(String id) {
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));

        IModel<Collection<PopulationCode>> model = PropertyModel.of(filter, PublicPaperFilter.POPULATION_CODES);
        List<? extends PopulationCode> choices = Arrays.asList(PopulationCode.values());
        final IChoiceRenderer<PopulationCode> choiceRenderer = new EnumChoiceRenderer<>(this);
        final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withMultiple(true).withLiveSearch(true).withNoneSelectedText(noneSelectedModel.getObject());
        final BootstrapMultiSelect<PopulationCode> multiSelect = new BootstrapMultiSelect<>(id, model, choices, choiceRenderer).with(config);
        multiSelect.add(new AttributeModifier("data-width", "fit"));
        queue(multiSelect);
    }

    private void makeStudyDesignCodesComplex(String id) {
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));

        IModel<Collection<StudyDesignCode>> model = PropertyModel.of(filter, PublicPaperFilter.STUDY_DESIGN_CODES);
        List<? extends StudyDesignCode> choices = Arrays.asList(StudyDesignCode.values());
        final IChoiceRenderer<StudyDesignCode> choiceRenderer = new EnumChoiceRenderer<>(this);
        final StringResourceModel noneSelectedModel = new StringResourceModel(id + ".noneSelected", this, null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withMultiple(true).withLiveSearch(true).withNoneSelectedText(noneSelectedModel.getObject());
        final BootstrapMultiSelect<StudyDesignCode> multiSelect = new BootstrapMultiSelect<>(id, model, choices, choiceRenderer).with(config);
        multiSelect.add(new AttributeModifier("data-width", "fit"));
        queue(multiSelect);
    }

    /**
     * Have the provider provide a list of all paper numbers (business key) matching the current filter.
     * Construct a navigateable with this list and set it into the session
     */
    private void updateNavigateable() {
        ScipamatoPublicSession.get().getPaperIdManager().initialize(dataProvider.findAllPaperNumbersByFilter());
    }

}
