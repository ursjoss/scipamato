package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class NewStudyTopicTest : PublicDbEntityTest<NewStudyTopic>() {

    override fun newEntity(created: LocalDateTime, lastModified: LocalDateTime, version: Int) =
        NewStudyTopic(
            sort = 1,
            title = "title",
            studies = listOf(NewStudy(1, 1), NewStudy(2, 2)),
            created = created,
            lastModified = lastModified,
            version = version
        )

    override fun assertSpecificGetters() {
        entity.sort shouldBeEqualTo 1
        entity.title shouldBeEqualTo "title"
        entity.studies.shouldHaveSize(2)
    }

    override fun verifyEquals() {
        EqualsVerifier.simple().forClass(NewStudyTopic::class.java).verify()
    }

    @Test
    fun secondaryConstructor_hasNoStudies() {
        val t = NewStudyTopic(2, "title2")
        t.sort shouldBeEqualTo 2
        t.title shouldBeEqualTo "title2"
        t.studies.shouldBeEmpty()
    }
}
