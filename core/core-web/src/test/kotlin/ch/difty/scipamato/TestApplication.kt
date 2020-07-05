package ch.difty.scipamato

import ch.difty.scipamato.core.ScipamatoSession
import ch.difty.scipamato.core.web.paper.list.PaperListPage
import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication
import org.apache.wicket.Page
import org.apache.wicket.Session
import org.apache.wicket.markup.head.IHeaderResponse
import org.apache.wicket.markup.head.ResourceAggregator
import org.apache.wicket.markup.head.filter.JavaScriptFilteredIntoFooterHeaderResponse
import org.apache.wicket.markup.html.SecurePackageResourceGuard
import org.apache.wicket.request.Request
import org.apache.wicket.request.Response
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class TestApplication : WicketBootSecuredWebApplication() {

    override fun getHomePage(): Class<out Page?> = PaperListPage::class.java

    override fun init() {
        super.init()

        /**
         * Inject a customized AjaxRequestTarget implementation into the application
         * that scales better with many listeners and therefore massively improves the
         * build time.
         */
        setAjaxRequestTargetProvider { page -> TestAjaxRequestHandler(page) }

        // enable putting JavaScript into Footer Container
        setHeaderResponseDecorator { r: IHeaderResponse? ->
            ResourceAggregator(JavaScriptFilteredIntoFooterHeaderResponse(r, "footer-container"))
        }
        registerJasperJrxmlFilesWithPackageResourceGuard()
    }

    // Allow access to jrxml jasper report definition files
    private fun registerJasperJrxmlFilesWithPackageResourceGuard() {
        val packageResourceGuard = resourceSettings.packageResourceGuard
        if (packageResourceGuard is SecurePackageResourceGuard) {
            packageResourceGuard.addPattern("+*.jrxml")
        }
    }

    override fun newSession(request: Request, response: Response): Session = ScipamatoSession(request)
}
