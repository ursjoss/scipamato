package ch.difty.scipamato.core.web.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SameParameterValue")
public class UserListPageTest extends BasePageTest<UserListPage> {

    private User user = new User(1, "user", "first", "last", "foo@bar.baz", "pw", true, Set.of(Role.ADMIN, Role.USER));

    private final List<User> results = new ArrayList<>();

    @MockBean
    protected UserService userServiceMock;

    @Override
    protected String getUserName() {
        return "testadmin";
    }

    @Override
    protected void setUpHook() {
        results.add(user);
        when(userServiceMock.countByFilter(isA(UserFilter.class))).thenReturn(results.size());
        when(userServiceMock.findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class))).thenReturn(results);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
    }

    @Override
    protected UserListPage makePage() {
        return new UserListPage(null);
    }

    @Override
    protected Class<UserListPage> getPageClass() {
        return UserListPage.class;
    }

    @Override
    protected void assertSpecificComponents() {
        assertFilterForm("filterForm");

        final String[] headers = { "User Name", "First Name", "Last Name", "Email" };
        final String[] values = { "user", "first", "last", "foo@bar.baz" };
        assertResultTable("results", headers, values);

        verify(userServiceMock).countByFilter(isA(UserFilter.class));
        verify(userServiceMock).findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class));
    }

    private void assertFilterForm(final String b) {
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "userName");
        getTester().assertComponent(b + ":newUser", BootstrapAjaxButton.class);
    }

    private void assertResultTable(final String b, final String[] labels, final String[] values) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        assertHeaderColumns(b, labels);
        assertTableValuesOfRow(b, 1, 1, values);
    }

    private void assertHeaderColumns(final String b, final String[] labels) {
        int idx = 0;
        for (final String label : labels)
            getTester().assertLabel(
                b + ":topToolbars:toolbars:2:headers:" + ++idx + ":header:orderByLink:header_body:label", label);
    }

    private void assertTableValuesOfRow(final String b, final int rowIdx, final Integer colIdxAsLink,
        final String[] values) {
        if (colIdxAsLink != null)
            getTester().assertComponent(b + ":body:rows:" + rowIdx + ":cells:" + colIdxAsLink + ":cell:link",
                Link.class);
        int colIdx = 1;
        for (final String value : values)
            getTester().assertLabel(b + ":body:rows:" + rowIdx + ":cells:" + colIdx + ":cell" + (
                colIdxAsLink != null && colIdx++ == colIdxAsLink ? ":link:label" : ""), value);
    }

    @Test
    public void clickingOnUserName_forwardsToUserEntryPage_withModelLoaded() {
        when(userServiceMock.findById(1)).thenReturn(Optional.of(user));
        getTester().startPage(getPageClass());

        getTester().clickLink("results:body:rows:1:cells:1:cell:link");
        getTester().assertRenderedPage(UserEditPage.class);

        // verify the user was loaded in the target page
        FormTester formTester = getTester().newFormTester("form");
        assertThat(formTester.getTextComponentValue("userName")).isEqualTo("user");

        verify(userServiceMock).countByFilter(isA(UserFilter.class));
        verify(userServiceMock).findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class));
        verify(userServiceMock).findById(1);
    }

    @Test
    public void clickingNewUser_forwardsToUserEditPage() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().assertEnabled("filterForm:newUser");
        FormTester formTester = getTester().newFormTester("filterForm");
        formTester.submit("newUser");

        getTester().assertRenderedPage(UserEditPage.class);

        // verify we have a blank user in the target page
        FormTester targetFormTester = getTester().newFormTester("form");
        assertThat(targetFormTester.getTextComponentValue("issue")).isBlank();

        verify(userServiceMock).countByFilter(isA(UserFilter.class));
        verify(userServiceMock).findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class));
    }

    @Test
    public void clickingRemoveIcon_delegatesRemovalToServiceAndRefreshesResultPanel() {
        getTester().startPage(getPageClass());
        getTester().assertRenderedPage(getPageClass());

        getTester().clickLink("results:body:rows:1:cells:5:cell:link");

        getTester().assertComponentOnAjaxResponse("results");
        getTester().assertComponentOnAjaxResponse("feedback");

        getTester().assertInfoMessages("User user was deleted successfully.");

        verify(userServiceMock).remove(user);

        verify(userServiceMock, times(2)).countByFilter(isA(UserFilter.class));
        verify(userServiceMock, times(2)).findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class));
    }
}