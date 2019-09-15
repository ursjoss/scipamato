package ch.difty.scipamato.common.web.model

import ch.difty.scipamato.common.web.WicketBaseTest
import org.junit.jupiter.api.Test

internal class InjectedLoadableDetachableModelTest : WicketBaseTest() {

    @Test
    fun canInstantiate_includingInjecting() {
        object : InjectedLoadableDetachableModel<String>() {
            override fun load(): List<String> = listOf("foo")
        }
        // not actually asserting the injection took place. How would I do that?
    }
}
