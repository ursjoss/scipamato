package ch.difty.scipamato.core.web.user

import ch.difty.scipamato.core.auth.Role
import ch.difty.scipamato.core.entity.User
import ch.difty.scipamato.core.web.common.BasePageTest
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxButton
import de.agilecoders.wicket.extensions.markup.html.bootstrap.table.BootstrapDefaultDataTable
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.markup.html.link.Link
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.util.ArrayList
import java.util.Optional

@Suppress("SameParameterValue")
internal class UserListPageAsAdminTest : BasePageTest<UserListPage>() {

    private val enabledUser =
        User(1, "enabledUser", "first", "last", "foo@bar.baz", "pw", true,
            setOf(Role.ADMIN, Role.USER))
    private val disabledUser =
        User(2, "disabledUser", "f", "l", "boo@bar.baz", "pw2", false,
            setOf(Role.VIEWER))

    private val results: MutableList<User> = ArrayList()

    override val userName: String
        get() = "testadmin"

    override fun setUpHook() {
        results.add(disabledUser)
        results.add(enabledUser)
        every { userServiceMock.countByFilter(any()) } returns results.size
        every { userServiceMock.findPageByFilter(any(), any()) } returns results
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(userServiceMock)
    }

    override fun makePage(): UserListPage = UserListPage(null)

    override val pageClass: Class<UserListPage>
        get() = UserListPage::class.java

    override fun assertSpecificComponents() {
        assertFilterForm("filterForm")
        val headers = arrayOf("User Name", "First Name", "Last Name", "Email", "Enabled")
        val row1 = arrayOf("disabledUser", "f", "l", "boo@bar.baz", "Disabled")
        val row2 = arrayOf("enabledUser", "first", "last", "foo@bar.baz", "Enabled")
        assertResultTable("results", headers, row1, row2)
        verify { userServiceMock.countByFilter(any()) }
        verify { userServiceMock.findPageByFilter(any(), any()) }
    }

    private fun assertFilterForm(b: String) {
        tester.assertComponent(b, Form::class.java)
        assertLabeledTextField(b, "userName")
        tester.assertComponent("$b:newUser", BootstrapAjaxButton::class.java)
    }

    private fun assertResultTable(b: String, labels: Array<String>, vararg rows: Array<String>) {
        tester.assertComponent(b, BootstrapDefaultDataTable::class.java)
        assertHeaderColumns(b, labels)
        assertTableValuesOfRow(b, 1, 1, *rows)
    }

    private fun assertHeaderColumns(b: String, labels: Array<String>) {
        var idx = 0
        for (label in labels) tester.assertLabel(
            b + ":topToolbars:toolbars:2:headers:" + ++idx + ":header:orderByLink:header_body:label", label)
    }

    private fun assertTableValuesOfRow(b: String, rowIdx: Int, colIdxAsLink: Int?, vararg rows: Array<String>) {
        var rIdx = rowIdx
        for (values in rows) {
            var colIdx = 1
            if (colIdxAsLink != null) tester.assertComponent("$b:body:rows:$rIdx:cells:$colIdxAsLink:cell:link",
                Link::class.java)
            for (value in values) tester.assertLabel(b + ":body:rows:" + rIdx + ":cells:" + colIdx + ":cell" + if (colIdxAsLink != null && colIdx++ == colIdxAsLink) ":link:label" else "", value)
            rIdx++
        }
    }

    @Test
    fun clickingOnUserName_forwardsToUserEntryPage_withModelLoaded() {
        every { userServiceMock.findById(1) } returns Optional.of(enabledUser)
        tester.startPage(pageClass)
        tester.clickLink("results:body:rows:2:cells:1:cell:link")
        tester.assertRenderedPage(UserEditPage::class.java)

        // verify the user was loaded in the target page
        val formTester = tester.newFormTester("form")
        formTester.getTextComponentValue("userName") shouldBeEqualTo "enabledUser"
        verify { userServiceMock.countByFilter(any()) }
        verify { userServiceMock.findPageByFilter(any(), any()) }
        verify { userServiceMock.findById(1) }
    }

    @Test
    fun clickingNewUser_forwardsToUserEditPage() {
        tester.startPage(pageClass)
        tester.assertRenderedPage(pageClass)
        tester.assertEnabled("filterForm:newUser")
        val formTester = tester.newFormTester("filterForm")
        formTester.submit("newUser")
        tester.assertRenderedPage(UserEditPage::class.java)

        // verify we have a blank user in the target page
        val targetFormTester = tester.newFormTester("form")
        targetFormTester.getTextComponentValue("issue").shouldBeNull()
        verify { userServiceMock.countByFilter(any()) }
        verify { userServiceMock.findPageByFilter(any(), any()) }
    }
}
