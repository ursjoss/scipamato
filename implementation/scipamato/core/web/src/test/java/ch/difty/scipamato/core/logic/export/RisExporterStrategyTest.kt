package ch.difty.scipamato.core.logic.export

import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy.DEFAULT
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy.DISTILLERSR
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RisExporterStrategyTest {

    @Test
    fun values() {
        assertThat(RisExporterStrategy.values()).containsExactly(DEFAULT, DISTILLERSR)
    }

    @Test
    fun canParseDefault() {
        assertThat(RisExporterStrategy.fromProperty("DEFAULT", "whatever-key-for-logging-only")).isEqualTo(DEFAULT)
    }


    @Test
    fun canParseDistillerSr() {
        assertThat(RisExporterStrategy.fromProperty("DISTILLERSR", "whatever-key-for-logging-only")).isEqualTo(DISTILLERSR)
    }

    @Test
    @Suppress("SpellCheckingInspection")
    fun gettingStrategyByName_withNotExistingName_returnsDefaultStrategy() {
        assertThat(RisExporterStrategy.fromProperty("ksjdflksjdk", "key")).isEqualTo(DEFAULT)
    }

}
