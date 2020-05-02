package ch.difty.scipamato.publ.web.common

import ch.difty.scipamato.publ.web.WicketTest
import org.junit.jupiter.api.Test

abstract class BasePageTest<T : BasePage<*>?> : WicketTest() {

    @Test
    fun assertPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        assertSpecificComponents()
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
        doVerify()
    }

    /**
     * @return instantiated page
     */
    protected abstract fun makePage(): T

    /**
     * @return page class to be tested
     */
    protected abstract val pageClass: Class<T>?

    /**
     * Override if you want to assert specific components
     */
    protected open fun assertSpecificComponents() {}

    /**
     * Override if you need to verify mock calls
     */
    protected open fun doVerify() {}
}