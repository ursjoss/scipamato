package ch.difty.scipamato.publ

import ch.difty.scipamato.common.logger
import ch.difty.scipamato.common.web.AbstractPage
import ch.difty.scipamato.publ.config.ScipamatoPublicProperties
import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication
import org.apache.wicket.Session
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse
import org.apache.wicket.request.Request
import org.apache.wicket.request.Response
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan

private val log = logger()

@SpringBootApplication
@EnableCaching
@ComponentScan(basePackages = ["ch.difty.scipamato"])
open class ScipamatoPublicApplication(
    private val applicationProperties: ScipamatoPublicProperties,
) : WicketBootSecuredWebApplication() {

    override fun newSession(request: Request, response: Response): Session = ScipamatoPublicSession(request)

    override fun init() {
        // temporarily disable CSP TODO consider activating
        cspSettings.blocking().disabled()
        super.init()

        // enable putting JavaScript into Footer Container
        headerResponseDecorators.add { r: IHeaderResponse? -> JavaScriptFilteredIntoFooterHeaderResponse(r, AbstractPage.FOOTER_CONTAINER) }
        logSpecialConfiguration()
    }

    // package-private for testing purposes
    fun logSpecialConfiguration() {
        if (applicationProperties.isCommercialFontPresent) log.info("CommercialFonts enabled.")
        if (applicationProperties.isResponsiveIframeSupportEnabled) log.info("Responsive iframes using PymJs enabled.")
    }
}

fun main(args: Array<String>) {
    runApplication<ScipamatoPublicApplication>(*args)
}
