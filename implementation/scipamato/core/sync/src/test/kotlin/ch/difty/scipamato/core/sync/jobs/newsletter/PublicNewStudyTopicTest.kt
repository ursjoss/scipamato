package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PublicNewStudyTopicTest {

    @Test
    fun canSetGet() {
        val pnst = PublicNewStudyTopic
                .builder()
                .newsletterId(2)
                .newsletterTopicId(3)
                .sort(4)
                .version(5)
                .created(CREATED)
                .lastModified(MODIFIED)
                .lastSynched(SYNCHED)
                .build()

        assertThat(pnst.newsletterId).isEqualTo(2)
        assertThat(pnst.newsletterTopicId).isEqualTo(3)
        assertThat(pnst.sort).isEqualTo(4)
        assertThat(pnst.version).isEqualTo(5)
        assertThat(pnst.created).isEqualTo(CREATED)
        assertThat(pnst.lastModified).isEqualTo(MODIFIED)
        assertThat(pnst.lastSynched).isEqualTo(SYNCHED)
    }

}