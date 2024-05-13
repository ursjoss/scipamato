package ch.difty.scipamato.core.web.model

import ch.difty.scipamato.core.web.AbstractWicketTest
import org.apache.wicket.util.tester.WicketTester
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.util.ReflectionTestUtils
import java.util.Locale

abstract class ModelTest : AbstractWicketTest() {

    @BeforeEach
    fun setUp() {
        ReflectionTestUtils.setField(application, "applicationContext", applicationContextMock)
        val tester = WicketTester(application)
        val locale = Locale.of("en_US")
        tester.session.locale = locale
        setUpLocal()
    }

    /**
     * Override if the actual test class needs a setUp
     */
    open fun setUpLocal() {
        // override if necessary
    }
}
