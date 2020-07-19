@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

internal class NewStudyTest : PublicEntityTest<NewStudy>() {

    override val toString: String
        get() = "NewStudy(sort=1, number=10, year=2018, authors=authors, headline=hl, description=descr)"

    override fun newEntity(): NewStudy = NewStudy(1, 10, 2018, "authors", "hl", "descr")

    override fun assertSpecificGetters() {
        entity.sort shouldBeEqualTo 1
        entity.number shouldBeEqualTo 10
        entity.reference shouldBeEqualTo "(authors; 2018)"
        entity.headline shouldBeEqualTo "hl"
        entity.description shouldBeEqualTo "descr"
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
        NewStudy.NewStudyFields.values().map { it.fieldName } shouldContainAll
            listOf("sort", "number", "year", "authors", "reference", "headline", "description")
    }
}
