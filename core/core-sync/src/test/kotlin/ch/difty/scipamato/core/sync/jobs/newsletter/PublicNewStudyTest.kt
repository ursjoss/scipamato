package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PublicNewStudyTest {

    @Test
    fun canSetGet() {
        val pns = PublicNewStudy
            .builder()
            .newsletterId(1)
            .newsletterTopicId(2)
            .sort(3)
            .paperNumber(4L)
            .year(2018)
            .authors("a")
            .headline("hl")
            .description("d")
            .version(5)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build()

        pns.newsletterId shouldBeEqualTo 1
        pns.newsletterTopicId shouldBeEqualTo 2
        pns.sort shouldBeEqualTo 3
        pns.paperNumber shouldBeEqualTo 4
        pns.year shouldBeEqualTo 2018
        pns.authors shouldBeEqualTo "a"
        pns.headline shouldBeEqualTo "hl"
        pns.description shouldBeEqualTo "d"
        pns.version shouldBeEqualTo 5
        pns.created shouldBeEqualTo CREATED
        pns.lastModified shouldBeEqualTo MODIFIED
        pns.lastSynched shouldBeEqualTo SYNCHED
    }
}
