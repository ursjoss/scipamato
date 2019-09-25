package ch.difty.scipamato.core.logic.export

import ch.difty.scipamato.core.logic.exporting.DefaultRisAdapter
import ch.difty.scipamato.core.logic.exporting.DistillerSrRisAdapter
import ch.difty.scipamato.core.logic.exporting.RisAdapterFactory
import ch.difty.scipamato.core.logic.exporting.RisExporterStrategy
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RisAdapterFactoryTest {

    @Test
    fun defaultRisExporter() {
        val factory = RisAdapterFactory.Companion.create(RisExporterStrategy.DEFAULT)
        val risAdapter = factory.createRisAdapter("brand", "iu", "pu")
        assertThat(risAdapter).isInstanceOf(DefaultRisAdapter::class.java)
    }


    @Test
    fun distillerSrRisExporter() {
        val factory = RisAdapterFactory.Companion.create(RisExporterStrategy.DISTILLERSR)
        val risAdapter = factory.createRisAdapter("brand", "iu", "pu")
        assertThat(risAdapter).isInstanceOf(DistillerSrRisAdapter::class.java)
    }

}