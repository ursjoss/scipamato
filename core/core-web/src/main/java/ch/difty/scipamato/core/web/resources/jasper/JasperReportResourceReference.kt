package ch.difty.scipamato.core.web.resources.jasper

import ch.difty.scipamato.common.logger
import net.sf.jasperreports.engine.JRException
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperReport
import org.apache.wicket.request.resource.PackageResourceReference
import org.apache.wicket.util.resource.IResourceStream
import org.apache.wicket.util.resource.ResourceStreamNotFoundException
import java.io.InputStream
import java.io.Serializable

private val log = logger()

/**
 * Static resource reference for a jasper report, wrapping the xml report
 * definition (jrxml), but also providing access to the compiled [JasperReport].
 *
 * The compiled report is typically cached, in order to avoid multiple
 * compilation, which is time intensive. However, e.g. when developing the
 * report, you might want to override the caching, so the compilation picks up
 * the latest changes in the report definition file.
 */
abstract class JasperReportResourceReference(
    scope: Class<*>,
    name: String,
    val isCacheReport: Boolean,
) : PackageResourceReference(scope, "$name.jrxml"), Serializable {

    private lateinit var report: JasperReport

    /**
     * @return the compiled [JasperReport] object.
     * @throws JasperReportException an unchecked runtime exception
     *          wrapping two checked exceptions ([ResourceStreamNotFoundException] and [JRException])
     */
    fun getReport(): JasperReport {
        if (!isCacheReport || !this::report.isInitialized) {
            try {
                compileReport()
            } catch (e: JRException) {
                throw JasperReportException(e)
            }
        }
        return report
    }

    @Throws(JRException::class)
    open fun compileReport() {
        report = JasperCompileManager.compileReport(inputStream)
        log.info("Successfully compiled JasperReport $name...")
    }

    private val inputStream: InputStream
        get() {
            val rs = resourceStreamFromResource
            return if (rs != null) {
                try {
                    getInputStream(rs)
                } catch (ex: ResourceStreamNotFoundException) {
                    throw JasperReportException(ex)
                }
            } else {
                throw JasperReportException("Unable to locate resource stream for jasper file '$name'")
            }
        }

    @Throws(ResourceStreamNotFoundException::class)
    open fun getInputStream(rs: IResourceStream): InputStream = rs.inputStream

    open val resourceStreamFromResource: IResourceStream?
        get() = resource.resourceStream

    companion object {
        private const val serialVersionUID = 1L
    }
}
