package ch.difty.scipamato.core.web.common

import ch.difty.scipamato.core.web.WicketTest
import org.junit.jupiter.api.Test

abstract class BasePageTest<T : BasePage<*>> : WicketTest() {

    @Test
    fun assertPage() {
        tester.startPage(makePage())
        tester.assertRenderedPage(pageClass)
        assertSpecificComponents()
        tester.assertNoErrorMessage()
        tester.assertNoInfoMessage()
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
}