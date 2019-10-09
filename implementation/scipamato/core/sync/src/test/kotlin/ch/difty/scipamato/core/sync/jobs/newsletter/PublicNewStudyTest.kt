package ch.difty.scipamato.core.sync.jobs.newsletter

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(pns.newsletterId).isEqualTo(1)
        assertThat(pns.newsletterTopicId).isEqualTo(2)
        assertThat(pns.sort).isEqualTo(3)
        assertThat(pns.paperNumber).isEqualTo(4)
        assertThat(pns.year).isEqualTo(2018)
        assertThat(pns.authors).isEqualTo("a")
        assertThat(pns.headline).isEqualTo("hl")
        assertThat(pns.description).isEqualTo("d")
        assertThat(pns.version).isEqualTo(5)
        assertThat(pns.created).isEqualTo(CREATED)
        assertThat(pns.lastModified).isEqualTo(MODIFIED)
        assertThat(pns.lastSynched).isEqualTo(SYNCHED)
    }
}
