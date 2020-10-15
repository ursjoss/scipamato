package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import java.time.LocalDate

internal class NewsletterTest : PublicDbEntityTest<Newsletter>() {

    override fun newEntity() =
        Newsletter(
            id = 1,
            issue = "2018/04",
            issueDate = LocalDate.of(2018, 4, 10),
        )

    override fun assertSpecificGetters() {
        entity.id shouldBeEqualTo 1
        entity.issue shouldBeEqualTo "2018/04"
        entity.issueDate.toString() shouldBeEqualTo "2018-04-10"

        entity.getMonthName("de") shouldBeEqualTo "April 2018"
        entity.getMonthName("en") shouldBeEqualTo "April 2018"
        entity.getMonthName("fr") shouldBeEqualTo "avril 2018"
    }

    override fun verifyEquals() {
        EqualsVerifier.simple().forClass(Newsletter::class.java).verify()
    }
}
