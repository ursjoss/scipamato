package ch.difty.scipamato.core.sync.jobs.language

import ch.difty.scipamato.core.sync.jobs.SYNCHED
import org.amshove.kluent.shouldBeEqualTo
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

        pp.code shouldBeEqualTo "en"
        pp.mainLanguage shouldBeEqualTo true
        pp.lastSynched shouldBeEqualTo SYNCHED
    }
}
