package ch.difty.scipamato.core.web.keyword;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.keyword.Keyword;
import ch.difty.scipamato.core.entity.keyword.KeywordDefinition;
import ch.difty.scipamato.core.entity.keyword.KeywordFilter;
import ch.difty.scipamato.core.persistence.KeywordService;
import ch.difty.scipamato.core.web.common.BasePage;

@MountPath("keywords")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
@SuppressWarnings({ "SameParameterValue", "WicketForgeJavaIdInspection", "WeakerAccess" })
public class KeywordListPage extends BasePage<Keyword> {

    private static final int    ROWS_PER_PAGE = 10;
    private static final String COLUMN_HEADER = "column.header.";

    @SpringBean
    private KeywordService            service;
    private KeywordFilter             filter;
    private KeywordDefinitionProvider dataProvider;

    public KeywordListPage(final PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new KeywordFilter();
        dataProvider = new KeywordDefinitionProvider(filter);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("filterForm");
        makeAndQueueTable("results");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<>(id, dataProvider));

        queueFieldAndLabel(new TextField<String>("name",
            PropertyModel.of(filter, KeywordFilter.KeywordFilterFields.NAME_MASK.getName())));
        queueNewTopicButton("newKeyword");

    }

    private void makeAndQueueTable(String id) {
        DataTable<KeywordDefinition, String> results = new BootstrapDefaultDataTable<>(id, makeTableColumns(),
            dataProvider, ROWS_PER_PAGE);
        results.setOutputMarkupId(true);
        results.add(new TableBehavior()
            .striped()
            .hover());
        queue(results);
    }

    private List<IColumn<KeywordDefinition, String>> makeTableColumns() {
        final List<IColumn<KeywordDefinition, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn("translationsAsString", this::onTitleClick));
        columns.add(makePropertyColumn(KeywordDefinition.KeywordFields.SEARCH_OVERRIDE.getName()));
        return columns;
    }

    private PropertyColumn<KeywordDefinition, String> makePropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression);
    }

    private ClickablePropertyColumn<KeywordDefinition, String> makeClickableColumn(String propExpression,
        SerializableConsumer<IModel<KeywordDefinition>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
            propExpression, propExpression, consumer);
    }

    private void onTitleClick(final IModel<KeywordDefinition> keywordModel) {
        setResponsePage(new KeywordEditPage(keywordModel, getPage().getPageReference()));
    }

    private void queueNewTopicButton(final String id) {
        BootstrapAjaxButton newButton = queueResponsePageButton(id,
            () -> new KeywordEditPage(Model.of(service.newUnpersistedKeywordDefinition()),
                getPage().getPageReference()));
        newButton.add(new LoadingBehavior(new StringResourceModel(id + LOADING_RESOURCE_TAG, this, null)));
    }
}