package ch.difty.scipamato.core.sync.jobs.newsletter


import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(pnt.id).isEqualTo(1)
        assertThat(pnt.langCode).isEqualTo("lc")
        assertThat(pnt.title).isEqualTo("t")
        assertThat(pnt.version).isEqualTo(3)
        assertThat(pnt.created).isEqualTo(CREATED)
        assertThat(pnt.lastModified).isEqualTo(MODIFIED)
        assertThat(pnt.lastSynched).isEqualTo(SYNCHED)
    }
}