package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NewStudyTest : PublicEntityTest<NewStudy>() {

    override val toString: String
        get() = "NewStudy(sort=1, number=10, year=2018, authors=authors, headline=hl, description=descr)"

    override fun newEntity(): NewStudy = NewStudy(1, 10, 2018, "authors", "hl", "descr")

    override fun assertSpecificGetters() {
        assertThat(entity.sort).isEqualTo(1)
        assertThat(entity.number).isEqualTo(10)
        assertThat(entity.reference).isEqualTo("(authors; 2018)")
        assertThat(entity.headline).isEqualTo("hl")
        assertThat(entity.description).isEqualTo("descr")
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
        assertThat(NewStudy.NewStudyFields.values().map { it.fieldName }).containsExactly("sort", "number", "year", "authors", "reference", "headline", "description")
    }

}
