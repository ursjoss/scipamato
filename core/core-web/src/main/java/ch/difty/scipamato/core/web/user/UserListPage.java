package ch.difty.scipamato.core.web.user;

import static ch.difty.scipamato.core.entity.User.UserFields.*;

import java.util.ArrayList;
import java.util.List;

import de.agilecoders.wicket.core.markup.html.bootstrap.table.TableBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private static final String COLUMN_HEADER = "column.header.";
    private static final int    ROWS_PER_PAGE = 10;

    private UserFilter   filter;
    private UserProvider dataProvider;

    public UserListPage(@Nullable final PageParameters parameters) {
        super(parameters);
        initFilterAndProvider();
    }

    private void initFilterAndProvider() {
        filter = new UserFilter();
        dataProvider = new UserProvider(filter);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        makeAndQueueFilterForm("filterForm");
        makeAndQueueTable("results");
    }

    private void makeAndQueueFilterForm(@NotNull final String id) {
        queue(new FilterForm<>(id, dataProvider));

        queueFieldAndLabel(new TextField<>(USER_NAME.getFieldName(), PropertyModel.of(filter, UserFilter.UserFilterFields.NAME_MASK.getFieldName())));
        queueNewButton("newUser");
    }

    private void queueNewButton(@NotNull final String id) {
        queue(newResponsePageButton(id, () -> {
            final PageParameters pp = new PageParameters();
            pp.set(CorePageParameters.MODE.getName(), UserEditPage.Mode.CREATE);
            return new UserEditPage(pp);
        }));
    }

    private void makeAndQueueTable(@NotNull final String id) {
        final DataTable<User, String> results = new BootstrapDefaultDataTable<>(id, makeTableColumns(), dataProvider, ROWS_PER_PAGE);
        results.setOutputMarkupId(true);
        results.add(new TableBehavior()
            .striped()
            .hover());
        queue(results);
    }

    @NotNull
    private List<IColumn<User, String>> makeTableColumns() {
        final List<IColumn<User, String>> columns = new ArrayList<>();
        columns.add(makeClickableColumn(USER_NAME.getFieldName(), this::onTitleClick));
        columns.add(makePropertyColumn(FIRST_NAME.getFieldName()));
        columns.add(makePropertyColumn(LAST_NAME.getFieldName()));
        columns.add(makePropertyColumn(EMAIL.getFieldName()));
        columns.add(makeBooleanPropertyColumn(ENABLED.getFieldName(), User::isEnabled));
        return columns;
    }

    @NotNull
    private ClickablePropertyColumn<User, String> makeClickableColumn(@NotNull final String propExpression,
        @NotNull final SerializableConsumer<IModel<User>> consumer) {
        return new ClickablePropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, consumer,
            propExpression);
    }

    private void onTitleClick(@NotNull final IModel<User> userModel) {
        final PageParameters pp = new PageParameters();
        pp.add(CorePageParameters.USER_ID.getName(), userModel
            .getObject()
            .getId());
        pp.add(CorePageParameters.MODE.getName(), UserEditPage.Mode.MANAGE);

        setResponsePage(new UserEditPage(pp));
    }

    @NotNull
    private PropertyColumn<User, String> makePropertyColumn(@NotNull final String propExpression) {
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, propExpression) {
            @java.io.Serial
            private static final long serialVersionUID = -6075124056081316865L;
        };
    }

    @NotNull
    private PropertyColumn<User, String> makeBooleanPropertyColumn(@NotNull final String propExpression,
        @NotNull final SerializableFunction<User, Boolean> predicate) {
        final String trueLabel = new StringResourceModel(propExpression + ".true", this, null).getString();
        final String falseLabel = new StringResourceModel(propExpression + ".false", this, null).getString();
        return new PropertyColumn<>(new StringResourceModel(COLUMN_HEADER + propExpression, this, null), propExpression, propExpression) {
            @java.io.Serial
            private static final long serialVersionUID = 406991980303131840L;

            @Override
            public IModel<?> getDataModel(@NotNull final IModel<User> rowModel) {
                return Model.of(Boolean.TRUE.equals(predicate.apply(rowModel.getObject())) ? trueLabel : falseLabel);
            }
        };
    }
}
