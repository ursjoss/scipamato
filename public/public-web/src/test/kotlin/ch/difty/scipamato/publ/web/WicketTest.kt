package ch.difty.scipamato.publ.web

import ch.difty.scipamato.common.DateTimeService
import ch.difty.scipamato.common.navigator.ItemNavigator
import ch.difty.scipamato.common.persistence.paging.PaginationContext
import ch.difty.scipamato.common.web.ScipamatoWebSessionFacade
import ch.difty.scipamato.publ.ScipamatoPublicApplication
import ch.difty.scipamato.publ.entity.Keyword
import ch.difty.scipamato.publ.persistence.api.KeywordService
import ch.difty.scipamato.publ.persistence.api.PublicPaperService
import com.ninjasquad.springmockk.MockkBean
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.select.BootstrapMultiSelect
import io.mockk.Matcher
import io.mockk.MockKMatcherScope
import io.mockk.every
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.ResourceAggregator
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.form.TextField
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.util.ReflectionTestUtils
import java.util.Locale

@SpringBootTest
abstract class WicketTest {

    @Autowired
    private lateinit var application: ScipamatoPublicApplication

    @Autowired
    private lateinit var applicationContextMock: ApplicationContext

    @Autowired
    private lateinit var dateTimeService: DateTimeService

    @MockkBean
    private lateinit var sessionFacadeMock: ScipamatoWebSessionFacade

    @MockkBean(relaxed = true)
    protected lateinit var itemNavigator: ItemNavigator<Long>

    @MockkBean(relaxed = true)
    protected lateinit var paperService: PublicPaperService

    @MockkBean(relaxed = true)
    private lateinit var keywordServiceMock: KeywordService

    lateinit var tester: WicketTester
        private set

    @BeforeEach
    fun setUp() {
        application.setHeaderResponseDecorator { r: IHeaderResponse? -> ResourceAggregator(JavaScriptFilteredIntoFooterHeaderResponse(r, "footer-container")) }
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock)
        tester = WicketTester(application)
        every { sessionFacadeMock.paperIdManager } returns itemNavigator
        val locale = Locale("en_US")
        every { sessionFacadeMock.languageCode } returns locale.language
        tester.session.locale = locale
        every { keywordServiceMock.findKeywords("en_us") } returns listOf(
            Keyword(10, 1, "en", "k1", null),
            Keyword(11, 2, "en", "k2", null)
        )
        setUpHook()
    }

    /**
     * override if needed
     */
    protected open fun setUpHook() {}

    protected fun assertLabeledTextField(b: String, id: String) {
        val bb = "$b:$id"
        tester.assertComponent(bb + "Label", Label::class.java)
        tester.assertComponent(bb, TextField::class.java)
    }

    protected fun assertLabeledMultiSelect(b: String, id: String) {
        val bb = "$b:$id"
        tester.assertComponent(bb + "Label", Label::class.java)
        tester.assertComponent(bb, BootstrapMultiSelect::class.java)
    }
}

inline fun <reified T : PaginationContext> MockKMatcherScope.matchPaginationContext(offset: Int, pageSize: Int, sort: String): T =
    match(PaginationContextMatcher(offset, pageSize, sort))

data class PaginationContextMatcher(
    val offset: Int,
    val pageSize: Int,
    val sort: String
) : Matcher<PaginationContext> {
    override fun match(p: PaginationContext?): Boolean =
        p != null && p.offset == offset && p.pageSize == pageSize && sort == p.sort.toString()
}
