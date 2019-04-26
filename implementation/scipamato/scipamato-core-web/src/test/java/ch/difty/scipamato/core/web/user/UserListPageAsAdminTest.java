package ch.difty.scipamato.core.web.user;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.core.auth.Role;
import ch.difty.scipamato.core.entity.User;
import ch.difty.scipamato.core.entity.search.UserFilter;
import ch.difty.scipamato.core.persistence.UserService;
import ch.difty.scipamato.core.web.common.BasePageTest;

@SuppressWarnings("SameParameterValue")
class UserListPageAsAdminTest extends BasePageTest<UserListPage> {

    private User enabledUser  = new User(1, "enabledUser", "first", "last", "foo@bar.baz", "pw", true,
        Set.of(Role.ADMIN, Role.USER));
    private User disabledUser = new User(2, "disabledUser", "f", "l", "boo@bar.baz", "pw2", false, Set.of(Role.VIEWER));

    private final List<User> results = new ArrayList<>();

    @MockBean
    protected UserService userServiceMock;

    @Override
    protected String getUserName() {
        return "testadmin";
    }

    @Override
    protected void setUpHook() {
        results.add(disabledUser);
        results.add(enabledUser);
        when(userServiceMock.countByFilter(isA(UserFilter.class))).thenReturn(results.size());
        when(userServiceMock.findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class))).thenReturn(results);
    }

    @AfterEach
    void tearDown() {
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

        final String[] headers = { "User Name", "First Name", "Last Name", "Email", "Enabled" };
        final String[] row1 = { "disabledUser", "f", "l", "boo@bar.baz", "Disabled" };
        final String[] row2 = { "enabledUser", "first", "last", "foo@bar.baz", "Enabled" };
        assertResultTable("results", headers, row1, row2);

        verify(userServiceMock).countByFilter(isA(UserFilter.class));
        verify(userServiceMock).findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class));
    }

    private void assertFilterForm(final String b) {
        getTester().assertComponent(b, Form.class);
        assertLabeledTextField(b, "userName");
        getTester().assertComponent(b + ":newUser", BootstrapAjaxButton.class);
    }

    private void assertResultTable(final String b, final String[] labels, final String[]... rows) {
        getTester().assertComponent(b, BootstrapDefaultDataTable.class);
        assertHeaderColumns(b, labels);
        assertTableValuesOfRow(b, 1, 1, rows);
    }

    private void assertHeaderColumns(final String b, final String[] labels) {
        int idx = 0;
        for (final String label : labels)
            getTester().assertLabel(
                b + ":topToolbars:toolbars:2:headers:" + ++idx + ":header:orderByLink:header_body:label", label);
    }

    private void assertTableValuesOfRow(final String b, final int rowIdx, final Integer colIdxAsLink,
        final String[]... rows) {
        int rIdx = rowIdx;
        for (final String[] values : rows) {
            int colIdx = 1;
            if (colIdxAsLink != null)
                getTester().assertComponent(b + ":body:rows:" + rIdx + ":cells:" + colIdxAsLink + ":cell:link",
                    Link.class);
            for (final String value : values)
                getTester().assertLabel(b + ":body:rows:" + rIdx + ":cells:" + colIdx + ":cell" + (
                    colIdxAsLink != null && colIdx++ == colIdxAsLink ? ":link:label" : ""), value);
            rIdx++;
        }
    }

    @Test
    void clickingOnUserName_forwardsToUserEntryPage_withModelLoaded() {
        when(userServiceMock.findById(1)).thenReturn(Optional.of(enabledUser));
        getTester().startPage(getPageClass());

        getTester().clickLink("results:body:rows:2:cells:1:cell:link");
        getTester().assertRenderedPage(UserEditPage.class);

        // verify the user was loaded in the target page
        FormTester formTester = getTester().newFormTester("form");
        assertThat(formTester.getTextComponentValue("userName")).isEqualTo("enabledUser");

        verify(userServiceMock).countByFilter(isA(UserFilter.class));
        verify(userServiceMock).findPageByFilter(isA(UserFilter.class), isA(PaginationRequest.class));
        verify(userServiceMock).findById(1);
    }

    @Test
    void clickingNewUser_forwardsToUserEditPage() {
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

}