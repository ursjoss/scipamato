package ch.difty.scipamato.core.sync.jobs.keyword

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(pc.id).isEqualTo(1)
        assertThat(pc.keywordId).isEqualTo(2)
        assertThat(pc.langCode).isEqualTo("lc")
        assertThat(pc.name).isEqualTo("name")
        assertThat(pc.version).isEqualTo(3)
        assertThat(pc.created).isEqualTo(CREATED)
        assertThat(pc.lastModified).isEqualTo(MODIFIED)
        assertThat(pc.lastSynched).isEqualTo(SYNCHED)
        assertThat(pc.searchOverride).isEqualTo("so")
    }
}