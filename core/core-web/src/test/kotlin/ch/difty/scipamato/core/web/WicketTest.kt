package ch.difty.scipamato.core.web

import ch.difty.scipamato.core.auth.Roles
import ch.difty.scipamato.core.web.authentication.LoginPage
import ch.difty.scipamato.core.web.paper.list.PaperListPage
import ch.difty.scipamato.core.web.security.TestUserDetailsService
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.checkboxx.CheckBoxX
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapSelect
import io.mockk.every
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.ResourceAggregator
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.TextArea
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.util.ReflectionTestUtils
import java.util.Locale

@Suppress("SpellCheckingInspection")
@SpringBootTest
@ActiveProfiles("wickettest")
abstract class WicketTest : AbstractWicketTest() {

    // override if needed
    protected open val userName = "testuser"
    protected open val PASSWORD = "secretpw"

    lateinit var tester: WicketTester
        private set

    @BeforeEach
    fun setUp() {
        application.setHeaderResponseDecorator { hr: IHeaderResponse? ->
            ResourceAggregator(JavaScriptFilteredIntoFooterHeaderResponse(hr, "footer-container"))
        }
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock)

        tester = WicketTester(application)

        every { sessionFacadeMock.paperIdManager } returns itemNavigatorMock

        val locale = Locale("en_US")

        with(sessionFacadeMock) {
            every { languageCode } returns locale.language
            every { hasAtLeastOneRoleOutOf(Roles.ADMIN) } returns currentUserIsAnyOf(Roles.ADMIN)
            every { hasAtLeastOneRoleOutOf(Roles.USER) } returns currentUserIsAnyOf(Roles.USER)
            every { hasAtLeastOneRoleOutOf(Roles.VIEWER) } returns currentUserIsAnyOf(Roles.VIEWER)
            every { hasAtLeastOneRoleOutOf(Roles.USER, Roles.ADMIN) } returns currentUserIsAnyOf(Roles.ADMIN, Roles.USER)
            every { hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.USER) } returns currentUserIsAnyOf(Roles.ADMIN, Roles.USER)
            every { hasAtLeastOneRoleOutOf(Roles.USER, Roles.VIEWER) } returns currentUserIsAnyOf(Roles.USER, Roles.VIEWER)
            every { hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.VIEWER) } returns currentUserIsAnyOf(Roles.ADMIN, Roles.VIEWER)
            every { hasAtLeastOneRoleOutOf(Roles.ADMIN, Roles.USER, Roles.VIEWER) } returns
                currentUserIsAnyOf(Roles.ADMIN, Roles.USER, Roles.VIEWER)
        }

        tester.session.locale = locale

        with(itemNavigatorMock) {
            every { initialize(any()) } returns Unit
            every { setFocusToItem(any()) } returns Unit
            every { setIdToHeadIfNotPresent(any()) } returns Unit
            every { remove(any()) } returns Unit
            every { hasPrevious() } returns false
            every { hasNext() } returns true
            every { isModified } returns false
        }
        with(paperServiceMock) {
            every { findPageOfIdsByFilter(any(), any()) } returns emptyList()
            every { findPageOfIdsBySearchOrder(any(), any()) } returns emptyList()
            every { hasDuplicateFieldNextToCurrent(any(), any(), any()) } returns java.util.Optional.empty()
            every { excludeFromSearchOrder(any(), any()) } returns Unit
            every { reincludeIntoSearchOrder(any(), any()) } returns Unit
            every { findLowestFreeNumberStartingFrom(any()) } returns 100L
        }
        with(paperSlimServiceMock) {
            every { countByFilter(any()) } returns 0
            every { countBySearchOrder(any()) } returns 0
        }
        with(newsletterServiceMock) {
            every { mergePaperIntoWipNewsletter(any()) } returns Unit
            every { remove(any()) } returns Unit
            every { canCreateNewsletterInProgress() } returns false
            every { removePaperFromWipNewsletter(any()) } returns true
        }
        with(newsletterTopicServiceMock) {
            every { findAll(any()) } returns emptyList()
            every { countByFilter(any()) } returns 0
            every { getSortedNewsletterTopicsForNewsletter(any()) } returns emptyList()
            every { saveSortedNewsletterTopics(any(), any()) } returns Unit
        }
        with(searchOrderServiceMock) {
            every { remove(any()) } returns Unit
            every { saveOrUpdateSearchCondition(any(), any(), any()) } returns null
            every { findPageByFilter(any(), any()) } returns emptyList()
        }

        setUpHook()

        login(userName, PASSWORD)
    }

    private fun currentUserIsAnyOf(vararg roles: String): Boolean {
        roles.forEach { role ->
            when (userName) {
                TestUserDetailsService.USER_ADMIN -> if (Roles.ADMIN == role) return true
                TestUserDetailsService.USER_USER -> if (Roles.USER == role) return true
                TestUserDetailsService.USER_VIEWER -> if (Roles.VIEWER == role) return true
            }
        }
        return false
    }

    /**
     * override if needed
     */
    protected open fun setUpHook() {}

    private fun login(username: String, password: String) {
        val session = tester.session as SecureWebSession
        session.signOut()
        tester.startPage(LoginPage::class.java)
        val formTester = tester.newFormTester("form")
        formTester.setValue("username", username)
        formTester.setValue("password", password)
        formTester.submit()
        tester.assertNoErrorMessage()
        tester.assertRenderedPage(PaperListPage::class.java)
    }

    protected fun assertLabeledTextArea(b: String, id: String) {
        val bb = "$b:$id"
        tester.assertComponent(bb + "Label", Label::class.java)
        tester.assertComponent(bb, TextArea::class.java)
    }

    protected fun assertLabeledTextField(b: String, id: String) {
        val bb = "$b:$id"
        tester.assertComponent(bb + "Label", Label::class.java)
        tester.assertComponent(bb, TextField::class.java)
    }

    @Suppress("SameParameterValue")
    protected fun assertLabeledCheckBoxX(b: String, id: String) {
        val bb = "$b:$id"
        tester.assertComponent(bb + "Label", Label::class.java)
        tester.assertComponent(bb, CheckBoxX::class.java)
    }

    protected fun assertLabeledBootstrapSelect(b: String, id: String) {
        val bb = "$b:$id"
        tester.assertComponent(bb + "Label", Label::class.java)
        tester.assertComponent(bb, BootstrapSelect::class.java)
    }
}
