package ch.difty.scipamato.common.web

import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse
import org.apache.wicket.protocol.http.WebApplication
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.util.ReflectionTestUtils

internal const val USERNAME = "testuser"
internal const val PASSWORD = "secretpw"

@Suppress("UnnecessaryAbstractClass")
@SpringBootTest(classes = [TestApplication::class])
abstract class WicketBaseTest {

    protected lateinit var tester: WicketTester

    @Autowired
    private lateinit var wicketApplication: WebApplication

    @Autowired
    private lateinit var applicationContextMock: ApplicationContext

    @BeforeEach
    internal fun setUp() {
        wicketApplication.headerResponseDecorators.add { r ->
            JavaScriptFilteredIntoFooterHeaderResponse(r, AbstractPage.FOOTER_CONTAINER)
        }

        ReflectionTestUtils.setField(wicketApplication, "applicationContext", applicationContextMock)
        tester = WicketTester(wicketApplication)
        val locale = java.util.Locale.of("en_US")
        tester.session.locale = locale
        setUpHook()
    }

    /**
     * override if needed
     */
    protected open fun setUpHook() {}
}
