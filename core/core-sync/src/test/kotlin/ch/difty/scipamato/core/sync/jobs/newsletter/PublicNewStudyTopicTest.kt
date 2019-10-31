package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(topic.newsletterId).isEqualTo(2)
        assertThat(topic.newsletterTopicId).isEqualTo(3)
        assertThat(topic.sort).isEqualTo(4)
        assertThat(topic.version).isEqualTo(5)
        assertThat(topic.created).isEqualTo(CREATED)
        assertThat(topic.lastModified).isEqualTo(MODIFIED)
        assertThat(topic.lastSynched).isEqualTo(SYNCHED)
    }
}
