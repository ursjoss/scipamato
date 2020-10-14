package ch.difty.scipamato.publ.entity

import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import java.time.LocalDateTime

@Suppress("SpellCheckingInspection")
internal class PublicPaperTest : PublicDbEntityTest<PublicPaper>() {

    override fun newEntity(created: LocalDateTime, lastModified: LocalDateTime, version: Int) = PublicPaper(
        id = 1L,
        number = 2L,
        pmId = 1000,
        authors = "authors",
        authorsAbbreviated = "auths",
        title = "title",
        location = "location",
        journal = "journal",
        publicationYear = 2016,
        goals = "goals",
        methods = "methods",
        population = "population",
        result = "result",
        comment = "comment",
        created = created,
        lastModified = lastModified,
        version = version
    )

    override fun assertSpecificGetters() {
        entity.id shouldBeEqualTo 1L
        entity.number shouldBeEqualTo 2L
        entity.pmId shouldBeEqualTo 1000
        entity.authors shouldBeEqualTo "authors"
        entity.authorsAbbreviated shouldBeEqualTo "auths"
        entity.title shouldBeEqualTo "title"
        entity.location shouldBeEqualTo "location"
        entity.journal shouldBeEqualTo "journal"
        entity.publicationYear shouldBeEqualTo 2016
        entity.goals shouldBeEqualTo "goals"
        entity.methods shouldBeEqualTo "methods"
        entity.population shouldBeEqualTo "population"
        entity.result shouldBeEqualTo "result"
        entity.comment shouldBeEqualTo "comment"
    }

    public override fun verifyEquals() {
        EqualsVerifier.simple()
            .forClass(PublicPaper::class.java)
            .verify()
    }
}
