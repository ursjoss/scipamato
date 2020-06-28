package ch.difty.scipamato.core.sync.jobs.code

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PublicCodeTest {

    @Test
    fun canSetGet() {
        val pc = PublicCode
            .builder()
            .code("c")
            .langCode("lc")
            .codeClassId(1)
            .name("name")
            .comment("comment")
            .sort(2)
            .version(3)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build()

        assertThat(pc.code).isEqualTo("c")
        assertThat(pc.langCode).isEqualTo("lc")
        assertThat(pc.codeClassId).isEqualTo(1)
        assertThat(pc.name).isEqualTo("name")
        assertThat(pc.comment).isEqualTo("comment")
        assertThat(pc.sort).isEqualTo(2)
        assertThat(pc.version).isEqualTo(3)
        assertThat(pc.created).isEqualTo(CREATED)
        assertThat(pc.lastModified).isEqualTo(MODIFIED)
        assertThat(pc.lastSynched).isEqualTo(SYNCHED)
    }
}
