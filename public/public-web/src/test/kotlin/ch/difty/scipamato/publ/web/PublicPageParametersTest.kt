package ch.difty.scipamato.publ.web

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class PublicPageParametersTest {

    @Test
    fun assertParameters() {
        PublicPageParameters.values() shouldContainSame listOf(
            PublicPageParameters.SEARCH_ORDER_ID,
            PublicPageParameters.SHOW_EXCLUDED,
            PublicPageParameters.SHOW_NAVBAR,
            PublicPageParameters.NUMBER,
            PublicPageParameters.ISSUE,
            PublicPageParameters.PARENT_URL
        )
    }

    @Test
    fun assertRoleNames() {
        PublicPageParameters.SEARCH_ORDER_ID.parameterName shouldBeEqualTo "searchOrderId"
        PublicPageParameters.SHOW_EXCLUDED.parameterName shouldBeEqualTo "showExcluded"
        PublicPageParameters.SHOW_NAVBAR.parameterName shouldBeEqualTo "showNavbar"
        PublicPageParameters.NUMBER.parameterName shouldBeEqualTo "number"
        PublicPageParameters.ISSUE.parameterName shouldBeEqualTo "issue"
        PublicPageParameters.PARENT_URL.parameterName shouldBeEqualTo "parentUrl"
    }
}
