package ch.difty.scipamato.core

import ch.difty.scipamato.common.web.AbstractPage
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

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@EnableFeignClients
@EnableCaching
@ComponentScan(basePackages = ["ch.difty.scipamato"])
open class ScipamatoCoreApplication : WicketBootSecuredWebApplication() {
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
}

fun main(args: Array<String>) {
    runApplication<ScipamatoCoreApplication>(*args)
}
