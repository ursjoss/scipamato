package ch.difty.scipamato.core.sync.jobs.newstudypagelink

import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PublicNewStudyPageLinkTest {

    @Test
    fun canSetGet_withStandardFieldsPopulated() {
        val pp = PublicNewStudyPageLink
                .builder()
                .langCode("en")
                .sort(1)
                .title("title")
                .url("url")
                .lastSynched(SYNCHED)
                .build()

        assertThat(pp.langCode).isEqualTo("en")
        assertThat(pp.sort).isEqualTo(1)
        assertThat(pp.title).isEqualTo("title")
        assertThat(pp.url).isEqualTo("url")
        assertThat(pp.lastSynched).isEqualTo(SYNCHED)
    }
}
