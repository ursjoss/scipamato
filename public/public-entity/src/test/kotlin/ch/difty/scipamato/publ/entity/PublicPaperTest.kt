package ch.difty.scipamato.publ.entity

import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED
import ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED
import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(entity.id).isEqualTo(1L)
        assertThat(entity.number).isEqualTo(2L)
        assertThat(entity.pmId).isEqualTo(1000)
        assertThat(entity.authors).isEqualTo("authors")
        assertThat(entity.authorsAbbreviated).isEqualTo("auths")
        assertThat(entity.title).isEqualTo("title")
        assertThat(entity.location).isEqualTo("location")
        assertThat(entity.journal).isEqualTo("journal")
        assertThat(entity.publicationYear).isEqualTo(2016)
        assertThat(entity.goals).isEqualTo("goals")
        assertThat(entity.methods).isEqualTo("methods")
        assertThat(entity.population).isEqualTo("population")
        assertThat(entity.result).isEqualTo("result")
        assertThat(entity.comment).isEqualTo("comment")
    }

    public override fun verifyEquals() {
        EqualsVerifier.forClass(PublicPaper::class.java)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.fieldName, MODIFIED.fieldName)
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify()
    }

    @Test
    fun assertEnumFields() {
        assertThat(PublicPaper.PublicPaperFields.values().map { it.fieldName })
            .containsExactly(
                "id", "number", "pmId", "authors", "authorsAbbreviated", "title", "location", "journal",
                "publicationYear", "goals", "methods", "population", "result", "comment"
            )
    }
}
