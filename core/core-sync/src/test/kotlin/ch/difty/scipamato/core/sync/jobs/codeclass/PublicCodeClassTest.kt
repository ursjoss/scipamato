package ch.difty.scipamato.core.sync.jobs.codeclass

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

internal class PublicCodeClassTest {

    @Test
    fun canSetGet() {
        val pcc = PublicCodeClass
            .builder()
            .codeClassId(1)
            .langCode("lc")
            .name("name")
            .description("description")
            .version(2)
            .created(CREATED)
            .lastModified(MODIFIED)
            .lastSynched(SYNCHED)
            .build()

        pcc.codeClassId shouldBeEqualTo 1
        pcc.langCode shouldBeEqualTo "lc"
        pcc.name shouldBeEqualTo "name"
        pcc.description shouldBeEqualTo "description"
        pcc.version shouldBeEqualTo 2
        pcc.created shouldBeEqualTo CREATED
        pcc.lastModified shouldBeEqualTo MODIFIED
        pcc.lastSynched shouldBeEqualTo SYNCHED
    }
}
