package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class NewsletterTest : PublicEntityTest<Newsletter>() {

    override val toString: String
        get() = "Newsletter(id=1, issue=2018/04, issueDate=2018-04-10)"

    override fun newEntity(): Newsletter = Newsletter(1, "2018/04", LocalDate.of(2018, 4, 10))

    override fun assertSpecificGetters() {
        entity.id shouldBeEqualTo 1
        entity.issue shouldBeEqualTo "2018/04"
        entity.issueDate.toString() shouldBeEqualTo "2018-04-10"

        entity.getMonthName("de") shouldBeEqualTo "April 2018"
        entity.getMonthName("en") shouldBeEqualTo "April 2018"
        entity.getMonthName("fr") shouldBeEqualTo "avril 2018"
    }

    override fun verifyEquals() {
        EqualsVerifier.simple()
            .forClass(NewStudy::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        Newsletter.NewsletterFields.values().map { it.fieldName } shouldContainAll
            listOf("id", "issue", "issueDate", "monthName")
    }
}
