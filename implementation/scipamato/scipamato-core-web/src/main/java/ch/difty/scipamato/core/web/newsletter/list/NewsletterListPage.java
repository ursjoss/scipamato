package ch.difty.scipamato.core.web.newsletter.list;

import static ch.difty.scipamato.core.entity.newsletter.Newsletter.NewsletterFields.*;
import static ch.difty.scipamato.core.entity.newsletter.NewsletterFilter.NewsletterFilterFields;
import static ch.difty.scipamato.core.entity.newsletter.NewsletterFilter.NewsletterFilterFields.ISSUE_MASK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCDNCSSReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
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
import ch.difty.scipamato.common.web.component.table.column.LinkIconColumn;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.newsletter.Newsletter;
import ch.difty.scipamato.core.entity.newsletter.NewsletterFilter;
import ch.difty.scipamato.common.entity.newsletter.PublicationStatus;
import ch.difty.scipamato.core.persistence.NewsletterService;
import ch.difty.scipamato.core.web.common.BasePage;
import ch.difty.scipamato.core.web.newsletter.NewsletterProvider;
import ch.difty.scipamato.core.web.newsletter.edit.NewsletterEditPage;

/**
 * Page to list all newsletters and apply simple filters to limit the results.
 * <p>
 * Offers the option to create a new newsletter.
 *
 * @author u.joss
 */
@SuppressWarnings({ "SameParameterValue", "WeakerAccess" })
@MountPath("/newsletters")
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class NewsletterListPage extends BasePage<Void> {

    private static final String COLUMN_HEADER = "column.header.";
    private static final int    ROWS_PER_PAGE = 10;

    @SpringBean
    private NewsletterService             service;
    private NewsletterFilter              filter;
    private NewsletterProvider            dataProvider;
    private DataTable<Newsletter, String> results;
    private BootstrapAjaxButton           newNewsletterButton;

    public NewsletterListPage(final PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new NewsletterFilter();
        dataProvider = new NewsletterProvider(filter);
    }

    @Override
    public void renderHead(final IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(FontAwesomeCDNCSSReference.instance()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("filterForm");
        makeAndQueueTable("results");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<>(id, dataProvider));

        queueFieldAndLabel(new TextField<String>(ISSUE.getName(), PropertyModel.of(filter, ISSUE_MASK.getName())));
        queueBootstrapSelectAndLabel(PUBLICATION_STATUS.getName());
        queueNewButton("newNewsletter");

    }

    private void queueBootstrapSelectAndLabel(final String id) {
        StringResourceModel labelModel = new StringResourceModel(id + LABEL_RESOURCE_TAG, this, null);
        queue(new Label(id + LABEL_TAG, labelModel));

        final PropertyModel<PublicationStatus> selectionModel = PropertyModel.of(filter,
            NewsletterFilterFields.PUBLICATION_STATUS.getName());
        final IModel<List<PublicationStatus>> choicesModel = Model.ofList(Arrays.asList(PublicationStatus.values()));
        BootstrapSelect<PublicationStatus> select = new BootstrapSelect<>(id, selectionModel, choicesModel,
            new EnumChoiceRenderer<>(this));
        select.setNullValid(true);
        select.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                target.add(results);
            }
        });
        queue(select);
    }

    @Override
    protected boolean setResponsePageButtonEnabled() {
        return service.canCreateNewsletterInProgress();
    }

    private void makeAndQueueTable(String id) {
        results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, ROWS_PER_PAGE);
        results.setOutputMarkupId(true);
        results.add(new TableBehavior()
            .striped()
            .hover());
        queue(results);
    }

    private List<IColumn<Newsletter, String>> makeTableColumns() {
        final List<IColumn<Newsletter, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn(ISSUE.getName(), this::onTitleClick));
        columns.add(makePropertyColumn(ISSUE_DATE.getName()));
        columns.add(makeEnumPropertyColumn(PUBLICATION_STATUS.getName()));
        columns.add(makeSortTopicLinkColumn("sortTopics"));
        columns.add(makeRemoveLinkColumn("remove"));
        return columns;
    }

    private ClickablePropertyColumn<Newsletter, String> makeClickableColumn(String propExpression,
        SerializableConsumer<IModel<Newsletter>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
            propExpression, propExpression, consumer);
    }

    private void onTitleClick(final IModel<Newsletter> newsletterModel) {
        setResponsePage(new NewsletterEditPage(newsletterModel));
    }

    private PropertyColumn<Newsletter, String> makePropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression);
    }

    /**
     * provides the localized values for the publication status as defined in the properties files.
     */
    private PropertyColumn<Newsletter, String> makeEnumPropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression) {
            @Override
            public IModel<String> getDataModel(final IModel<Newsletter> rowModel) {
                IModel dataModel = super.getDataModel(rowModel);
                PublicationStatus ps = (PublicationStatus) dataModel.getObject();
                return new StringResourceModel("PublicationStatus." + ps.name(), NewsletterListPage.this, null);
            }
        };
    }

    private IColumn<Newsletter, String> makeSortTopicLinkColumn(String id) {
        return new LinkIconColumn<>(new StringResourceModel(COLUMN_HEADER + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(final IModel<Newsletter> rowModel) {
                return Model.of("fa fa-fw fa-random");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<Newsletter> rowModel) {
                return new StringResourceModel("column.title." + id, NewsletterListPage.this, rowModel);
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<Newsletter> rowModel,
                AjaxLink<Void> link) {
                setResponsePage(new NewsletterTopicSortPage(rowModel, getPageReference()));
            }
        };
    }

    private IColumn<Newsletter, String> makeRemoveLinkColumn(String id) {
        return new LinkIconColumn<>(new StringResourceModel(COLUMN_HEADER + id, this, null)) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<String> createIconModel(final IModel<Newsletter> rowModel) {
                final boolean canDelete = rowModel
                    .getObject()
                    .isDeletable();
                return Model.of(canDelete ? "fa fa-fw fa-trash" : "");
            }

            @Override
            protected IModel<String> createTitleModel(IModel<Newsletter> rowModel) {
                return new StringResourceModel("column.title." + id, NewsletterListPage.this, rowModel);
            }

            @Override
            protected void onClickPerformed(AjaxRequestTarget target, IModel<Newsletter> rowModel,
                AjaxLink<Void> link) {
                final Newsletter nl = rowModel.getObject();
                if (nl.isDeletable()) {
                    service.remove(nl);
                    info(new StringResourceModel("newsletter.deleted.success", NewsletterListPage.this,
                        rowModel).getString());
                    target.add(results);
                    target.add(newNewsletterButton);
                    target.add(getFeedbackPanel());
                }
            }

        };
    }

    private void queueNewButton(final String id) {
        newNewsletterButton = newResponsePageButton(id, NewsletterEditPage::new);
        newNewsletterButton.add(new LoadingBehavior(new StringResourceModel(id + LOADING_RESOURCE_TAG, this, null)));
        queue(newNewsletterButton);
    }
}
