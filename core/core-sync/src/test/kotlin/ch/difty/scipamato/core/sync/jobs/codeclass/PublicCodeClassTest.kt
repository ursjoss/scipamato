package ch.difty.scipamato.core.sync.jobs.codeclass

import ch.difty.scipamato.core.sync.jobs.CREATED
import ch.difty.scipamato.core.sync.jobs.MODIFIED
import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
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

        assertThat(pcc.codeClassId).isEqualTo(1)
        assertThat(pcc.langCode).isEqualTo("lc")
        assertThat(pcc.name).isEqualTo("name")
        assertThat(pcc.description).isEqualTo("description")
        assertThat(pcc.version).isEqualTo(2)
        assertThat(pcc.created).isEqualTo(CREATED)
        assertThat(pcc.lastModified).isEqualTo(MODIFIED)
        assertThat(pcc.lastSynched).isEqualTo(SYNCHED)
    }
}
