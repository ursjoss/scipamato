package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PublicNewStudyTopicTest {

    @Test
    fun canSetGet() {
        val topic = PublicNewStudyTopic
            .builder()
            .newsletterId(2)
            .newsletterTopicId(3)
            .sort(4)
            .version(5)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build()

        topic.newsletterId shouldBeEqualTo 2
        topic.newsletterTopicId shouldBeEqualTo 3
        topic.sort shouldBeEqualTo 4
        topic.version shouldBeEqualTo 5
        topic.created shouldBeEqualTo CREATED
        topic.lastModified shouldBeEqualTo MODIFIED
        topic.lastSynched shouldBeEqualTo SYNCHED
    }
}
