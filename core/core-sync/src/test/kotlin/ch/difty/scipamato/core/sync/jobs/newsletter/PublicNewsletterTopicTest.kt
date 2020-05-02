package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PublicNewsletterTopicTest {

    @Test
    fun canSetGet() {
        val pnt = PublicNewsletterTopic
            .builder()
            .id(1)
            .langCode("lc")
            .title("t")
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build()

        pnt.id shouldBeEqualTo 1
        pnt.langCode shouldBeEqualTo "lc"
        pnt.title shouldBeEqualTo "t"
        pnt.version shouldBeEqualTo 3
        pnt.created shouldBeEqualTo CREATED
        pnt.lastModified shouldBeEqualTo MODIFIED
        pnt.lastSynched shouldBeEqualTo SYNCHED
    }
}
