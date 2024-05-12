package ch.difty.scipamato.core.logic.export

import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy.DEFAULT
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy.DISTILLERSR
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

internal class RisExporterStrategyTest {

    @Test
    fun values() {
        RisExporterStrategy.entries shouldContainSame listOf(DEFAULT, DISTILLERSR)
    }

    @Test
    fun canParseDefault() {
        val property =
            RisExporterStrategy.fromProperty("DEFAULT", "whatever-key-for-logging-only")
        property shouldBeEqualTo DEFAULT
    }

    @Test
    fun canParseDistillerSr() {
        val property =
            RisExporterStrategy.fromProperty("DISTILLERSR", "whatever-key-for-logging-only")
        property shouldBeEqualTo DISTILLERSR
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun gettingStrategyByName_withNotExistingName_returnsDefaultStrategy() {
        RisExporterStrategy.fromProperty("ksjdflksjdk", "key") shouldBeEqualTo DEFAULT
    }
}
