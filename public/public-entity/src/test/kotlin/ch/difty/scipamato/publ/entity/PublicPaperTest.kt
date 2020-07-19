package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test

@Suppress("SpellCheckingInspection")
internal class PublicPaperTest : PublicEntityTest<PublicPaper>() {

    override val toString: String
        get() = "PublicPaper(id=1, number=2, pmId=1000, authors=authors, authorsAbbreviated=auths, title=title, " +
            "location=location, journal=journal, publicationYear=2016, goals=goals, methods=methods, " +
            "population=population, result=result, comment=comment)"

    override fun newEntity(): PublicPaper = PublicPaper.builder()
        .id(1L)
        .number(2L)
        .pmId(1000)
        .authors("authors")
        .authorsAbbreviated("auths")
        .title("title")
        .location("location")
        .journal("journal")
        .publicationYear(2016)
        .goals("goals")
        .methods("methods")
        .population("population")
        .result("result")
        .comment("comment")
        .build()

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
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        PublicPaper.PublicPaperFields.values().map { it.fieldName } shouldContainAll listOf(
            "id", "number", "pmId", "authors", "authorsAbbreviated", "title", "location", "journal",
            "publicationYear", "goals", "methods", "population", "result", "comment"
        )
    }
}
