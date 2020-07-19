package ch.difty.scipamato.core.entity.search

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class SearchOrderFilterTest {

    @Test
    fun canInstantiate() {
        val f = SearchOrderFilter()
        f.nameMask = "foo"
        f.owner = 3
        f.ownerIncludingGlobal = 4
        f.global = true

        f.nameMask shouldBeEqualTo "foo"
        f.owner shouldBeEqualTo 3
        f.ownerIncludingGlobal shouldBeEqualTo 4
        f.global shouldBeEqualTo true

        f.toString() shouldBeEqualTo
            "SearchOrderFilter(nameMask=foo, owner=3, global=true, ownerIncludingGlobal=4)"
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(SearchOrderFilter::class.java)
            .withRedefinedSuperclass()
            .verify()
    }
}
