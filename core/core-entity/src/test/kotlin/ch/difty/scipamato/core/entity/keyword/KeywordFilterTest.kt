package ch.difty.scipamato.core.entity.keyword

import nl.jqno.equalsverifier.EqualsVerifier
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
        EqualsVerifier.simple()
            .forClass(KeywordFilter::class.java)
            .withRedefinedSuperclass()
            .verify()
    }

    @Test
    fun assertEnumFields() {
        KeywordFilter.KeywordFilterFields.entries.map { it.fieldName } shouldContainAll listOf("nameMask")
    }
}
