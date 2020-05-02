package ch.difty.scipamato.core.sync.jobs.keyword

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PublicKeywordTest {

    @Test
    fun canSetGet() {
        val pc = PublicKeyword
            .builder()
            .id(1)
            .keywordId(2)
            .langCode("lc")
            .name("name")
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .searchOverride("so")
            .build()

        pc.id shouldBeEqualTo 1
        pc.keywordId shouldBeEqualTo 2
        pc.langCode shouldBeEqualTo "lc"
        pc.name shouldBeEqualTo "name"
        pc.version shouldBeEqualTo 3
        pc.created shouldBeEqualTo CREATED
        pc.lastModified shouldBeEqualTo MODIFIED
        pc.lastSynched shouldBeEqualTo SYNCHED
        pc.searchOverride shouldBeEqualTo "so"
    }
}
