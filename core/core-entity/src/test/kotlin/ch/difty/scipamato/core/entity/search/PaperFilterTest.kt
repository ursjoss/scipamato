package ch.difty.scipamato.core.entity.search

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class PaperFilterTest {

    private val f = PaperFilter()

    @Test
    fun getAndSet() {
        f.number = 1L
        f.authorMask = "authorMask"
        f.methodsMask = "methodsMask"
        f.searchMask = "searchMask"
        f.publicationYearFrom = 2015
        f.publicationYearUntil = 2017
        f.newsletterId = 2

        f.number shouldBeEqualTo 1L
        f.authorMask shouldBeEqualTo "authorMask"
        f.methodsMask shouldBeEqualTo "methodsMask"
        f.searchMask shouldBeEqualTo "searchMask"
        f.publicationYearFrom shouldBeEqualTo 2015
        f.publicationYearUntil shouldBeEqualTo 2017
        f.newsletterId shouldBeEqualTo 2

        f.toString() shouldBeEqualTo
            "PaperFilter(number=1, authorMask=authorMask, methodsMask=methodsMask, searchMask=searchMask, " +
            "publicationYearFrom=2015, publicationYearUntil=2017, newsletterId=2)"
    }

    @Test
    fun equals() {
        EqualsVerifier.simple()
            .forClass(PaperFilter::class.java)
            .withRedefinedSuperclass()
            .verify()
    }

    @Test
    fun assertEnumFields() {
        PaperFilter.PaperFilterFields.values().map { it.fieldName } shouldContainAll listOf(
            "number",
            "authorMask",
            "methodsMask",
            "searchMask",
            "publicationYearFrom",
            "publicationYearUntil",
            "newsletterId"
        )
    }
}
