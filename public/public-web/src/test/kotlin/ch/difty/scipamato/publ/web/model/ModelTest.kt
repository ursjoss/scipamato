package ch.difty.scipamato.publ.web.model

import ch.difty.scipamato.publ.ScipamatoPublicApplication
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.util.ReflectionTestUtils
import java.util.Locale

@SpringBootTest
@Suppress("UnnecessaryAbstractClass")
abstract class ModelTest {

    @Autowired
    private val application: ScipamatoPublicApplication? = null

    @Autowired
    private val applicationContextMock: ApplicationContext? = null

    @BeforeEach
    protected fun setUp() {
        ReflectionTestUtils.setField(application!!, "applicationContext", applicationContextMock)
        val tester = WicketTester(application)
        val locale = Locale("en_US")
        tester
            .session.locale = locale
        setUpLocal()
    }

    /**
     * Override if the actual test class needs a setUp
     */
    protected fun setUpLocal() {
        // override if necessary
    }
}
