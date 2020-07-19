package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class NewStudyTopicTest : PublicEntityTest<NewStudyTopic>() {

    override val toString: String
        get() = "NewStudyTopic(sort=1, title=title, studies=[])"

    override fun newEntity(): NewStudyTopic = NewStudyTopic(1, "title", ArrayList())

    override fun assertSpecificGetters() {
        entity.sort shouldBeEqualTo 1
        entity.title shouldBeEqualTo "title"
        entity.studies.shouldBeEmpty()
    }

    override fun verifyEquals() {
        EqualsVerifier.simple()
            .forClass(NewStudyTopic::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        NewStudyTopic.NewStudyTopicFields.values().map { it.fieldName } shouldContainAll
            listOf("sort", "title", "studies")
    }

    @Test
    fun secondaryConstructor_hasNoStudies() {
        val t = NewStudyTopic(2, "title2")
        t.sort shouldBeEqualTo 2
        t.title shouldBeEqualTo "title2"
        t.studies.shouldBeEmpty()
    }
}
