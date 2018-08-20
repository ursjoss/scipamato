package ch.difty.scipamato.core.web.newsletter.topic;

import java.util.ArrayList;
import java.util.List;

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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopic;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicDefinition;
import ch.difty.scipamato.core.entity.newsletter.NewsletterTopicFilter;
import ch.difty.scipamato.core.persistence.NewsletterTopicService;
import ch.difty.scipamato.core.web.common.BasePage;
import ch.difty.scipamato.core.web.newsletter.NewsletterTopicDefinitionProvider;

@MountPath("newsletter/topics")
@Slf4j
@AuthorizeInstantiation({ Roles.USER, Roles.ADMIN })
public class NewsletterTopicListPage extends BasePage<NewsletterTopic> {

    private static final int    ROWS_PER_PAGE = 10;
    private static final String COLUMN_HEADER = "column.header.";

    @SpringBean
    private NewsletterTopicService                       service;
    private NewsletterTopicFilter                        filter;
    private NewsletterTopicDefinitionProvider            dataProvider;
    private DataTable<NewsletterTopicDefinition, String> results;

    public NewsletterTopicListPage(final PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new NewsletterTopicFilter();
        dataProvider = new NewsletterTopicDefinitionProvider(filter);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("filterForm");
        makeAndQueueTable("results");
    }

    private void makeAndQueueFilterForm(final String id) {
        queue(new FilterForm<>(id, dataProvider));

        queueFieldAndLabel(new TextField<String>("title",
            PropertyModel.of(filter, NewsletterTopicFilter.NewsletterTopicFilterFields.TITLE_MASK.getName())));
        //        newNewsletterTopicButton = queueResponsePageButton("newNewsletterTopic", NewsletterTopicEditPage::new);
    }

    private void makeAndQueueTable(String id) {
        results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, ROWS_PER_PAGE);
        results.setOutputMarkupId(true);
        results.add(new TableBehavior()
            .striped()
            .hover());
        queue(results);
    }

    private List<IColumn<NewsletterTopicDefinition, String>> makeTableColumns() {
        final List<IColumn<NewsletterTopicDefinition, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn("translationsAsString", this::onTitleClick));
        return columns;
    }

    private ClickablePropertyColumn<NewsletterTopicDefinition, String> makeClickableColumn(String propExpression,
        SerializableConsumer<IModel<NewsletterTopicDefinition>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
            propExpression, propExpression, consumer);
    }

    private void onTitleClick(final IModel<NewsletterTopicDefinition> newsletterTopicModel) {
        setResponsePage(new NewsletterTopicEditPage(newsletterTopicModel));
    }
}
