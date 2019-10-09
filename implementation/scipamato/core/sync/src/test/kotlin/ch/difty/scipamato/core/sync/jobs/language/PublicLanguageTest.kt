package ch.difty.scipamato.core.sync.jobs.language

import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PublicLanguageTest {

    @Test
    fun canSetGet_withStandardFieldsPopulated() {
        val pp = PublicLanguage
            .builder()
            .code("en")
            .mainLanguage(true)
            .lastSynched(SYNCHED)
            .build()

        assertThat(pp.code).isEqualTo("en")
        assertThat(pp.mainLanguage).isEqualTo(true)
        assertThat(pp.lastSynched).isEqualTo(SYNCHED)
    }
}
