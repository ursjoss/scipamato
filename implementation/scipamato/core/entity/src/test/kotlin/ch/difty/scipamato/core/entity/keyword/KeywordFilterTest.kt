package ch.difty.scipamato.core.entity.keyword

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KeywordFilterTest {

    private val f = KeywordFilter()

    @Test
    fun getAndSet() {
        f.nameMask = "nameMask"
        assertThat(f.nameMask).isEqualTo("nameMask")
        assertThat(f.toString()).isEqualTo("KeywordFilter(nameMask=nameMask)")
    }

    @Test
    fun equals() {
        EqualsVerifier
                .forClass(KeywordFilter::class.java)
                .withRedefinedSuperclass()
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(KeywordFilter.KeywordFilterFields.values()).extracting("name").containsExactly("nameMask")
    }

}