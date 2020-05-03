package ch.difty.scipamato.core.web

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class CorePageParametersTest {

    @Test
    fun values() {
        CorePageParameters.values() shouldContainSame listOf(
            CorePageParameters.SEARCH_ORDER_ID,
            CorePageParameters.SHOW_EXCLUDED,
            CorePageParameters.USER_ID,
            CorePageParameters.MODE,
            CorePageParameters.TAB_INDEX
        )
    }

    @Test
    fun assertRoleNames() {
        CorePageParameters.SEARCH_ORDER_ID.getName() shouldBeEqualTo "searchOrderId"
        CorePageParameters.SHOW_EXCLUDED.getName() shouldBeEqualTo "showExcluded"
        CorePageParameters.USER_ID.getName() shouldBeEqualTo "userId"
        CorePageParameters.MODE.getName() shouldBeEqualTo "mode"
        CorePageParameters.TAB_INDEX.getName() shouldBeEqualTo "tabIndex"
    }
}
