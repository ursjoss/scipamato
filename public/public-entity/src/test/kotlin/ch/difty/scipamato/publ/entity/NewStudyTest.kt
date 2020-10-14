@file:Suppress("SpellCheckingInspection")

package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import java.time.LocalDateTime

internal class NewStudyTest : PublicDbEntityTest<NewStudy>() {

    override fun newEntity(created: LocalDateTime, lastModified: LocalDateTime, version: Int) =
        NewStudy(
            sort = 1,
            number = 10,
            year = 2018,
            authors = "authors",
            headline = "hl",
            description = "descr",
            created = created,
            lastModified = lastModified,
            version = version
        )

    override fun assertSpecificGetters() {
        entity.sort shouldBeEqualTo 1
        entity.number shouldBeEqualTo 10
        entity.reference shouldBeEqualTo "(authors; 2018)"
        entity.headline shouldBeEqualTo "hl"
        entity.description shouldBeEqualTo "descr"
    }

    override fun verifyEquals() {
        EqualsVerifier.simple().forClass(NewStudy::class.java).verify()
    }
}
