package ch.difty.scipamato.core.entity.search

import ch.difty.scipamato.core.entity.search.SearchTermType.AUDIT
import ch.difty.scipamato.core.entity.search.SearchTermType.BOOLEAN
import ch.difty.scipamato.core.entity.search.SearchTermType.INTEGER
import ch.difty.scipamato.core.entity.search.SearchTermType.STRING
import ch.difty.scipamato.core.entity.search.SearchTermType.UNSUPPORTED
import ch.difty.scipamato.core.entity.search.SearchTermType.byId
import ch.difty.scipamato.core.entity.search.SearchTermType.values
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Test

internal class SearchTermTypeTest {

    @Test
    fun testValues() {
        values() shouldContainAll listOf(BOOLEAN, INTEGER, STRING, AUDIT, UNSUPPORTED)
    }

    @Test
    fun testId() {
        BOOLEAN.id shouldBeEqualTo 0
        INTEGER.id shouldBeEqualTo 1
        STRING.id shouldBeEqualTo 2
        AUDIT.id shouldBeEqualTo 3
        UNSUPPORTED.id shouldBeEqualTo -1
    }

    @Test
    fun testById_withValidIds() {
        byId(0) shouldBeEqualTo BOOLEAN
        byId(1) shouldBeEqualTo INTEGER
        byId(2) shouldBeEqualTo STRING
        byId(3) shouldBeEqualTo AUDIT
    }

    @Test
    fun testById_withInvalidIds() {
        invoking { byId(-2) } shouldThrow IllegalArgumentException::class withMessage "id -2 is not supported"
        invoking { byId(-1) } shouldThrow IllegalArgumentException::class withMessage "id -1 is not supported"
        invoking { byId(4) } shouldThrow IllegalArgumentException::class withMessage "id 4 is not supported"
    }
}
