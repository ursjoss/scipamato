package ch.difty.scipamato.public_.web.paper.browse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.public_.ScipamatoPublicSession;
import ch.difty.scipamato.public_.entity.Code;
import ch.difty.scipamato.public_.entity.CodeClass;
import ch.difty.scipamato.public_.entity.PopulationCode;
import ch.difty.scipamato.public_.entity.PublicPaper;
import ch.difty.scipamato.public_.entity.StudyDesignCode;
import ch.difty.scipamato.public_.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.public_.web.common.BasePage;
import ch.difty.scipamato.public_.web.model.CodeClassModel;
import ch.difty.scipamato.public_.web.model.CodeModel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelectConfig;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;

@MountPath("/")
@WicketHomePage
public class PublicPage extends BasePage<Void> {

    private static final long serialVersionUID = 1L;

    private static final int RESULT_PAGE_SIZE = 20;

    private static final String COLUMN_HEADER         = "column.header.";
    private static final String CODES_CLASS_BASE_NAME = "codesOfClass";

    private static final String CODES_NONE_SELECT_RESOURCE_TAG = "codes.noneSelected";

    private static final String AM_DATA_WIDTH = "data-width";

    private PublicPaperFilter   filter;
    private PublicPaperProvider dataProvider;
    private boolean             extendedSearch = false;

    private WebMarkupContainer             extendedSearchContainer;
    private DataTable<PublicPaper, String> results;

    public PublicPage(PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
        extendedSearch = parameters.get("extendedSearch")
            .toBoolean(false);
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
        FilterForm<PublicPaperFilter> filterForm = new FilterForm<PublicPaperFilter>(id, dataProvider) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                updateNavigateable();
            }
        };
        queue(filterForm);

        queueFieldAndLabel(
            new TextField<String>("methodsSearch", PropertyModel.of(filter, PublicPaperFilter.METHODS_MASK)));
        queueFieldAndLabel(
            new TextField<String>("authorsSearch", PropertyModel.of(filter, PublicPaperFilter.AUTHOR_MASK)));
        queueFieldAndLabel(
            new TextField<String>("pubYearFrom", PropertyModel.of(filter, PublicPaperFilter.PUB_YEAR_FROM)));
        queueFieldAndLabel(
            new TextField<String>("pubYearUntil", PropertyModel.of(filter, PublicPaperFilter.PUB_YEAR_UNTIL)));
        queueFieldAndLabel(new TextField<String>("number", PropertyModel.of(filter, PublicPaperFilter.NUMBER)));

        queuePopulationCodesComplex("populationCodes");
        queueStudyDesignCodesComplex("studyDesignCodes");

        queueExtendedSearchButton("toggleExtendedSearch");
        queueQueryButton("query", filterForm);

        queueExtendedSearchContainer("extendedSearchContainer");
    }

    private void queueQueryButton(final String id, final FilterForm<PublicPaperFilter> filterForm) {
        final StringResourceModel labelModel = new StringResourceModel("button." + id + LABEL_RESOURCE_TAG, this, null);
        BootstrapButton queryButton = new BootstrapButton(id, labelModel, Buttons.Type.Primary);
        queue(queryButton);
        filterForm.setDefaultButton(queryButton);
    }

    private void queueExtendedSearchButton(final String id) {
        final String buttonprefix = "button.";
        queue(new BootstrapAjaxButton(id, Buttons.Type.Default) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setLabel();
            }

            private void setLabel() {
                final StringResourceModel srm;
                if (extendedSearch) {
                    srm = new StringResourceModel(buttonprefix + id + ".showSimple" + LABEL_RESOURCE_TAG, this, null);
                } else {
                    srm = new StringResourceModel(buttonprefix + id + ".showExtended" + LABEL_RESOURCE_TAG, this, null);
                }
                setLabel(Model.of(srm.getString()));
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                extendedSearch = !extendedSearch;
                if (!extendedSearch) {
                    clearCodeSelectBoxes();
                }
                target.add(this);
                target.add(extendedSearchContainer);
                target.add(results);
            }

            private void clearCodeSelectBoxes() {
                filter.getCodesOfClass1()
                    .clear();
                filter.getCodesOfClass2()
                    .clear();
                filter.getCodesOfClass3()
                    .clear();
                filter.getCodesOfClass4()
                    .clear();
                filter.getCodesOfClass5()
                    .clear();
                filter.getCodesOfClass6()
                    .clear();
                filter.getCodesOfClass7()
                    .clear();
                filter.getCodesOfClass8()
                    .clear();
            }
        });
    }

    private void queueExtendedSearchContainer(final String id) {
        extendedSearchContainer = new WebMarkupContainer(id) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onConfigure() {
                super.onConfigure();
                setVisible(extendedSearch);
            }
        };
        extendedSearchContainer.setOutputMarkupPlaceholderTag(true);
        queue(extendedSearchContainer);

        CodeClassModel codeClassModel = new CodeClassModel(getLanguageCode());
        List<CodeClass> codeClasses = codeClassModel.getObject();

        makeCodeClassComplex(CodeClassId.CC1, codeClasses);
        makeCodeClassComplex(CodeClassId.CC2, codeClasses);
        makeCodeClassComplex(CodeClassId.CC3, codeClasses);
        makeCodeClassComplex(CodeClassId.CC4, codeClasses);
        makeCodeClassComplex(CodeClassId.CC5, codeClasses);
        makeCodeClassComplex(CodeClassId.CC6, codeClasses);
        makeCodeClassComplex(CodeClassId.CC7, codeClasses);
        makeCodeClassComplex(CodeClassId.CC8, codeClasses);
    }

    private BootstrapMultiSelect<Code> makeCodeClassComplex(final CodeClassId codeClassId,
            final List<CodeClass> codeClasses) {
        final int id = codeClassId.getId();
        final String componentId = CODES_CLASS_BASE_NAME + id;
        final String className = codeClasses.stream()
            .filter(cc -> cc.getCodeClassId() == id)
            .map(CodeClass::getName)
            .findFirst()
            .orElse(codeClassId.name());
        queue(new Label(componentId + LABEL_TAG, Model.of(className)));

        final CodeModel choices = new CodeModel(codeClassId, getLanguageCode());
        final IChoiceRenderer<Code> choiceRenderer = new ChoiceRenderer<>(Code.DISPLAY_VALUE, Code.CODE);
        final StringResourceModel noneSelectedModel = new StringResourceModel(CODES_NONE_SELECT_RESOURCE_TAG, this,
                null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withMultiple(true)
            .withNoneSelectedText(noneSelectedModel.getObject())
            .withLiveSearch(true);

        final PropertyModel<List<Code>> model = PropertyModel.of(filter, componentId);
        final BootstrapMultiSelect<Code> multiSelect = new BootstrapMultiSelect<Code>(componentId, model, choices,
                choiceRenderer).with(config);
        multiSelect.add(new AttributeModifier(AM_DATA_WIDTH, "fit"));
        queue(multiSelect);
        return multiSelect;
    }

    private void makeAndQueueResultTable(String id) {
        results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, dataProvider.getRowsPerPage());
        results.setOutputMarkupId(true);
        results.add(new TableBehavior().striped()
            .hover());
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
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
                propExpression);
    }

    private ClickablePropertyColumn<PublicPaper, String> makeClickableColumn(String propExpression,
            SerializableConsumer<IModel<PublicPaper>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
                propExpression, propExpression, consumer);
    }

    private void onTitleClick(IModel<PublicPaper> m) {
        ScipamatoPublicSession.get()
            .getPaperIdManager()
            .setFocusToItem(m.getObject()
                .getId());
        setResponsePage(new PublicPaperDetailPage(m, getPage().getPageReference()));
    }

    private void queuePopulationCodesComplex(String id) {
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));

        IModel<Collection<PopulationCode>> model = PropertyModel.of(filter, PublicPaperFilter.POPULATION_CODES);
        List<? extends PopulationCode> choices = Arrays.asList(PopulationCode.values());
        final IChoiceRenderer<PopulationCode> choiceRenderer = new EnumChoiceRenderer<>(this);
        final StringResourceModel noneSelectedModel = new StringResourceModel(CODES_NONE_SELECT_RESOURCE_TAG, this,
                null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withMultiple(true)
            .withLiveSearch(true)
            .withNoneSelectedText(noneSelectedModel.getObject());
        final BootstrapMultiSelect<PopulationCode> multiSelect = new BootstrapMultiSelect<>(id, model, choices,
                choiceRenderer).with(config);
        multiSelect.add(new AttributeModifier(AM_DATA_WIDTH, "fit"));
        queue(multiSelect);
    }

    private void queueStudyDesignCodesComplex(String id) {
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));

        IModel<Collection<StudyDesignCode>> model = PropertyModel.of(filter, PublicPaperFilter.STUDY_DESIGN_CODES);
        List<? extends StudyDesignCode> choices = Arrays.asList(StudyDesignCode.values());
        final IChoiceRenderer<StudyDesignCode> choiceRenderer = new EnumChoiceRenderer<>(this);
        final StringResourceModel noneSelectedModel = new StringResourceModel(CODES_NONE_SELECT_RESOURCE_TAG, this,
                null);
        final BootstrapSelectConfig config = new BootstrapSelectConfig().withMultiple(true)
            .withLiveSearch(true)
            .withNoneSelectedText(noneSelectedModel.getObject());
        final BootstrapMultiSelect<StudyDesignCode> multiSelect = new BootstrapMultiSelect<>(id, model, choices,
                choiceRenderer).with(config);
        multiSelect.add(new AttributeModifier(AM_DATA_WIDTH, "fit"));
        queue(multiSelect);
    }

    /**
     * Have the provider provide a list of all paper numbers (business key) matching
     * the current filter. Construct a navigateable with this list and set it into
     * the session
     */
    private void updateNavigateable() {
        ScipamatoPublicSession.get()
            .getPaperIdManager()
            .initialize(dataProvider.findAllPaperNumbersByFilter());
    }

}
