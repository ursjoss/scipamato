package ch.difty.scipamato.core.sync.jobs.paper

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainAll
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

        pp.id shouldBeEqualTo 1L
        pp.number shouldBeEqualTo 2L
        pp.pmId shouldBeEqualTo 10000
        pp.authors shouldBeEqualTo "authors"
        pp.title shouldBeEqualTo "title"
        pp.location shouldBeEqualTo "location"
        pp.publicationYear shouldBeEqualTo 2017
        pp.goals shouldBeEqualTo "goals"
        pp.methods shouldBeEqualTo "methods"
        pp.population shouldBeEqualTo "population"
        pp.result shouldBeEqualTo "result"
        pp.comment shouldBeEqualTo "comment"
        pp.version shouldBeEqualTo 3
        pp.created shouldBeEqualTo CREATED
        pp.lastModified shouldBeEqualTo MODIFIED
        pp.codesPopulation shouldContainAll listOf(1.toShort(), 2.toShort())
        pp.codesStudyDesign shouldContainAll listOf(3.toShort(), 4.toShort())
        pp.codes shouldContainAll listOf("1A", "2B", "3C")
        pp.lastSynched shouldBeEqualTo SYNCHED
    }
}
