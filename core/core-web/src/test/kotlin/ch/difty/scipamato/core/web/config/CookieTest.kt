package ch.difty.scipamato.core.web.config

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import java.util.HashSet

internal class CookieTest {

    @Test
    fun assertValues() {
        Cookie.values() shouldContainAll listOf(Cookie.PAPER_LIST_PAGE_MODAL_WINDOW)
    }

    @Test
    fun assertPaperListPageModalWindow() {
        Cookie.PAPER_LIST_PAGE_MODAL_WINDOW.getName() shouldBeEqualTo "SciPaMaTo-xmlPasteModal-1"
    }

    @Test
    fun assertAllNamesAreUnique() {
        val cookies: MutableSet<String> = HashSet()
        for (c in Cookie.values()) cookies.add(c.getName())
        cookies shouldHaveSize Cookie.values().size
    }
}
