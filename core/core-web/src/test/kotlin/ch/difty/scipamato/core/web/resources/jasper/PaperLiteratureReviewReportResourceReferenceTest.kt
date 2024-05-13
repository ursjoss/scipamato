package ch.difty.scipamato.core.web.resources.jasper

import io.mockk.mockk
import net.sf.jasperreports.engine.JRException
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.withMessage
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.ResourceStreamNotFoundException
import org.junit.jupiter.api.Test
import java.io.InputStream

internal class PaperLiteratureReviewReportResourceReferenceTest : JasperReportResourceReferenceTest<PaperLiteratureReviewReportResourceReference>() {

    override val resourceReference: PaperLiteratureReviewReportResourceReference
        get() = PaperLiteratureReviewReportResourceReference.get()

    override val reportBaseName: String
        get() = "paper_literature_review_A4"

    override val resourceReferencePath: String
        get() = "ch.difty.scipamato.core.web.resources.jasper.PaperLiteratureReviewReportResourceReference"

    @Test
    fun gettingResourceStream_withNullStream() {
        val rr: JasperReportResourceReference = object : JasperReportResourceReference(
            PaperLiteratureReviewReportResourceReference::class.java, "baz", false
        ) {
            private val serialVersionUID: Long = 1L
            override val resourceStreamFromResource: IResourceStream?
                get() = null
        }
        invoking { rr.getReport() } shouldThrow
            JasperReportException::class withMessage "Unable to locate resource stream for jasper file 'baz.jrxml'"
    }

    @Test
    fun gettingResourceStream_withResourceStreamNotFoundException() {
        val rr: JasperReportResourceReference = object : JasperReportResourceReference(
            PaperLiteratureReviewReportResourceReference::class.java, "baz", false
        ) {
            private val serialVersionUID: Long = 1L
            override val resourceStreamFromResource: IResourceStream
                get() = mockk()

            override fun getInputStream(rs: IResourceStream): InputStream =
                throw ResourceStreamNotFoundException("boom")
        }
        invoking { rr.getReport() } shouldThrow JasperReportException::class withMessage
            "org.apache.wicket.util.resource.ResourceStreamNotFoundException: boom"
    }

    @Test
    fun compilingReport_throwingJRException() {
        val rr: JasperReportResourceReference = object : JasperReportResourceReference(
            PaperLiteratureReviewReportResourceReference::class.java, "baz", false
        ) {
            private val serialVersionUID: Long = 1L
            override fun compileReport(): Unit = throw JRException("boom")
        }
        invoking { rr.getReport() } shouldThrow JasperReportException::class withMessage
            "net.sf.jasperreports.engine.JRException: boom"
    }
}
