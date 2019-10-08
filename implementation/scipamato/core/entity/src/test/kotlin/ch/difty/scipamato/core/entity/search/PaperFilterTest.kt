package ch.difty.scipamato.core.entity.search

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(f.number).isEqualTo(1L)
        assertThat(f.authorMask).isEqualTo("authorMask")
        assertThat(f.methodsMask).isEqualTo("methodsMask")
        assertThat(f.searchMask).isEqualTo("searchMask")
        assertThat(f.publicationYearFrom).isEqualTo(2015)
        assertThat(f.publicationYearUntil).isEqualTo(2017)
        assertThat(f.newsletterId).isEqualTo(2)

        assertThat(f.toString()).isEqualTo(
                "PaperFilter(number=1, authorMask=authorMask, methodsMask=methodsMask, searchMask=searchMask, publicationYearFrom=2015, publicationYearUntil=2017, newsletterId=2)")
    }

    @Test
    fun equals() {
        EqualsVerifier
                .forClass(PaperFilter::class.java)
                .withRedefinedSuperclass()
                .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
                .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(PaperFilter.PaperFilterFields.values().map { it.fieldName }).containsExactly(
                "number", "authorMask", "methodsMask", "searchMask", "publicationYearFrom", "publicationYearUntil", "newsletterId"
        )
    }

}
