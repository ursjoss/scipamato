package ch.difty.scipamato.core.web.paper.jasper

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test

internal class ScipamatoPdfResourceHandlerTest {

    @Test
    fun testWithDefaultConfig() {
        val h = ScipamatoPdfResourceHandler()
        h.contentType shouldBeEqualTo "application/pdf"
        h.extension shouldBeEqualTo "pdf"
        validateDefaultConfig(h)
    }

    private fun validateDefaultConfig(h: ScipamatoPdfResourceHandler) {
        val exporter = h.newExporter()
        exporter.shouldNotBeNull()
        exporter.exporterKey shouldBeEqualTo "net.sf.jasperreports.pdf"
        exporter.exporterPropertiesPrefix shouldBeEqualTo "net.sf.jasperreports.export.pdf."
    }

    @Test
    fun testWithExplicitConfigNull() {
        val h = ScipamatoPdfResourceHandler(null)
        validateDefaultConfig(h)
    }

    @Test
    fun testWithExplicitConfig() {
        val c: ClusterablePdfExporterConfiguration = ScipamatoPdfExporterConfiguration.Builder("hp", 1L)
            .withAuthor("a")
            .build()
        val h = ScipamatoPdfResourceHandler(c)
        validateDefaultConfig(h)
    }
}
