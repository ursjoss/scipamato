package ch.difty.scipamato.core.web.user;

import static ch.difty.scipamato.core.entity.User.UserFields.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeCDNCSSReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import ch.difty.scipamato.common.web.component.SerializableConsumer;
import ch.difty.scipamato.common.web.component.SerializableFunction;
import ch.difty.scipamato.common.web.component.table.column.ClickablePropertyColumn;
import ch.difty.scipamato.core.auth.Roles;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.web.CorePageParameters;
import ch.difty.scipamato.core.web.common.BasePage;

/**
 * Page to list all scipamato users and apply simple filters to limit the results.
 * <p>
 * Offers the option to create a new user.
 *
 * @author u.joss
 */
@MountPath("/users")
@AuthorizeInstantiation({ Roles.ADMIN })
@SuppressWarnings("SameParameterValue")
public class UserListPage extends BasePage<Void> {

    private static final String COLUMN_HEADER = "column.header.";
    private static final int    ROWS_PER_PAGE = 10;

    private UserFilter   filter;
    private UserProvider dataProvider;

    @SuppressWarnings("WeakerAccess")
    public UserListPage(final PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new UserFilter();
        dataProvider = new UserProvider(filter);
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

        queueFieldAndLabel(new TextField<String>(USER_NAME.getName(),
            PropertyModel.of(filter, UserFilter.UserFilterFields.NAME_MASK.getName())));
        queueResponsePageButton("newUser", () -> {
            try {
                final PageParameters pp = new PageParameters();
                pp.set(CorePageParameters.MODE.getName(), UserEditPage.Mode.CREATE);
                return new UserEditPage(pp);
            } catch (Exception ex) {
                error(ex.getMessage());
                return null;
            }
        });
    }

    private void makeAndQueueTable(String id) {
        final DataTable<User, String> results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider,
            ROWS_PER_PAGE);
        results.setOutputMarkupId(true);
        results.add(new TableBehavior()
            .striped()
            .hover());
        queue(results);
    }

    private List<IColumn<User, String>> makeTableColumns() {
        final List<IColumn<User, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn(USER_NAME.getName(), this::onTitleClick));
        columns.add(makePropertyColumn(FIRST_NAME.getName()));
        columns.add(makePropertyColumn(LAST_NAME.getName()));
        columns.add(makePropertyColumn(EMAIL.getName()));
        columns.add(makeBooleanPropertyColumn(ENABLED.getName(), User::isEnabled));
        return columns;
    }

    private ClickablePropertyColumn<User, String> makeClickableColumn(String propExpression,
        SerializableConsumer<IModel<User>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null),
            propExpression, propExpression, consumer);
    }

    private void onTitleClick(final IModel<User> userModel) {
        PageParameters pp = new PageParameters();
        pp.add(CorePageParameters.USER_ID.getName(), userModel
            .getObject()
            .getId());
        pp.add(CorePageParameters.MODE.getName(), UserEditPage.Mode.MANAGE);

        setResponsePage(new UserEditPage(pp));
    }

    private PropertyColumn<User, String> makePropertyColumn(String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression) {
        };
    }

    private PropertyColumn<User, String> makeBooleanPropertyColumn(String propExpression,
        final SerializableFunction<User, Boolean> predicate) {
        final String trueLabel = new StringResourceModel(propExpression + ".true", this, null).getString();
        final String falseLabel = new StringResourceModel(propExpression + ".false", this, null).getString();
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression,
            propExpression) {

            @Override
            public IModel<?> getDataModel(final IModel<User> rowModel) {
                return Model.of(Stream
                    .of(rowModel.getObject())
                    .map(predicate)
                    .findFirst()
                    .orElse(false) ? trueLabel : falseLabel);
            }
        };
    }

}
