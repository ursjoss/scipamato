package ch.difty.scipamato.core.entity.search

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SearchOrderFilterTest {

    @Test
    fun canInstantiate() {
        val f = SearchOrderFilter()
        f.nameMask = "foo"
        f.owner = 3
        f.ownerIncludingGlobal = 4
        f.global = true

        assertThat(f.nameMask).isEqualTo("foo")
        assertThat(f.owner).isEqualTo(3)
        assertThat(f.ownerIncludingGlobal).isEqualTo(4)
        assertThat(f.global).isEqualTo(true)

        assertThat(f.toString()).isEqualTo(
            "SearchOrderFilter(nameMask=foo, owner=3, global=true, ownerIncludingGlobal=4)"
        )
    }

    @Test
    fun equals() {
        EqualsVerifier
            .forClass(SearchOrderFilter::class.java)
            .withRedefinedSuperclass()
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }
}
