package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class NewsletterTest : PublicEntityTest<Newsletter>() {

    override val toString: String
        get() = "Newsletter(id=1, issue=2018/04, issueDate=2018-04-10)"

    override fun newEntity(): Newsletter = Newsletter(1, "2018/04", LocalDate.of(2018, 4, 10))

    override fun assertSpecificGetters() {
        assertThat(entity.id).isEqualTo(1)
        assertThat(entity.issue).isEqualTo("2018/04")
        assertThat(entity.issueDate.toString()).isEqualTo("2018-04-10")

        assertThat(entity.getMonthName("de")).isEqualTo("April 2018")
        assertThat(entity.getMonthName("en")).isEqualTo("April 2018")
        assertThat(entity.getMonthName("fr")).isEqualTo("avril 2018")
    }

    override fun verifyEquals() {
        EqualsVerifier.forClass(NewStudy::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(Newsletter.NewsletterFields.values().map { it.fieldName })
            .containsExactly("id", "issue", "issueDate", "monthName")
    }
}
