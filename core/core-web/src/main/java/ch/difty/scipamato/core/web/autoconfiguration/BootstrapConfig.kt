package ch.difty.scipamato.core.web.autoconfiguration

import com.giffing.wicket.spring.boot.context.extensions.ApplicationInitExtension
import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration
import de.agilecoders.wicket.core.Bootstrap
import de.agilecoders.wicket.less.BootstrapLess
import de.agilecoders.wicket.themes.markup.html.bootswatch.BootswatchThemeProvider
import org.apache.wicket.protocol.http.WebApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ApplicationInitExtension
@ConditionalOnProperty(prefix = BootstrapProperties.PROPERTY_PREFIX, value = ["enabled"], matchIfMissing = true)
@ConditionalOnClass(BootstrapConfig::class)
@EnableConfigurationProperties(BootstrapProperties::class)
open class BootstrapConfig(private val prop: BootstrapProperties) : WicketApplicationInitConfiguration {
    override fun init(webApplication: WebApplication) {
        BootswatchThemeProvider(prop.theme).also { prop.themeProvider = it }
        Bootstrap.install(webApplication, prop)
        BootstrapLess.install(webApplication)
    }
}
