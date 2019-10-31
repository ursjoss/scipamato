package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PublicPaperTest {

    @Test
    fun canSetGet() {
        val pp = PublicPaper
            .builder()
            .id(1L)
            .number(2L)
            .pmId(10000)
            .authors("authors")
            .title("title")
            .location("location")
            .publicationYear(2017)
            .goals("goals")
            .methods("methods")
            .population("population")
            .result("result")
            .comment("comment")
            .codesPopulation(arrayOf(1.toShort(), 2.toShort()))
            .codesStudyDesign(arrayOf(3.toShort(), 4.toShort()))
            .codes(arrayOf("1A", "2B", "3C"))
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build()

        assertThat(pp.id).isEqualTo(1L)
        assertThat(pp.number).isEqualTo(2L)
        assertThat(pp.pmId).isEqualTo(10000)
        assertThat(pp.authors).isEqualTo("authors")
        assertThat(pp.title).isEqualTo("title")
        assertThat(pp.location).isEqualTo("location")
        assertThat(pp.publicationYear).isEqualTo(2017)
        assertThat(pp.goals).isEqualTo("goals")
        assertThat(pp.methods).isEqualTo("methods")
        assertThat(pp.population).isEqualTo("population")
        assertThat(pp.result).isEqualTo("result")
        assertThat(pp.comment).isEqualTo("comment")
        assertThat(pp.version).isEqualTo(3)
        assertThat(pp.created).isEqualTo(CREATED)
        assertThat(pp.lastModified).isEqualTo(MODIFIED)
        assertThat(pp.codesPopulation).containsExactly(1.toShort(), 2.toShort())
        assertThat(pp.codesStudyDesign).containsExactly(3.toShort(), 4.toShort())
        assertThat(pp.codes).containsExactly("1A", "2B", "3C")
        assertThat(pp.lastSynched).isEqualTo(SYNCHED)
    }
}
