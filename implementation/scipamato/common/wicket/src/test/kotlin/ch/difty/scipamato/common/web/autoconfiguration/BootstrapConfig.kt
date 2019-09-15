package ch.difty.scipamato.common.web.autoconfiguration

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration
import de.agilecoders.wicket.core.Bootstrap
import org.apache.wicket.protocol.http.WebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@ApplicationInitExtension
@ConditionalOnProperty(prefix = "wicket.external.agilecoders.bootstrap", value = ["enabled"], matchIfMissing = true)
@ConditionalOnClass(BootstrapConfig::class)
open class BootstrapConfig : WicketApplicationInitConfiguration {

    override fun init(webApplication: WebApplication) {
        Bootstrap.install(webApplication)
    }

}
