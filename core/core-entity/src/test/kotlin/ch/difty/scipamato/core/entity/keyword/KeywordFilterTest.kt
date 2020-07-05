package ch.difty.scipamato.core.entity.keyword

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class KeywordFilterTest {

    private val f = KeywordFilter()

    @Test
    fun getAndSet() {
        f.nameMask = "nameMask"
        f.nameMask shouldBeEqualTo "nameMask"
        f.toString() shouldBeEqualTo "KeywordFilter(nameMask=nameMask)"
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
        KeywordFilter.KeywordFilterFields.values().map { it.fieldName } shouldContainAll listOf("nameMask")
    }
}
