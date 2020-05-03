package ch.difty.scipamato.core.web.resources.jasper

import net.sf.jasperreports.engine.JRException
import org.amshove.kluent.shouldBeEqualTo
import org.apache.wicket.util.resource.ResourceStreamNotFoundException
import org.junit.jupiter.api.Test

internal class JasperReportExceptionTest {

    private lateinit var exception: JasperReportException

    @Test
    fun makeJasperReportExceptionWithJRException() {
        val e = JRException("foo")
        exception = JasperReportException(e)
        exception.message shouldBeEqualTo "net.sf.jasperreports.engine.JRException: foo"
    }

    @Test
    fun makeJasperReportExceptionWithResourceStreamNotFoundException() {
        val e = ResourceStreamNotFoundException("foo")
        exception = JasperReportException(e)
        exception.message shouldBeEqualTo "org.apache.wicket.util.resource.ResourceStreamNotFoundException: foo"
    }

    @Test
    fun makeJasperReportException_withStringMessage() {
        exception = JasperReportException("foo")
        exception.message shouldBeEqualTo "foo"
    }
}
