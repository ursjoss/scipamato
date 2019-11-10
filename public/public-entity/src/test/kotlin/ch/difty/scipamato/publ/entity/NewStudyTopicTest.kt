package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NewStudyTopicTest : PublicEntityTest<NewStudyTopic>() {

    override val toString: String
        get() = "NewStudyTopic(sort=1, title=title, studies=[])"

    override fun newEntity(): NewStudyTopic = NewStudyTopic(1, "title", ArrayList())

    override fun assertSpecificGetters() {
        assertThat(entity.sort).isEqualTo(1)
        assertThat(entity.title).isEqualTo("title")
        assertThat(entity.studies).isEmpty()
    }

    override fun verifyEquals() {
        EqualsVerifier.forClass(NewStudyTopic::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(NewStudyTopic.NewStudyTopicFields.values().map { it.fieldName })
            .containsExactly("sort", "title", "studies")
    }

    @Test
    fun secondaryConstructor_hasNoStudies() {
        val t = NewStudyTopic(2, "title2")
        assertThat(t.sort).isEqualTo(2)
        assertThat(t.title).isEqualTo("title2")
        assertThat(t.studies).isEmpty()
    }
}
