package ch.difty.scipamato.core.sync.jobs.newstudypagelink

import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
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

        pp.langCode shouldBeEqualTo "en"
        pp.sort shouldBeEqualTo 1
        pp.title shouldBeEqualTo "title"
        pp.url shouldBeEqualTo "url"
        pp.lastSynched shouldBeEqualTo SYNCHED
    }
}
