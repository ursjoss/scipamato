package ch.difty.scipamato.publ.web.paper.browse;

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
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import ch.difty.scipamato.common.entity.CodeClassId;
import ch.difty.scipamato.common.entity.FieldEnumType;
import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.publ.entity.Code;
import ch.difty.scipamato.publ.entity.CodeClass;
import ch.difty.scipamato.publ.entity.PopulationCode;
import ch.difty.scipamato.publ.entity.PublicPaper;
import ch.difty.scipamato.publ.entity.StudyDesignCode;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.publ.entity.filter.PublicPaperFilter.PublicPaperFilterFields;
import ch.difty.scipamato.publ.web.common.BasePage;
import ch.difty.scipamato.publ.web.model.CodeClassModel;
import ch.difty.scipamato.publ.web.model.CodeModel;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.tabs.ClientSideBootstrapTabbedPanel;
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
        addTabPanel(filterForm, "tabs");

        // TODO continue

        queueExtendedSearchButton("toggleExtendedSearch");
        queueQueryButton("query", filterForm);

        queueExtendedSearchContainer("extendedSearchContainer");
    }

    private void addTabPanel(FilterForm<PublicPaperFilter> filterForm, String tabId) {
        List<ITab> tabs = new ArrayList<>();
        tabs.add(new AbstractTab(new StringResourceModel("tab1" + LABEL_RESOURCE_TAG, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            public Panel getPanel(String panelId) {
                return new TabPanel1(panelId, Model.of(filter));
            }

        });
        filterForm.add(new ClientSideBootstrapTabbedPanel<ITab>(tabId, tabs));
    }

    private class TabPanel1 extends AbstractTabPanel {
        private static final long serialVersionUID = 1L;

        public TabPanel1(String id, IModel<PublicPaperFilter> model) {
            super(id, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            Form<Object> form = new Form<>("tab1Form");
            queue(form);

            addTextFieldTo(form, "methodsSearch", PublicPaperFilter.PublicPaperFilterFields.METHODS_MASK);
            addTextFieldTo(form, "authorsSearch", PublicPaperFilter.PublicPaperFilterFields.AUTHOR_MASK);
            addTextFieldTo(form, "pubYearFrom", PublicPaperFilter.PublicPaperFilterFields.PUB_YEAR_FROM);
            addTextFieldTo(form, "pubYearUntil", PublicPaperFilter.PublicPaperFilterFields.PUB_YEAR_UNTIL);
            addTextFieldTo(form, "number", PublicPaperFilter.PublicPaperFilterFields.NUMBER);
            queueCodesComplex(form, "populationCodes", PublicPaperFilter.PublicPaperFilterFields.POPULATION_CODES,
                PopulationCode.values());
            queueCodesComplex(form, "studyDesignCodes", PublicPaperFilter.PublicPaperFilterFields.STUDY_DESIGN_CODES,
                StudyDesignCode.values());
            // TODO continue
        }

    }

    private abstract class AbstractTabPanel extends Panel {

        private static final long serialVersionUID = 1L;

        public AbstractTabPanel(String id) {
            super(id);
        }

        public AbstractTabPanel(String id, IModel<?> model) {
            super(id, model);
        }

        protected void addTextFieldTo(Form<Object> form, String id, PublicPaperFilterFields filterField) {
            TextField<String> field = new TextField<String>(id, PropertyModel.of(filter, filterField.getName()));
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
            form.add(new Label(id + LABEL_TAG, labelModel));
            field.setLabel(labelModel);
            form.add(field);
        }

        protected <C extends Enum<C>> void queueCodesComplex(Form<Object> form, String id,
                PublicPaperFilterFields filterField, C[] values) {
            StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
            form.add(new Label(id + LABEL_TAG, labelModel));

            IModel<Collection<C>> model = PropertyModel.of(filter, filterField.getName());
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
            form.add(multiSelect);
        }
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
        final IChoiceRenderer<Code> choiceRenderer = new ChoiceRenderer<>(Code.CodeFields.DISPLAY_VALUE.getName(),
                Code.CodeFields.CODE.getName());
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
        columns.add(makePropertyColumn(PublicPaper.PublicPaperFields.AUTHORS));
        columns.add(makeClickableColumn(PublicPaper.PublicPaperFields.TITLE, this::onTitleClick));
        columns.add(makePropertyColumn(PublicPaper.PublicPaperFields.LOCATION));
        columns.add(makePropertyColumn(PublicPaper.PublicPaperFields.PUBL_YEAR));
        return columns;
    }

    private PropertyColumn<PublicPaper, String> makePropertyColumn(FieldEnumType fieldType) {
        final String propExpression = fieldType.getName();
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
                propExpression);
    }

    private ClickablePropertyColumn<PublicPaper, String> makeClickableColumn(FieldEnumType fieldType,
            SerializableConsumer<IModel<PublicPaper>> consumer) {
        final String propExpression = fieldType.getName();
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
                propExpression, propExpression, consumer);
    }

    private void onTitleClick(IModel<PublicPaper> m) {
        getPaperIdManager().setFocusToItem(m.getObject()
            .getId());
        setResponsePage(new PublicPaperDetailPage(m, getPage().getPageReference()));
    }

    /**
     * Have the provider provide a list of all paper numbers (business key) matching
     * the current filter. Construct a navigateable with this list and set it into
     * the session
     */
    private void updateNavigateable() {
        getPaperIdManager().initialize(dataProvider.findAllPaperNumbersByFilter());
    }

}
