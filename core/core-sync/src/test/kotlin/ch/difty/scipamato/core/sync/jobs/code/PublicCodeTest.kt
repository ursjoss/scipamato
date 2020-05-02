package ch.difty.scipamato.core.sync.jobs.code

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
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

        pc.code shouldBeEqualTo "c"
        pc.langCode shouldBeEqualTo "lc"
        pc.codeClassId shouldBeEqualTo 1
        pc.name shouldBeEqualTo "name"
        pc.comment shouldBeEqualTo "comment"
        pc.sort shouldBeEqualTo 2
        pc.version shouldBeEqualTo 3
        pc.created shouldBeEqualTo CREATED
        pc.lastModified shouldBeEqualTo MODIFIED
        pc.lastSynched shouldBeEqualTo SYNCHED
    }
}
