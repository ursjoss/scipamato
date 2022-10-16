package ch.difty.scipamato.core

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.core.web.sync.ISyncTask
import ch.difty.scipamato.core.web.sync.TasksRunnable
import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication
import org.apache.wicket.Session
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse
import org.apache.wicket.markup.html.SecurePackageResourceGuard
import org.apache.wicket.request.Request
import org.apache.wicket.request.Response
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val log = logger()

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EnableFeignClients
@EnableCaching
@ComponentScan(basePackages = ["ch.difty.scipamato"])
open class ScipamatoCoreApplication : WicketBootSecuredWebApplication() {

    private val executorService = Executors.newSingleThreadExecutor()

    override fun init() {
        // TODO consider making it CSP compliant
        cspSettings.blocking().disabled()

        super.init()

        headerResponseDecorators.add { JavaScriptFilteredIntoFooterHeaderResponse(it, AbstractPage.FOOTER_CONTAINER) }
        registerJasperJrxmlFilesWithPackageResourceGuard()
    }

    // Allow access to jrxml jasper report definition files
    private fun registerJasperJrxmlFilesWithPackageResourceGuard() {
        resourceSettings.packageResourceGuard?.run {
            if (this is SecurePackageResourceGuard) addPattern("+*.jrxml")
        }
    }

    override fun newSession(request: Request, response: Response): Session = ScipamatoSession(request)

    @Suppress("MagicNumber")
    override fun onDestroy() {
        executorService.shutdown()
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                executorService.shutdownNow()
        } catch (e: InterruptedException) {
            log.error("Unable to shutdown executor service", e)
        }
        super.onDestroy()
    }

    @Synchronized
    open fun launchSyncTask(task: ISyncTask) {
        val sjr = ScipamatoSession.get().syncJobResult
        sjr.clear()
        executorService.execute(TasksRunnable(task, sjr::setSuccess, sjr::setFailure, sjr::setWarning, sjr::setDone))
    }

    companion object {
        fun getApplication() = get() as ScipamatoCoreApplication
    }
}


@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<ScipamatoCoreApplication>(*args)
}
